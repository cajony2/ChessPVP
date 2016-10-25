package com.example.roma.servertest;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

class ReadFromDB extends AsyncTask< Void, Void, String> {

    //Activity activity;
    String action;
    Game game;
    String userName;
    JSONObject gameJson;
    int attempts;
    Communicator comm;
    String returnMessage;
    private String psw;

    public static final String MAKE_MOVE = "makeMove";
    //public static final String MOVE_MADE = "moveMade";
    public static final String GAME_READY = "isGameReady";
    public static final String GAME_NOT_READY = "gameNotReady";
    public static final String GAME_IS_READY = "gameIsReady";
    public static final String ENDGAME = "endGame";
    public static final String GETINFO = "getInfo";

    public ReadFromDB(Communicator _comm, String _action, String _userName, Game _game,String _psw){
        action = _action;
        userName=_userName;
        game=_game;
        attempts=0;
        comm = _comm;
        psw=_psw;
    }

    protected String doInBackground(Void... params) {
        Log.i("chess","HTTP:action"+action);
        String message="";
        String response="";
        try{
            if(action.equals(GAME_READY))
                Thread.sleep(1500);
            URL url = new URL("http://5.29.207.103:8080/Chess/ChessServlet");

            URLConnection connection = url.openConnection();

            connection.setRequestProperty("Action",action);
            connection.setRequestProperty("UserName", userName);
            connection.setRequestProperty("gameId",game.getGameId()+"");
            connection.setRequestProperty("Password",psw);
            if(action.equals(ENDGAME)){
                if(comm.getWin())
                    connection.setRequestProperty("winner",userName);
                else
                    connection.setRequestProperty("winner",game.getBlackPlayerName());
            }

            Log.i("chess", "connecting to db:"+action);


            connection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            if(action.equals(MAKE_MOVE) || action.equals(ENDGAME)) {
                if (game != null) {
                    JSONObject json = game.toJson();
                    Log.i("chess", "json:" + json.toString());
                    out.write(json.toString());
                } else
                    Log.i("chess", "game is null");

            }
            out.close();

            // get response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String nextLine;

            while ((nextLine = in.readLine()) != null) {
                message += nextLine;
            }
            returnMessage = message;
            response = connection.getHeaderField("Response");
            Log.i("chess","******** test"+response);
            if(response.equals(GAME_IS_READY)){
                gameJson = new JSONObject(message);
            }
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

        return response;
    }

    @Override
    public void onPostExecute(String message){
        // after move was sent to database check if opponent made his move
        switch (action) {
            case ENDGAME:
                comm.gameEnded();
                break;
            case GETINFO:
                comm.setInfo(returnMessage);
                break;
            default:
                comm.moveMade(message, returnMessage);
                break;
        }




    }


}
