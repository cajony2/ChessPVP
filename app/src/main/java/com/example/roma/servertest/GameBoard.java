package com.example.roma.servertest;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import layout.Board;
import layout.BottomInfo;
import layout.TimerBar;
import layout.TopInfo;


/*
    this is the game himself display the game board with all the pieces and make moves
    contact the server and receive the new board after the second player has made his move
    we will have this activity from the begining of the game till the end!!!

 */
public class GameBoard extends Activity implements Communicator {

    private static final int OK_GAME = 1;

    private final int YOU_LOOSE =1;
    private final int OPPONENT_LOOSE=2;
    private final int CHCKMATE =3;
    private final int TIMEROVER=4;
    private final int CHECK = 2;

    ArrayList<Piece> whiteEaten;            // need to move this to game object
    ArrayList<Piece> blackEaten;            // need to move this to game object
    String color;
    String userName;
    private Game game;
    int myColor;
    private boolean firstTime;
    Dialog dialog;
    Button okButton;
    ProgressBar spinner;
    boolean win;
    private String psw;
    boolean moveMade;
    boolean endGame;
    Piece[] pieces;





    public static final String GAME_READY = "isGameReady";
    public static final String MOVE_MADE = "moveMade";
    public static final String YOU_WON = "youWon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_activity);
        Intent intent = getIntent();
        //this is the name of the player that playes this game
        userName = intent.getStringExtra("userName");           // username ==player1 - this player is playing the white pieces
        psw =intent.getStringExtra("password");
        String gameJson = intent.getStringExtra("game");

        firstTime=true;
        moveMade=false;
        endGame = false;
        // create the game from string extra "game"
        try {
            game = new Game(new JSONObject(gameJson));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //set players color
        myColor = (userName.equals(game.getPlayer1())) ? Color.WHITE : Color.BLACK;
        addEatenPieces(game.getEatenPieces());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("chess","in on start");
        if(firstTime  && !this.canClick()){
            new ReadFromDB(this, GAME_READY, userName, game,psw).execute();
        }
        firstTime=false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        endGame();
    }

    private void addEatenPieces(ArrayList<Piece> eatenPieces) {
        // set eaten pieces
        whiteEaten = new ArrayList<>();
        blackEaten = new ArrayList<>();

            for (Piece eaten : eatenPieces) {
                if (eaten.getColor().equals("black"))
                    blackEaten.add(eaten);
                else
                    whiteEaten.add(eaten);
            }
    }
    /* TODO
        after receiving the new game object  after the opponent made his move
        if check then player must get out of check
        if checkMate  end game update score, send server that u r a looser

        if regular move
        update UI -     tiles( the pieces the opponent move and/or ate)
                        eatenPieces - update eaten adapters
                        reset timer

     */
   public void refreshGame(String str)  {
       try {
           game = new Game(new JSONObject(str));
           updateStatus();
           int status=game.getStatus();
           switch (status){
               case OK_GAME:                      // opponent made a move, just update UI, enable back the adapter and reset timer
                    updateUI();
                   break;
               case CHECK:                      //check , player must get out of check , update UI enable back the adapter resert timer SET POSSIBLE MOVES!!!!!
                    showCheckToast();
                    updateUI();
                   break;
               case 3:                      // checkMate , finish game

                   break;
               default:                      // error
                   break;

           }
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }

    private void showCheckToast() {
        Toast toast = Toast.makeText(this, "Get out of Check", Toast.LENGTH_LONG);
        toast.show();
    }

    private void updateUI( ) {
        Log.i("chess","in updateUI");
        FragmentManager fManager = getFragmentManager();

        //update pieces
        Board boardFragment  = (Board) fManager.findFragmentById(R.id.board);
        boardFragment.refresh(game.getBoard2());
        boardFragment.setMoveMade(false);

        //update eaten pieces
        addEatenPieces(game.getEatenPieces());
        TopInfo topFragment = (TopInfo) fManager.findFragmentById(R.id.topFragment);
        BottomInfo bottomFragment = ( BottomInfo) fManager.findFragmentById(R.id.bottomFragment);
        if(myColor==Color.BLACK) {
            topFragment.refresh(blackEaten);
            bottomFragment.refresh(whiteEaten);
        }else {
            topFragment.refresh(whiteEaten);
            bottomFragment.refresh(blackEaten);
        }

        //reset timer
        TimerBar timerBarFragment = (TimerBar) fManager.findFragmentById(R.id.timerBarFragment);
        if(timerBarFragment==null)
            Log.i("chess","timer is null");


            timerBarFragment.reset();
    }

    @Override
    public String getOpponentName() {
        if(game!=null) {
            if (myColor == Color.WHITE)
                return game.getPlayer2();
            else
                return game.getPlayer1();
        }
        else
            return "OpponentName";
    }

    public String getUserName(){
        if (game!=null) {
            if (myColor == Color.BLACK)
                return game.getPlayer2();
            else
                return game.getPlayer1();
        }
        else
            return "UserName";
    }

    @Override
    public ArrayList<Piece> getEatenPieces(int color) {
        if(color==Color.BLACK)
            return blackEaten;
        else
            return whiteEaten;
    }

    public void undo() {
        FragmentManager fManager = getFragmentManager();
        Board boardFragment  = (Board) fManager.findFragmentById(R.id.board);
        pieces = boardFragment.undoClicked();
    }

    @Override
    public boolean getWin() {
        return win;
    }

    @Override
    public void makeMove() {
        FragmentManager fManager = getFragmentManager();

        //update pieces
        Board boardFragment  = (Board) fManager.findFragmentById(R.id.board);

        moveMade =  boardFragment.getMoveMade();

        if(moveMade) {
            if (pieces != null)
                game.setPieces(pieces);
            ReadFromDB read = new ReadFromDB(this,ReadFromDB.MAKE_MOVE,userName,game,psw);
            read.execute();
        }else {
            Toast toast = Toast.makeText(this, "game is null", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void updateStatus(){
        FragmentManager fManager = getFragmentManager();

        //update pieces
        Board boardFragment  = (Board) fManager.findFragmentById(R.id.board);
        if(boardFragment.isChess() == CHECK) {
            Log.i("chess","check!!!");
            game.setStatus(CHECK);
        }
        else if (boardFragment.isChess() == CHCKMATE)//check mate
        {
            Log.i("chess","check mate!!!");
            game.setStatus(CHCKMATE);
        }
        else
        {
            Log.i("chess","No check :) !!!");
            game.setStatus(OK_GAME);
        }
    }

    @Override
    public Piece[][] getPieces() {
        return  game.getGridPieces();
    }

    @Override
    public Piece[] getPiecesOld() {
        return game.getBoard2();
    }

    @Override
    public void setEatenPiece(Piece p) {
       game.addEatenPiece(p);
    }

    @Override
    public void erasePieceFromEatenPieces(Piece p) {
        game.erasePieceFromEatenPieces(p);
    }

    @Override
    public int getColor() {
        return myColor;
    }

    @Override
    public void setGame(String gameJson) {
        refreshGame(gameJson);
    }


    @Override
    public void moveMade(String answerType, String message) {
        switch (answerType) {
            case MOVE_MADE:                                         //servlet received players move
                Log.i("chess", "move make was successful");
                FragmentManager fManager = getFragmentManager();
                TimerBar timer   = (TimerBar) fManager.findFragmentById(R.id.timerBarFragment);
                try {
                    game = new Game(new JSONObject(message));
                    timer.reset();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                new ReadFromDB(this, GAME_READY, userName, game,psw).execute();             // check if opponent made his move

                break;
            case ReadFromDB.GAME_NOT_READY:                                                                        //check if opponent made his move
                if(!endGame)
                    new ReadFromDB(this, GAME_READY, userName, game,psw).execute();
                break;
            case ReadFromDB.GAME_IS_READY:
                Log.i("chess", "game is ready and gameJson is:" + game.toJson());
                setGame(message);
                break;
            case YOU_WON:
                showDialog(OPPONENT_LOOSE, TIMEROVER);
                endGame();
                break;
        }
    }

    /*
        if timer is done , show the propper message and end game
     */
    @Override
    public void timerFinished() {

        if(game.getTurn().equals(userName)) {
            win = false;
            showDialog(YOU_LOOSE, TIMEROVER);
            endGame();
        }
        else {
            win = true;
        }
        endGame=true;
    }
    /*
        by Roma
        show end Game Dialog
     */
    private void showDialog(int looser , int looseType){
        dialog = new Dialog(this ,R.style.myCoolDialog);
        dialog.setTitle("Game Over");
        dialog.setContentView(R.layout.end_game_dialog);

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        TextView messageTextView  = (TextView) dialog.findViewById(R.id.endGameTextView);
        okButton = (Button) dialog.findViewById(R.id.endGameOkButton);
        spinner = (ProgressBar) dialog.findViewById(R.id.EndGameProgressBar);
        okButton.setVisibility(View.GONE);
        String message="";

        switch (looseType){
            case CHCKMATE: message+="CheckMate! - "; break;
            case TIMEROVER: message+="Time Over! - "; break;
        }
        switch (looser){
            case YOU_LOOSE: message+="You Loose!";break;
            case OPPONENT_LOOSE: message+="You Win!";break;
        }

        messageTextView.setText(message);
    }

    @Override
    public boolean canClick() {
        Log.i("chess","turn:"+game.getTurn());
        return userName.equals(game.getTurn());
    }


    public void endGame() {
        if(game!=null) {
            ReadFromDB read = new ReadFromDB(this, ReadFromDB.ENDGAME, userName, game, psw);
            read.execute();
        }
    }
    @Override
    public void gameEnded() {
        if(game!=null) {
            ReadFromDB read = new ReadFromDB(this, ReadFromDB.GETINFO, userName, game, psw);
            read.execute();
        }
    }

    @Override
        public void setInfo(final String message) {
            okButton.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startPersonalActivity(message);
                }
            });

        }
    private void startPersonalActivity(String infoJson){
        Intent intent = new Intent(this, PersonalInfo.class);
        intent.putExtra("JSON", infoJson);
        intent.putExtra("password",psw);
        startActivity(intent);
    }
}


