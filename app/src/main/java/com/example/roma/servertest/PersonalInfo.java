package com.example.roma.servertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Roma on 8/26/2016.
 * This activity saves and shows on screen the player`s details
 */
public class PersonalInfo extends Activity implements View.OnClickListener {

    TextView nametv;
    TextView scoretv;
    TextView winstv;
    Button createNewGameBtn;
    Button joinGameBtn;
    String userName ;
    String psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_activity);

        nametv = (TextView) findViewById(R.id.name);
        scoretv = (TextView) findViewById(R.id.score);
        winstv = (TextView) findViewById(R.id.wins);

        createNewGameBtn = (Button) findViewById(R.id.createGameBtn);
        joinGameBtn = (Button) findViewById(R.id.joinGameBtn);
        createNewGameBtn.setOnClickListener(this);
        joinGameBtn.setOnClickListener(this);

        Intent intent = getIntent();
        psw = intent.getStringExtra("password");
        try {
            JSONObject json = new JSONObject(intent.getStringExtra("JSON"));            // get info from login activity and create a json
            userName =json.getString("playerName");
            nametv.setText(json.getString("playerName"));                                  // get user name
            scoretv.setText(""+json.getInt("score"));
            winstv.setText(""+json.getInt("wins"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, InitGame.class);
        intent.putExtra("userName",userName );

        switch (v.getId()){
            case R.id.createGameBtn:
                Log.i("chess","button clicked, create new Game");
                intent.putExtra("ACTION","createNewGame");
                intent.putExtra("userName",userName);
                intent.putExtra("password",psw);
                break;
            case R.id.joinGameBtn:
                Log.i("chess","button clicked, create new Game");
                intent.putExtra("ACTION","joinGame");
                break;
        }
        startActivity(intent);
    }
}
