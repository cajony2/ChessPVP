package com.example.roma.servertest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/*
    this is the game himself display the game board with all the pieces and make moves
    contact the server and receive the new board after the second player has made his move
    we will have this activity from the begining of the game till the end!!!

 */
public class GameBoard extends Activity implements AdapterView.OnItemClickListener ,View.OnClickListener {

    ArrayList<Piece> whiteEaten;
    ArrayList<Piece> blackEaten;
    EatenAdapter eatenAdapter;
    EatenAdapter eatenAdapterUp;
    GridView whiteEatenPieces;
    GridView blackEatenPieces;
    ArrayList<Piece> pieces;
    boolean[] possibleMove;
    String color;
    String userName;
    String psw;
    Boolean isSelected;
    Adapter adapter;
    GridView gv;
    Game game;
    TextView player1;
    TextView player2;
    TextView timerTextView;
    Button submit;
    boolean moveMade;
    boolean canClick;   // ca the user select some tiles in chess board

    public static final String MAKE_MOVE = "makeMove";
    public static final String GAME_READY = "isGameReady";
    public static final String MOVE_MADE = "moveMade";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_activity);

        player1 = (TextView) findViewById(R.id.player1);
        player2 = (TextView) findViewById(R.id.player2);
        timerTextView = (TextView) findViewById(R.id.timer);
        whiteEatenPieces = (GridView) findViewById(R.id.eatenpieces);
        blackEatenPieces = (GridView) findViewById(R.id.eatenpiecesup) ;
        canClick=true;
        pieces = null;
        game=null;
        Intent intent = getIntent();

        userName = intent.getStringExtra("userName");
        String gameJson = intent.getStringExtra("game");
        String action  = intent.getStringExtra("ACTION");

        Log.i("chess","gameBoard created , action: "+action);

        gv = (GridView) findViewById(R.id.chessboard);
        submit = (Button) findViewById(R.id.submit);
        moveMade = false;
        isSelected =false;
        possibleMove = new boolean[64];

        /*switch (action){
            case "fullGame":*/
        if (action.equals("fullGame")) {
            Game game = null;
            try {
                game = new Game(new JSONObject(gameJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (game != null) {

                //jony added. flipps board and pieces accordingly
                if (game.getTurn().equals("jonjony"))//getTurn should return int
                    flipBoard(game);

                adapter = new Adapter(this, game, null);
                gv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                player1.setText(game.getPlayer1());
                player2.setText(game.getPlayer2());
                Log.i("chess", "game created shpud work");
                Log.i("chess", "player1: " + game.getPlayer1());
                addEatenPieces(game.getEatenPieces());
                Timer timer = new Timer(60, timerTextView);
                timer.start();
                gv.setOnItemClickListener(this);
                submit.setOnClickListener(this);
            }
        }
               /* break;
        }*/
    }

    private void flipBoard(Game game)
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
    }


    private void addEatenPieces(ArrayList<Piece> eatenPieces) {
        // set eaten pieces

        whiteEaten = new ArrayList<Piece>();
        blackEaten = new ArrayList<Piece>();

            for (Piece eaten : eatenPieces) {
                if (eaten.getColor().equals("black"))
                    blackEaten.add(eaten);
                else
                    whiteEaten.add(eaten);
            }

        //TODO check the color of the player and set eaten adapter appropriate , for no show white in the bottom
        eatenAdapter = new EatenAdapter(this,blackEaten);
        eatenAdapterUp = new EatenAdapter(this,whiteEaten);

        whiteEatenPieces.setAdapter(eatenAdapter);
        blackEatenPieces.setAdapter(eatenAdapterUp);
    }

    @Override
    // listener  for the gridview when the user clicks a tile check if the tile contains a piece and
    // the piece is of the legal color and create a legalmoves array and update adapter and UI
    //if the click is after we selected a piece   check if the move is legal and if so make the move
    // update adapter and UI and disable the touch
    //THIS METHOD CREATED ONLY FOR TESTING NEED ALOT OF CHANGING!!!!!!!!!!
    //TODO finish this method :)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Game game = adapter.getGame();
        Piece[] pieces = game.getBoard2();
        int selectedTile=adapter.getSelectedTile();

        ArrayList<Integer> moves ;
        // do only if the square clicked is the users color
        if(canClick) {
            if (!isSelected) {
                Log.i("chess", "username: " + userName + " player1: " + game.getPlayer1());
                if (!(game.getPlayer1().equals(userName) == pieces[position].getColor().equals("white"))) {         //then this user is white
                    isSelected = true;
                    moves = pieces[position].getLegalMoves(game);
                    if (moves != null) {
                        for (int pos : moves) {
                            possibleMove[pos] = true;
                        }
                        adapter.setSelectedTile(position);
                        adapter.setPossibleMoves(possibleMove);
                    }
                }
            } else {                        //selected maybe a move
                if (selectedTile >= 0) {   //making a move
                    if (possibleMove[position]) {    // this move is legal
                        pieces[position] = pieces[selectedTile];
                        pieces[position].setPosition(position);

                        pieces[selectedTile] = new Empty("empty", "white", selectedTile);
                        pieces[selectedTile].setEmpty(true);

                        adapter.setGame(game);
                        moveMade = true;
                        Log.i("chess", "move made");
                        adapter.setSelectedTile(-1);
                    }
                }
                isSelected = false;
                for (int i = 0; i < possibleMove.length; i++)
                    possibleMove[i] = false;
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void test(){

    }
    //if the user clicks the SUBMIT button send a move to the server and disable the touch
    /*
    TODO CANT ACCESS GAME OBJECT FROM ONCLICK NEED TO FIX THIS !!!!!
     */
    @Override
    public void onClick(View v) {
        if(game==null){
            Log.i("chess","*******chess******");
        }
        if(moveMade){
            Log.i("chess","clicked make move");
            new ReadFromDB(this , MAKE_MOVE,game).execute();
            //lock all click listeners
            for(int i =0 ; i<63 ; i++ )
                adapter.isEnabled(i);

        }else{
            Toast toast = Toast.makeText(this, "Make a move", Toast.LENGTH_LONG);
            toast.show();
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
       JSONObject gameJson = null;
       int status=-1;
       try {
            gameJson = new JSONObject(str);
           status = gameJson.getInt("status");
       } catch (JSONException e) {
           e.printStackTrace();
       }

       switch (status){
           case 1:                      // opponent made a move, just update UI, enable back the adapter and reset timer

               break;
           case 2:                      //check , player must get out of check , update UI enable back the adapter resert timer SET POSSIBLE MOVES!!!!!

               break;
           case 3:                      // checkMate , finish game

               break;
           default:                      // error
               break;

       }
   }
    /*
             TODO check if checkMate if so end game.
     */


    /*
        Created by Roma
        send request to server

     */
    class ReadFromDB extends AsyncTask< Void, Void, String> {
        Activity activity;
        String action;
        Game g;

        //constructor
        public ReadFromDB(Activity _e, String _action){
            activity=_e;
            action = _action;
        }

        public ReadFromDB(Activity _e, String _action, Game _game){
            activity=_e;
            action = _action;
            g=_game;
            if(game==null)
                Log.i("chess","game is null god knows why");

        }

        @Override
        protected String doInBackground(Void... params) {
            String message="";
            try{
                URL url = new URL("http://5.29.207.103:8080/Chess/ChessServlet");

                URLConnection connection = url.openConnection();
                connection.setRequestProperty("Action",action);
                Log.i("chess", "connecting to db:"+action);

                connection.setRequestProperty("UserName", userName);
                connection.setDoOutput(true);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                if (action.equals(MAKE_MOVE)) {
                    if(g!=null) {
                        JSONObject json = g.toJson();
                        Log.i("chess", "json:" + json.toString());
                        out.write(json.toString());
                    }else
                        Log.i("chess", "game is null");
                }
                else
                    out.write("");

                out.close();

                // get response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String nextLine;

                while ((nextLine = in.readLine()) != null) {
                    message += nextLine;
                }

                Log.d("chess","the return message "+message);
                in.close();
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
            return message;
        }

        @Override


        public void onPostExecute(String message){

            if(action.equals(MAKE_MOVE)) {
                makeMoveOnPostExecute(message);
            }
            else if(action.equals("No")){           //opponent didnt made his move yet , ask again in 1000 ms
                askAgainOnPostExecute();
            }else{                                  // else the msg is json with the new game
                refreshGame(message);
            }


        }

        private void askAgainOnPostExecute(){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new ReadFromDB(activity , GAME_READY).execute();

        }

        /*
       Created By Roma
           check the servers response
           if response is moveMade start a loop of requests to get the game object after the
           opponent made his move by executing a new ReadFromDB thread with action "isGameReady"
        */
        private void makeMoveOnPostExecute(String msg){
            if (msg.equals(MOVE_MADE)){
                //freeze UI in main thread TODO
                canClick=false;

                //start new Thread ReadFromDB
                new ReadFromDB(activity , GAME_READY).execute();
            }
            else{
                //TODO check Error
            }
        }

    }//end of inner class ReadFromDB



}


