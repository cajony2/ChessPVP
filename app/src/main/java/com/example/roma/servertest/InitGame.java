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
import android.widget.Toast;

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
    private String psw;
    private Game game;
    public static final String CREATE_GAME = "createNewGame";
    public static final String JOIN_GAME = "joinGame";
    public static final String JOIN_READY ="JoinedGame";
    public static final String JOIN_FAILED = "ERROR";
    public static final String GAME_READY = "gameReady";
    public static final String KILL_GAME ="killGame";

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
        psw= intent.getStringExtra("password");
        String action = intent.getStringExtra("ACTION");
        switch (action){
            case CREATE_GAME:
                status.setText("Creating Game...");
                game = null;
                ReadFromDB createGame = new ReadFromDB(this,CREATE_GAME );
                createGame.execute();
                break;
            case JOIN_GAME:
                status.setText("Searching Game");
                ReadFromDB joinGame = new ReadFromDB(this,JOIN_GAME );
                joinGame.execute();
                break;
        }
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(game!=null) {
            ReadFromDB read = new ReadFromDB(this, KILL_GAME);
            read.execute();
        }
    }


    /* async task readfromDB send a request in a separate thread depending on the action
	 join game
	 create new game
	 gameReady */
    class ReadFromDB extends AsyncTask< Void, Void, String> {
        private static final int MAX_ATTEMPTS = 30;
        Activity activity;
        String action;
        String response;
        int attempts;


        //constructor
        public ReadFromDB(Activity _e, String _action){
            activity=_e;
            action = _action;
            attempts =0;
            Log.d("chess","new read from db created action: "+action);
        }
        public ReadFromDB(Activity _e, String _action, int _attempts){
            activity=_e;
            action = _action;
            attempts = _attempts;
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
                if(action.equals("gameReady") || (game!=null && action.equals(KILL_GAME)))
                    connection.setRequestProperty("gameId",game.getGameId()+"");
                Log.d("chess", "connecting to db:"+action);

                connection.setRequestProperty("UserName", userName);
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

                if(action.equals(JOIN_GAME))
                    response = connection.getHeaderField("Response");

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
            if(attempts >=MAX_ATTEMPTS) {
                Toast toast = Toast.makeText(activity, "No players", Toast.LENGTH_LONG);
                toast.show();
                activity.finish();
            }else if(action.equals(CREATE_GAME)){      // if game created get game json from response
                try {
                    game = new Game(new JSONObject(message));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status.setText("Searching for player attempts: "+ attempts);
                new ReadFromDB(activity,GAME_READY).execute();                       // chech to see if player 2 is ready
            }
            else if (action.equals(GAME_READY)){                                                 //action is "gameready
                if (message.equals("null")){
                        new ReadFromDB(activity, GAME_READY, ++attempts).execute();
                        status.setText("Searching for players attempts: " + attempts);
                }
                else{
                    status.setText("game Ready");
                    startGame(message);
                }
            }
            else if (action.equals(JOIN_GAME)){
                if(response.equals(JOIN_FAILED)){
                    new ReadFromDB(activity,JOIN_GAME).execute();
                    status.setText("Searching for players attempts: " + attempts);
                }
                else if (response.equals(JOIN_READY)){
                    Log.i("chess","joined game successfully");
                    startGame(message);
                }

            }
        }

        private void startGame(String gameJson ){
            Intent intent = new Intent(activity, GameBoard.class);
            intent.putExtra("game",gameJson );
            intent.putExtra("userName", userName);
            intent.putExtra("password",psw);
            startActivity(intent);
        }

    }

}
