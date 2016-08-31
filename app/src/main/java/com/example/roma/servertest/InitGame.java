package com.example.roma.servertest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Roma on 8/21/2016.
 * this is the middle layer between the login activity and the gameboard activity
 * if action is createnewgame this activity will sent a request to the server to create a new game
 * after that it will send every 1 second a gameready request , if another player joine's the new game the server will
 * responde with gameready and the game json string and the activity will start a new gameboard activity.
 * if the action is join game , is will send every 1 second is getEmptyGame request to the server if there's such game
 * the server will send json string of the game  and a new gameboard activity will be lunched
 */
public class InitGame extends Activity{

    private ProgressBar spinner;
    private TextView status;
    private String userName;
    private Game game;

    @Override
    /*
        on create init UI  and send the first request to the server depending on the action specified
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_game_activity);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        status  = (TextView)findViewById(R.id.status);
        status.setTextColor(Color.RED);
        spinner.setVisibility(View.GONE);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        String action = intent.getStringExtra("ACTION");

        if(action.equals("createNewGame")) {
            status.setText("Creating Game...");
            game = null;
            ReadFromDB createGame = new ReadFromDB(this, "createNewGame");
            createGame.execute();
        }

        spinner.setVisibility(View.VISIBLE);

    }
    /*
    async task readfromDB send a request in a separate thread depending on the action
     join game
     create new game
     gameReady
     getEmptyGame

     */
    class ReadFromDB extends AsyncTask< Void, Void, String> {
        Activity activity;
        String action;

        //constructor
        public ReadFromDB(Activity _e, String _action){
            activity=_e;
            action = _action;
            Log.d("chess","new read from db created action: "+action);
        }

        @Override
        // send  request to server
        protected String doInBackground(Void... params) {
            String message="";

            try{
                Thread.sleep(1000);
                URL url = new URL("http://5.29.207.103:8080/Chess/ChessServlet");
                // URL url = new URL("http://10.0.2.2:8080/Chess/ChessServlet");

                URLConnection connection = url.openConnection();
                connection.setRequestProperty("Action",action);
                if(action.equals("gameReady"))
                    connection.setRequestProperty("gameId",game.getGameId()+"");
                Log.d("chess", "connecting to db:"+action);

                connection.setRequestProperty("UserName", userName);
                //connection.setRequestProperty("Password", psw);
                connection.setDoOutput(true);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

                if (action.equals("gameReady")){
                    out.write(game.toJson().toString());
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
        // Update UI and  continue according to the response
        public void onPostExecute(String message){
            Log.d("chess","on post execute return msg: "+message);

            if(action.equals("createNewGame")){      // if game created
                try {
                    game = new Game(new JSONObject(message));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status.setText("Searching for player");
                new ReadFromDB(activity,"gameReady").execute();                       // chech to see if player 2 is ready
            }
            else {                                                 //action is "gameready
                Log.d("chess","mesg: "+message);
                if (message.equals("null")){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    new ReadFromDB(activity,"gameReady").execute();
                }
                else{
                    status.setText("game Ready");
                    Intent intent = new Intent(activity, GameBoard.class);

                    intent.putExtra("game",message );
                    intent.putExtra("ACTION","fullGame" );
                    //intent.putExtra(ACTION,"createNewGame");
                    startActivity(intent);
                }
            }
        }

    }

}
