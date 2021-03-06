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
import java.util.ArrayList;

public class BottomInfo extends Fragment implements View.OnClickListener {

    TextView userNameTV;
    Communicator comm;
    EatenAdapter eatenAdapter;
    GridView eatenGridView;
    Button makeMove;
    Button undoMove;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_info,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userNameTV = (TextView) view.findViewById(R.id.player1);
        eatenGridView = (GridView) view.findViewById(R.id.eatenPiecesBottom);
        makeMove = (Button) view.findViewById(R.id.submit);
        undoMove = (Button) view.findViewById(R.id.undo);



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
        undoMove.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(comm!=null)
        {
            if (v.equals(makeMove))
            {
                comm.makeMove();
            }
            else if (v.equals(undoMove))     //undo clicked
            {
                comm.undo();
            }


        }

    }

    public void refresh(ArrayList<Piece> eatenPieces){
        eatenAdapter.setEatenPieces(eatenPieces);
        eatenAdapter.notifyDataSetChanged();
    }
}

