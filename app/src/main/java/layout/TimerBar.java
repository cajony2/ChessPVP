package layout;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.roma.servertest.Communicator;
import com.example.roma.servertest.R;

public class TimerBar extends Fragment {
    int progress;
    ProgressBar simpleProgressBar;
    TextView progressBarText;
    private Handler handler;
    Communicator comm;
    private String progressBarMessage;
    boolean stopTimer;
    Runnable runnable;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer_bar,container,false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        simpleProgressBar = (ProgressBar) view.findViewById(R.id.timerBar);
        progressBarText = (TextView) view.findViewById(R.id.timerText);
        stopTimer =false;
        handler  = new Handler();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
        startTimer();
    }

    private void startTimer() {
        //set text
        if(comm.canClick())
            progressBarMessage="Make a Move: ";
        else
            progressBarMessage="Waiting for Opponent: ";
        //reset progress
        progress=0;
        //start timer thread
        if( runnable!= null){
            handler.removeCallbacks(runnable);
            stopTimer=false;
        }
         runnable = new Runnable() {
            @Override
            public void run() {
                // if no interrupt
                Log.i("chess","progress:"+progress);
                if(!stopTimer) {
                    simpleProgressBar.setProgress(progress);
                    progressBarText.setText(progressBarMessage + timeLeft());
                    progress++;
                    if (progress < simpleProgressBar.getMax())
                        handler.postDelayed(this, 1000);
                    else {
                        progressBarText.setText("Time's Up");
                        comm.timerFinished();
                    }
                }
            }
        };
        handler.postDelayed(runnable,100);
    }

    private String timeLeft(){
        String minutes ="",seconds="";
        int timeleft =60-progress;

            if(timeleft>=60) {
                minutes="0"+ timeleft /60;
            }
            else {
                minutes="00";
            }
            if(timeleft<10)
                seconds="0";
            seconds+=(timeleft%60);
        return minutes+":"+seconds;
    }

    public void reset() {
        stopTimer=true;
        Log.i("chess","reseting timer");
        startTimer();
    }
}
