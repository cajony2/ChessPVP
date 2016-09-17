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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer_bar,container,false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        simpleProgressBar = (ProgressBar) view.findViewById(R.id.timerBar);
        progressBarText = (TextView) view.findViewById(R.id.timerText);
        progress=0;



    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
        if(comm.canClick())
            progressBarMessage="Make a Move: ";
        else
            progressBarMessage="Waiting for Opponent: ";
        handler  = new Handler();
        setProgressValue();

    }

    private void setProgressValue() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("chess","in timer somthing");
                simpleProgressBar.setProgress(progress);
                if(progress%10==0)
                    progressBarText.setText(progressBarMessage+timeLeft());
                progress++;
                if(progress<simpleProgressBar.getMax())
                    handler.postDelayed(this, 100);
                else {
                    progressBarText.setText("Time's Up");
                    comm.timerFinished();
                }
            }
        };
        handler.postDelayed(runnable,100);

    }

    private String timeLeft(){
        String minutes ="",seconds="";
        int timeleft =60-progress/10;

            if(timeleft>=60) {
                minutes=""+ timeleft /60;
            }
            else {
                minutes="0";
            }
            if(timeleft<10)
                seconds="0";
            seconds+=(timeleft%60);

        return minutes+":"+seconds;
    }


}
