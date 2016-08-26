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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_activity);

        player1 = (TextView) findViewById(R.id.player1);
        player2 = (TextView) findViewById(R.id.player2);
        timerTextView = (TextView) findViewById(R.id.timer);

        Intent intent = getIntent();
        pieces = null;
        game=null;

        userName = intent.getStringExtra("userName");

        String gameJson = intent.getStringExtra("game");
        String action  = intent.getStringExtra("ACTION");

        Log.i("chess","gameBoard created , action: "+action);

        gv = (GridView) findViewById(R.id.chessboard);
        submit = (Button) findViewById(R.id.submit);

        moveMade = false;
        isSelected =false;

        possibleMove = new boolean[64];//i think it gets false values at initialization (default values)

        //maybe unnecessary
        for(int i=0 ; i<possibleMove.length ; i++){
            possibleMove[i]=false;
        }

        switch (action){
            case "fullGame":
                Game game = null;
                try {
                    game = new Game(new JSONObject(gameJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(game!=null) {
                    adapter = new Adapter(this, game, null);
                    gv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    player1.setText(game.getPlayer1());
                    player2.setText(game.getPlayer2());
                    Log.i("chess", "game created shpud work");
                    Log.i("chess", "player1: " + game.getPlayer1());

                }
                break;
        }


        gv.setOnItemClickListener(this);
        submit.setOnClickListener(this);
    }


    @Override
    // listener  for the gridview when the user clicks a tile check if the tile contains a piece and
    // the piece is of the legal color and create a legalmoves array and update adapter and UI
    //if the click is after we selected a piece   check if the move is legal and if so make the move
    // update adapter and UI and disable the touch
    //THIS METHOD CREATED ONLY FOR TESTING NEED ALOT OF CHANGING!!!!!!!!!!
    //TODO finish this method :)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("ITEMCLICKED","item number "+position);

        Game game = adapter.getGame();
        Piece[] pieces = game.getBoard2();
        int selectedTile=adapter.getSelectedTile();

        ArrayList<Integer> moves ;
        // do only if the square clicked is the users color
        if (!isSelected) {
            Log.i("chess","username: "+userName+" player1: "+game.getPlayer1());
            if(!(game.getPlayer1().equals(userName) ^ pieces[position].getColor().equals("white"))) {         //then this user is white
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
            if(selectedTile>=0){   //making a move
                if(possibleMove[position]) {    // this move is legal
                    pieces[position] = pieces[selectedTile];
                    pieces[position].setPosition(position);

                    pieces[selectedTile] = new Empty("empty","white",selectedTile);
                    pieces[selectedTile].setEmpty(true);

                    adapter.setGame(game);
                    moveMade = true;
                    Log.i("chess","move made");
                    adapter.setSelectedTile(-1);
                }

            }
            isSelected = false;
            for (int i = 0; i < possibleMove.length; i++)
                possibleMove[i] = false;
        }

        adapter.notifyDataSetChanged();
    }

    @Override

    //if the user clicks the SUBMIT button send a move to the server and disable the touch
    public void onClick(View v) {
        if(moveMade){
            Log.d("chess","clicked submit");
            new ReadFromDB(this,"makeMove").execute();
            //lock all click listeners
            for(int i =0 ; i<63 ; i++ )
                adapter.isEnabled(i);

        }else{
            Toast toast = Toast.makeText(this, "Make a move", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    // since we have the the middle layer "initgame" we won't be needing these for now
    // i leave it for other server connections (make a move get newBoard end game and so on...)
    class ReadFromDB extends AsyncTask< Void, Void, String> {
        Activity activity;
        String action;

        //constructor
        public ReadFromDB(Activity _e, String _action){
            activity=_e;
            action = _action;
        }

        @Override
        protected String doInBackground(Void... params) {
            String message="";
            try{
                URL url = new URL("http://5.29.207.103:8080/Chess/ChessServlet");
                // URL url = new URL("http://10.0.2.2:8080/Chess/ChessServlet");

                URLConnection connection = url.openConnection();
                connection.setRequestProperty("Action",action);
                Log.d("chess", "connecting to db:"+action);

                connection.setRequestProperty("UserName", userName);
                connection.setRequestProperty("Password", psw);
                connection.setDoOutput(true);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                if (action.equals("makeMove")) {
                    JSONObject jsonsss = game.toJson();
                    Log.d("chess" , "json:"+jsonsss.toString()) ;
                    out.write(jsonsss.toString());
                }
                else
                    out.write("no body");

                out.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String nextLine;

                while ((nextLine = in.readLine()) != null) {
                    message += nextLine;
                }

                //Log.d("chess","the return message "+message);
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

            if(!action.equals("makeMove")) {
                try
                {
                    game = new Game(new JSONObject(message));

                    if (game != null) {
                        adapter = new Adapter(activity, game, null);
                        gv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        player1.setText(game.getPlayer1());

                        Log.d("chess","gameStarted with id:"+game.getGameId());

                        Timer timer = new Timer(60,timerTextView);
                        timer.start();

                        if (game.getPlayer2() != null) {
                            player2.setText(game.getPlayer2());
                        } else
                            player2.setText("Searching for Player");


                        //TO DO - set eaten pieces

                        //TO DO - log to server an get game json object
                    }
                }
                catch (JSONException ex)
                {
                    ex.printStackTrace();
                }
            }
            //TO DO - set players name
        }
    }//end of inner class ReadFromDB
}


