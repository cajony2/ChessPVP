package com.example.roma.servertest;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Roma on 8/18/2016.
 */
public class Timer   {


    private Handler handler;
    private int time;
    private int timeRemaining;
    private boolean canceled;
    TextView textView;
    private boolean done;

    public Timer(int _time , TextView _textView){
        handler  = new Handler();
        time=_time;
        timeRemaining=0;
        canceled=false;
        done=false;
        textView=_textView;
        Log.d("chess","timer created");
    }

    public Timer(){
        handler  = new Handler();
    }


    public void start() {
        timeRemaining=time;
        canceled=false;
        done=false;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String minutes ="",seconds="";

                        if(timeRemaining>=0  &&  !canceled) {
                            if(timeRemaining>=60) {
                                minutes=""+ timeRemaining /60;
                    }
                    else {
                        minutes="0";
                    }
                    if(timeRemaining<10)
                        seconds="0";
                    seconds+=(timeRemaining%60);
                    textView.setText(minutes+":"+seconds);

                   // Log.d("chess","timer:"+minutes+":"+seconds);
                    timeRemaining--;
                    handler.postDelayed(this, 1000);
                }
                else
                    done=true;



            }
        };

        handler.postDelayed(runnable,1000);
    }

    public int getTimePassed(){
        return time-timeRemaining;
    }

    public void cancel(){
        canceled = true;
    }





}
