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
    Piece[] piecesOldCopy = null;
    Piece[][] piecesCopy = null;
    Piece eatenPiece = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        canClick= comm.canClick();
        piecesOld= comm.getPiecesOld();
        pieces = comm.getPieces();
        myColor = comm.getColor();
        adapter  = new Adapter(getActivity(),piecesOld);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();              //
        if (myColor == Color.WHITE )//getTurn should return int
             flipBoard();
        else
            resetIsFlipped();

    }

    private void resetIsFlipped(){
        for (Piece p : piecesOld)
        {
            p.setIsFlipped(false);
        }
    }

    private void flipBoard(){
        chessBoardView.setRotationX(180);
        rotatePieces();
    }

    private void rotatePieces(){

        for (Piece p : piecesOld)
        {
            p.setIsFlipped(true);
        }
    }

    public void refresh(Piece[] _pieces){
        updateStatus();

        piecesOld=_pieces;
        adapter.setPieces(_pieces);
        adapter.notifyDataSetChanged();
        if (myColor == Color.WHITE )//getTurn should return int
            flipBoard();
        else
            resetIsFlipped();
        Log.i("chess","after refresh");
    }

    private void updateStatus() {
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
        canClick = comm.canClick();
        //if (view == )//user hit submit button
        if(canClick)
        {
            if (!moveMade)
            {
                if (!isSelected)
                {
                    Log.i("chess", "tile " + position + " Selected");

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
                }
                else
                {
                    if (selectedTile >= 0)
                    {   //making a move
                        if (possibleMove[position])// this move is legal
                        {
                            //copy of the board in case the player hits undo the whole pieces will get the copy value
                            piecesOldCopy = new Piece[64];
                            piecesCopy = new Piece[8][8];
                            for (int i = 0; i < 64; i++)
                            {
                                switch (piecesOld[i].getName()){
                                    case "rook":
                                        piecesOldCopy[i] = new Rook(piecesOld[i]);
                                        break;
                                    case "queen":
                                        piecesOldCopy[i] = new Queen(piecesOld[i]);
                                        break;
                                    case "pawn":
                                        piecesOldCopy[i] = new Pawn(piecesOld[i]);
                                        break;
                                    case "knight":
                                        piecesOldCopy[i] = new Knight(piecesOld[i]);
                                        break;
                                    case "king":
                                        piecesOldCopy[i] = new King(piecesOld[i]);
                                        break;
                                    case "empty":
                                        piecesOldCopy[i] = new Empty(piecesOld[i]);
                                        break;
                                    case "bishop":
                                        piecesOldCopy[i] = new Bishop(piecesOld[i]);
                                        break;
                                }
                            }
                            fillDoubleArrayFromSingle(piecesOldCopy, piecesCopy);

                            if (!piecesOld[position].getName().equals("empty"))//if player eats opponent piece
                            {
                                eatenPiece = piecesOld[position];
                                comm.setEatenPiece(eatenPiece);
                                //whatWasEaten += piecesOld[position].getName();
                            }

                            swapPieces(piecesOld, selectedTile, position);

                            moveMade = true;
                            //TODO
                            //if (moveMade)
                            //if it`s a pawn then it turns into a queen when reaches the last row
                            //set king/rook hasNotMovedYet to false

                            Log.i("chess", "move made");
                            adapter.setSelectedTile(-1);
                        }
                    }
                    isSelected = false;
                    for (int i = 0; i < possibleMove.length; i++)
                        possibleMove[i] = false;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void swapPieces(Piece[] piecesOld, int selectedTile, int position) {

        //castling
        if (piecesOld[selectedTile].getName().equals("king"))
        {
            if (selectedTile == 4 && position == 6)
            {
                swapPieces(piecesOld, 7, 5);
            }
            else if (selectedTile == 4 && position == 2)
            {
                swapPieces(piecesOld, 0, 3);
            }
            else if (selectedTile == 60 && position == 62)
            {
                swapPieces(piecesOld, 63, 61);
            }
            else if (selectedTile == 60 && position == 58)
            {
                swapPieces(piecesOld, 56, 59);
            }
        }

        //pawn turns into a queen when reaches the end
        if (piecesOld[selectedTile].getName().equals("pawn"))
        {
            if (position >= 56 || position <= 7)//pawn reached last row
            {
                piecesOld[selectedTile] = new Queen("queen", piecesOld[selectedTile].getColor(), piecesOld[selectedTile].getPosition());
            }
        }

        Point tempPoint = new Point(piecesOld[position].getPointPosition().x, piecesOld[position].getPointPosition().y);
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
        piecesOld[position].setPosition(position);
        int x = piecesOld[selectedTile].getPointPosition().x;
        int y = piecesOld[selectedTile].getPointPosition().y;
        piecesOld[selectedTile] = new Empty("empty", "white", selectedTile);
        piecesOld[selectedTile].setPointPosition(x, y);
        piecesOld[selectedTile].setEmpty(true);

        fillDoubleArrayFromSingle(piecesOld, pieces);
    }

    private void swapPieces(Piece[] piecesOld, Piece selectedTile, Piece position) {

        //castling
        if (selectedTile.getName().equals("king"))
        {
            if (selectedTile.getPosition() == 4 && position.getPosition() == 6)
            {
                swapPieces(piecesOld, 7, 5);
            }
            else if (selectedTile.getPosition() == 4 && position.getPosition() == 2)
            {
                swapPieces(piecesOld, 0, 3);
            }
            else if (selectedTile.getPosition() == 60 && position.getPosition() == 62)
            {
                swapPieces(piecesOld, 63, 61);
            }
            else if (selectedTile.getPosition() == 60 && position.getPosition() == 58)
            {
                swapPieces(piecesOld, 56, 59);
            }
        }

        //pawn turns into a queen when reaches the end
        if (selectedTile.getName().equals("pawn"))
        {
            if (position.getPosition() >= 56 || position.getPosition() <= 7)//pawn reached last row
            {
                selectedTile = new Queen("queen", selectedTile.getColor(), selectedTile.getPosition());
            }
        }

        Point tempPoint = new Point(position.getPointPosition().x, position.getPointPosition().y);
        switch (selectedTile.getName()){
            case "rook":
                position = new Rook(selectedTile);
                break;
            case "queen":
                position = new Queen(selectedTile);
                break;
            case "pawn":
                position = new Pawn(selectedTile);
                break;
            case "knight":
                position = new Knight(selectedTile);
                break;
            case "king":
                position = new King(selectedTile);
                break;
            case "empty":
                position = new Empty(selectedTile);
                break;
            case "bishop":
                position = new Bishop(selectedTile);
                break;
        }
        position.setPointPosition(tempPoint);
        position.setPosition(position.getPosition());
        int x = selectedTile.getPointPosition().x;
        int y = selectedTile.getPointPosition().y;
        selectedTile = new Empty("empty", "white", selectedTile.getPosition());
        selectedTile.setPointPosition(x, y);
        selectedTile.setEmpty(true);

        fillDoubleArrayFromSingle(piecesOld, pieces);
    }

    //not tested yet
    public boolean isChess()
    {
        Piece myKing = null;
        for (Piece p : piecesOld)
        {
             if (p.getIntColor() == myColor && p instanceof King)
             {
                 myKing = p;
                 break;
             }
        }
        int opponentColor = (myColor == Color.WHITE) ? Color.BLACK : Color.WHITE;
        if (myKing.isThreatened(piecesOld, opponentColor).size() != 0)
            return true;
        else
            return false;
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

    public boolean getMoveMade() {
        return moveMade;
    }

    public void setMoveMade(boolean moveMade) {
        this.moveMade = moveMade;
    }

    public void undoClicked() {

        if (moveMade)
        {
            //swapPieces(piecesOld, movingPiece, eatenPiece);
            //revertSwap();
            adapter.setSelectedTile(-1);
            moveMade = false;
            isSelected = false;
            for (int i = 0; i < possibleMove.length; i++)
                possibleMove[i] = false;
            piecesOld = piecesOldCopy;
            pieces = piecesCopy;
            if (eatenPiece != null)
            {
                comm.erasePieceFromEatenPieces(eatenPiece);
            }
            adapter.setPieces(piecesOld);
            adapter.notifyDataSetChanged();
        }
    }
}
