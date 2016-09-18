package com.example.roma.servertest;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

    private final int TILES_NUMBER_IN_A_ROW = 8;

    ArrayList<Piece> whiteEaten;            // need to move this to game object
    ArrayList<Piece> blackEaten;            // need to move this to game object
    String color;
    String userName;
    private Game game;
    int myColor;
    private boolean firstTime;






    public static final String GAME_READY = "isGameReady";
    public static final String MOVE_MADE = "moveMade";
    public static final String USER_NAME  = "userName";
    public static final String PSW = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_activity);
        Intent intent = getIntent();
        //this is the name of the player that playes this game
        userName = intent.getStringExtra("userName");           // username ==player1 - this player is playing the white pieces
        String gameJson = intent.getStringExtra("game");
        String action = intent.getStringExtra("ACTION");

        Log.i("chess", "gameBoard created , action: " + action);
        firstTime=true;
        // create the game from string extra "game"
        try {
            game = new Game(new JSONObject(gameJson));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //set players color
        myColor = (userName.equals(game.getPlayer1())) ? Color.WHITE : Color.BLACK;
        addEatenPieces(game.getEatenPieces());


        // if player is white then he starts the game
        if (action.equals("fullGame")) {
        }
        else if(action.equals("joinedGame")){
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("chess","in on start");
        if(firstTime  && !this.canClick()){
            new ReadFromDB(this, GAME_READY, userName, game).execute();
        }
        firstTime=false;
    }

    /*private void flipBoard(Game game)
    {
        View v = findViewById(R.id.linearlayountchessboard);//the view in which the board is in
        v.setRotationX(180);
        rotatePieces(game);
    }

    private void rotatePieces(Game game) {
        Piece[] pieces = game.getBoard2();
        for (Piece p : pieces)
        {
            p._isFlipped = true;
        }
    }*/


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

           int status=game.getStatus();
           switch (status){
               case 1:                      // opponent made a move, just update UI, enable back the adapter and reset timer
                    updateUI();
                   break;
               case 2:                      //check , player must get out of check , update UI enable back the adapter resert timer SET POSSIBLE MOVES!!!!!

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

    private void updateUI( ) {
        Log.i("chess","in updateUI");
        FragmentManager fManager = getFragmentManager();

        //update pieces
        Board boardFragment  = (Board) fManager.findFragmentById(R.id.board);
        boardFragment.refresh(game.getBoard2());

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

        if(game.getTurn().equals(userName))
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

    @Override
    public void makeMove() {
        if(game==null) {
            Toast toast = Toast.makeText(this, "game is null", Toast.LENGTH_LONG);
            toast.show();
        }else {
            ReadFromDB read = new ReadFromDB(this,ReadFromDB.MAKE_MOVE,userName,game);
            read.execute();
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
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new ReadFromDB(this, GAME_READY, userName, game).execute();             // check if opponent made his move
                break;
            case ReadFromDB.GAME_NOT_READY:                                                                        //check if opponent made his move
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new ReadFromDB(this, GAME_READY, userName, game).execute();
                break;
            case ReadFromDB.GAME_IS_READY:
                Log.i("chess", "game is ready and gameJson is:" + game.toJson());
                setGame(message);
                break;
        }
    }

    @Override
    public void timerFinished() {
        Log.i("chess","Times up!");
    }

    @Override
    public boolean canClick() {
        return userName.equals(game.getTurn());
    }
}


