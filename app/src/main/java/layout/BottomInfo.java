package layout;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


import com.example.roma.servertest.Communicator;
import com.example.roma.servertest.EatenAdapter;
import com.example.roma.servertest.Piece;
import com.example.roma.servertest.R;
import com.example.roma.servertest.Timer;

import java.util.ArrayList;

public class BottomInfo extends Fragment implements View.OnClickListener {

    TextView userNameTV;
    TextView timerTV;
    Communicator comm;
    EatenAdapter eatenAdapter;
    GridView eatenGridView;
    Button makeMove;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_info,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userNameTV = (TextView) view.findViewById(R.id.player1);
        timerTV =(TextView) view.findViewById(R.id.timer);
        eatenGridView = (GridView) view.findViewById(R.id.eatenPiecesBottom);
        makeMove = (Button) view.findViewById(R.id.submit);




    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
        String userName = comm.getUserName();

        userNameTV.setText(userName);

        eatenAdapter = new EatenAdapter(getActivity(),comm.getEatenPieces(Color.WHITE));
        eatenGridView.setAdapter(eatenAdapter);

        makeMove.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(comm!=null)
            comm.makeMove();

    }

    public void refresh(ArrayList<Piece> eatenPieces){
        eatenAdapter.setEatenPieces(eatenPieces);
        eatenAdapter.notifyDataSetChanged();
    }
}

