package com.example.roma.servertest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class Login extends Activity implements View.OnClickListener {

    public final static String USERNAME = "com.example.roma.servletTest.USERNAME";
    public final static String ACTION = "com.example.roma.servletTest.FIRST";

    EditText userName = null;
    EditText pswField = null;

    Button doubleMe;
    Button createUser;
    Button joinGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        userName = (EditText) findViewById(R.id.firstName);
        pswField = (EditText) findViewById(R.id.psw);

        doubleMe  = (Button) findViewById(R.id.createGameBtn);
        createUser = (Button) findViewById(R.id.logIn);
        joinGame = (Button) findViewById(R.id.joinGame);

        doubleMe.setOnClickListener(this);
        createUser.setOnClickListener(this);

        joinGame.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.logIn:
                Log.i("chess","button clicked, logIn");
                logIn();
                break;
            case R.id.createGameBtn:
                createGame();
                break;
            case R.id.joinGame:
                joinGame();
                break;
        }
    }


    private void joinGame(){
        Intent intent = new Intent(this, GameBoard.class);

        intent.putExtra(USERNAME,userName.getText().toString() );
        intent.putExtra("action","joinGame");
        startActivity(intent);
    }


    private void logIn(){
        String name = userName.getText().toString();
        String psw = pswField.getText().toString();
        ReadFromDB readFromDB = new ReadFromDB(this,name,psw);

        readFromDB.execute();

        //Intent intent = new Intent(this, PersonalInfo.class);

        //startActivity(intent);
    }


    private void createGame(){


        Intent intent = new Intent(this, InitGame.class);

        intent.putExtra(USERNAME,userName.getText().toString() );
        intent.putExtra(ACTION,"createNewGame");
        startActivity(intent);

    }
    class ReadFromDB extends AsyncTask< Void, Void, String> {

        Activity activity;
        ProgressDialog progressDialog;
        String name;
        String psw;

        //constructor
        public ReadFromDB(Activity _e,String _name, String _psw){
            activity=_e;
            name=_name;
            psw=_psw;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(activity, "", "Loading...");
        }


        @Override
        protected String doInBackground(Void... params) {
            String message="";
            try{
                URL url = new URL("http://5.29.207.103:8080/Chess/ChessServlet");
                // URL url = new URL("http://10.0.2.2:8080/Chess/ChessServlet");

                URLConnection connection = url.openConnection();
                connection.setRequestProperty("Action","logIn");
                Log.d("chess", "connecting to db: LogIn");

                connection.setRequestProperty("UserName", name);
                connection.setRequestProperty("Password", psw);
                connection.setDoOutput(true);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

                out.write("");

                out.close();
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
            Log.i("chess",message);
            progressDialog.dismiss();
            //check returend msg from server
            if(message.equals("error")){                // is message is error then account no such account
                Toast.makeText(activity, "No such Account",Toast.LENGTH_LONG).show();
            }else{                                      // else msg is json with use info show personal info activity
                Intent intent = new Intent(activity, PersonalInfo.class);

                intent.putExtra("JSON",message);
                startActivity(intent);
            }

        }
    }//end of inner class ReadFromDB

}
