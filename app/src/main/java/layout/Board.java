package layout;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.roma.servertest.Adapter;
import com.example.roma.servertest.Bishop;
import com.example.roma.servertest.Empty;
import com.example.roma.servertest.King;
import com.example.roma.servertest.Knight;
import com.example.roma.servertest.Pawn;
import com.example.roma.servertest.Piece;
import com.example.roma.servertest.Queen;
import com.example.roma.servertest.R;
import com.example.roma.servertest.Communicator;
import com.example.roma.servertest.Rook;
import java.util.ArrayList;


public class Board extends Fragment implements AdapterView.OnItemClickListener {

    private final int TILES_NUMBER_IN_A_ROW = 8;

    GridView gridView;
    Communicator comm;
    Adapter adapter;
    Piece[] piecesOld;         //old version   -->for testing for now
    Piece[][] pieces;          //new version   --> jony's algorythm
    boolean canClick;
    boolean isSelected;
    int myColor;
    boolean[] possibleMove;
    boolean moveMade;
    View chessBoardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canClick=true;
        isSelected=false;
        moveMade=false;
        possibleMove = new boolean[64];
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = (GridView) view.findViewById(R.id.chessboard);
        chessBoardView = view.findViewById(R.id.linearlayountchessboard);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
        piecesOld= comm.getPiecesOld();
        pieces = comm.getPieces();
        myColor = comm.getColor();
        adapter  = new Adapter(getActivity(),piecesOld);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();              //
        if (myColor == Color.WHITE )//getTurn should return int
             flipBoard();


    }
    private void flipBoard()
    {
        chessBoardView.setRotationX(180);
        //chessBoardView.setRotationY(180);
        rotatePieces();
    }
    private void rotatePieces() {

        for (Piece p : piecesOld)
        {
            p.setIsFlipped(true);
        }
    }

    public void refresh(Piece[] _pieces){
        Log.i("chess","in refresh");
        piecesOld=_pieces;
        adapter.setPieces(_pieces);
        adapter.notifyDataSetChanged();
        Log.i("chess","after refresh");
    }

    //@Override
    // listener  for the gridview when the user clicks a tile check if the tile contains a piece and
    // the piece is of the legal color and create a legalmoves array and update adapter and UI
    //if the click is after we selected a piece   check if the move is legal and if so make the move
    // update adapter and UI and disable the touch
    //THIS METHOD CREATED ONLY FOR TESTING NEED ALOT OF CHANGING!!!!!!!!!!
    //TODO finish this method :)

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int selectedTile=adapter.getSelectedTile();

        ArrayList<Integer> moves ;
        // do only if the square clicked is the users color
        if(canClick) {
            if (!isSelected) {
                Log.i("chess", "tile "+position+" Selected");

                //if (!(game.getPlayer1().equals(userName) == pieces[position].getColor().equals("white")))
                if (piecesOld[position].getIntColor() == myColor)      // selected his own color
                {//then this user is white. needs to be changed to pieces[position].getColor == Player.getColor
                    isSelected = true;
                    moves = piecesOld[position].getLegalMoves(piecesOld);
                    if (moves != null && moves.size() != 0)//jony added moves.size() != 0
                    {
                        for (int pos : moves) {
                            possibleMove[pos] = true;
                        }
                        adapter.setSelectedTile(position);
                        adapter.setPossibleMoves(possibleMove);
                    }
                }
            } else {                        //selected maybe a move
                if (selectedTile >= 0) {   //making a move
                    if (possibleMove[position]) {    // this move is legal
                        //should add if piece canMove (so he would not expose the king)

                        if(!piecesOld[position].getName().equals("empty"))      //if player eats opponent piece
                                comm.setEatenPiece(piecesOld[position]);

                        Point tempPoint = new Point(piecesOld[position].getPointPosition().x, piecesOld[position].getPointPosition().y);
                        int tempPosition = piecesOld[position].getPosition();

                        switch (piecesOld[selectedTile].getName()){
                            case "rook":
                                piecesOld[position] = new Rook(piecesOld[selectedTile]);
                                break;
                            case "queen":
                                piecesOld[position] = new Queen(piecesOld[selectedTile]);
                                break;
                            case "pawn":
                                piecesOld[position] = new Pawn(piecesOld[selectedTile]);
                                break;
                            case "knight":
                                piecesOld[position] = new Knight(piecesOld[selectedTile]);
                                break;
                            case "king":
                                piecesOld[position] = new King(piecesOld[selectedTile]);
                                break;
                            case "empty":
                                piecesOld[position] = new Empty(piecesOld[selectedTile]);
                                break;
                            case "bishop":
                                piecesOld[position] = new Bishop(piecesOld[selectedTile]);
                                break;
                        }
                        piecesOld[position].setPointPosition(tempPoint);
                        piecesOld[position].setPosition(tempPosition);

                        int x = piecesOld[selectedTile].getPointPosition().x;
                        int y = piecesOld[selectedTile].getPointPosition().y;
                        piecesOld[selectedTile] = new Empty("empty", "white", selectedTile);
                        piecesOld[selectedTile].setPointPosition(x, y);
                        piecesOld[selectedTile].setEmpty(true);

                        fillDoubleArrayFromSingle(piecesOld, pieces);
                        /*piecesOld[position] = piecesOld[selectedTile];
                        piecesOld[position].setPosition(position);
                        Point p = piecesOld[selectedTile].getPointPosition();
                        piecesOld[position].setPointPosition(p);

                        piecesOld[selectedTile] = new Empty("empty", "white", selectedTile);
                        piecesOld[selectedTile].setEmpty(true);
                        piecesOld[selectedTile].setPosition(position);*/
                        /*
                        pieces[selectedTile].setPointPosition(tempPointPosition);
                        pieces[selectedTile].setPosition(tempPosition);
                            */
                        moveMade = true;

                        Log.i("chess", "move made");
                        adapter.setSelectedTile(-1);
                    }
                }
                isSelected = false;
                for (int i = 0; i < possibleMove.length; i++)
                    possibleMove[i] = false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    //filling the double array of Pieces from a single array
    private void fillDoubleArrayFromSingle(Piece[] singleArray, Piece[][] doubleArray)
    {
        int counter = 0;
        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                doubleArray[row][col] = singleArray[counter];
                doubleArray[row][col].setPointPosition(row, col);
                counter++;
            }
        }
    }

}
