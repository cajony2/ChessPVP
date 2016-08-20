package com.example.roma.servertest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

//Main activity
public class Login extends Activity implements View.OnClickListener {

    public final static String USERNAME = "com.example.roma.servletTest.USERNAME";
    public final static String ACTION = "com.example.roma.servletTest.FIRST";

    EditText inputValue = null;
    EditText pswField = null;

    Button doubleMe;
    Button createUser;
    Button startGameBtn ;
    Button joinGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        inputValue = (EditText) findViewById(R.id.firstName);
        pswField = (EditText) findViewById(R.id.psw);

        doubleMe  = (Button) findViewById(R.id.createGameBtn);
        createUser = (Button) findViewById(R.id.createUser);
        startGameBtn = (Button) findViewById(R.id.startGameButton);
        joinGame = (Button) findViewById(R.id.joinGame);

        doubleMe.setOnClickListener(this);
        createUser.setOnClickListener(this);
        startGameBtn.setOnClickListener(this);
        joinGame.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.createUser:
                createUser();
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

        intent.putExtra(USERNAME,inputValue.getText().toString() );
        intent.putExtra(ACTION,"joinGame");
        startActivity(intent);
    }


    private void createUser(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL("http://5.29.207.103:8080/Chess/ChessServlet");
                    URLConnection connection  = url.openConnection();

                    String message = "create new User message from android";
                    String userName = inputValue.getText().toString();
                    String password =  pswField.getText().toString();

                    connection.setRequestProperty("Action", "createUser");
                    connection.setRequestProperty("UserName",userName);
                    connection.setRequestProperty("Password",password );

                    Log.d("chess", "create user");

                    connection.setDoOutput(true);
                    OutputStreamWriter out  = new OutputStreamWriter(connection.getOutputStream());
                    out.write(message);
                    out.close();

                    BufferedReader in   = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String returnLine ="";
                    returnLine = in.readLine();


                    String fullMessage = returnLine;

                    while( (returnLine = in.readLine())!=null){
                        fullMessage+=(returnLine+"\n");
                    }
                    Log.d("chess","->>"+fullMessage);

                    in.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void createGame(){


        Intent intent = new Intent(this, GameBoard.class);

        intent.putExtra(USERNAME,inputValue.getText().toString() );
        intent.putExtra(ACTION,"createNewGame");
        startActivity(intent);

    }

}
