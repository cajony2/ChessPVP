package layout;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.roma.servertest.Communicator;
import com.example.roma.servertest.EatenAdapter;
import com.example.roma.servertest.Piece;
import com.example.roma.servertest.R;

import java.util.ArrayList;


public class TopInfo extends Fragment {

    TextView opponentNameTV;
    Communicator comm;
    EatenAdapter eatenAdapter;
    GridView eatenGridView;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_info,container,false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        opponentNameTV = (TextView) view.findViewById(R.id.player2);
        eatenGridView = (GridView) view.findViewById(R.id.eatenpiecesup);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
        String opponentName = comm.getOpponentName();

        opponentNameTV.setText(opponentName);

        eatenAdapter = new EatenAdapter(getActivity(),comm.getEatenPieces(Color.BLACK));
        eatenGridView.setAdapter(eatenAdapter);
    }

    public void refresh(ArrayList<Piece> eatenPieces){
        eatenAdapter.setEatenPieces(eatenPieces);
        eatenAdapter.notifyDataSetChanged();
    }


}
