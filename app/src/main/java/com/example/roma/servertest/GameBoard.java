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
    Button submit;
    boolean moveMade;



    int[] images={R.drawable.bdt60,R.drawable.blt60,R.drawable.kdt60,
                  R.drawable.klt60,R.drawable.ndt60,R.drawable.nlt60,
                  R.drawable.pdt60,R.drawable.plt60,R.drawable.qdt60,
                  R.drawable.qlt60,R.drawable.rdt60,R.drawable.rlt60,
                  R.drawable.black,R.drawable.white};



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_activity);

        player1 = (TextView) findViewById(R.id.player1);
        player2 = (TextView) findViewById(R.id.player2);

        Intent intent = getIntent();
        pieces = null;
        game=null;

        userName = intent.getStringExtra(Login.USERNAME);
        String action  = intent.getStringExtra(Login.ACTION);

        Log.d("chess","gameBoard created , action: "+action);


        gv=(GridView)   findViewById(R.id.chessboard);
        submit = (Button) findViewById(R.id.submit);

        moveMade = false;
        isSelected =false;
        possibleMove = new boolean[64];

        for(int i=0 ; i<possibleMove.length ; i++){
            possibleMove[i]=false;
        }

        switch (action){
            case "createNewGame":
                new ReadFromDB(this,"createNewGame").execute();
                break;
            case "joinGame":
                new ReadFromDB(this,"joinGame").execute();
                break;
        }

        gv.setOnItemClickListener(this);
        submit.setOnClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("ITEMCLICKED","item number "+position);

            Game game = adapter.getGame();
            Piece[] pieces = game.getBoard2();
            int selectedTile=adapter.getSelectedTile();

            ArrayList<Integer> moves = null;
            // do only if the square clicked is the users color
            if (!isSelected) {
                Log.d("debug","username: "+userName+" player1: "+game.getPlayer1());
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
                        Log.d("chess","move made");
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

    class ReadFromDB extends AsyncTask< Void, Void, String>{
        Activity e;
        String action;

        public ReadFromDB(Activity _e, String _action){
            e=_e;
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

                Log.d("chess","the return message "+message);
                in.close();



            } catch (Exception e1) {
                e1.printStackTrace();
            }

            return message;
        }
        @Override
        public void onPostExecute(String message){

            if(!action.equals("makeMove")) {
                try {

                    game = new Game(new JSONObject(message));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (game != null) {
                    adapter = new Adapter(e, game, null);
                    gv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    player1.setText(game.getPlayer1());
                    if (game.getPlayer2() != null) {
                        player2.setText(game.getPlayer2());
                    } else
                        player2.setText("Searching for Player");

                    //set eaten pieces


                    //log to server an get game json object

                }

            }
            //set players name

        }
    }

}


