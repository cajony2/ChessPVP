package com.example.roma.servertest;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;



/**
 * Created by Roma on 9/14/2016.
 *
 */
public class EndGamePopup extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.end_game_popup);

        Log.i("chess","width");

    }
}
