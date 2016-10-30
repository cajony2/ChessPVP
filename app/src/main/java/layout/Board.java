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
    private final int OK = 1;
    private final int CHECK = 2;
    private final int CHECK_MATE = 3;
    private final int DRAW = 4;//TODO roma, plz deal with this situation (draw msg for the user)

    GridView gridView;
    Communicator comm;
    Adapter adapter;
    //Piece[] piecesOld;         //old version   -->for testing for now
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
    int destinationPositionOfMovingPiece = -1;

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
        //piecesOld= comm.getPiecesOld();
        pieces = comm.getPieces();
        myColor = comm.getColor();
        adapter  = new Adapter(getActivity(),pieces);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();              //
        if (myColor == Color.WHITE )//getTurn should return int
             flipBoard();
        else
            resetIsFlipped();

    }

    private void resetIsFlipped(){

        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                pieces[row][col].setIsFlipped(false);
            }
        }

        /*for (Piece p : piecesOld)
        {
            p.setIsFlipped(false);
        }*/
    }

    private void flipBoard(){
        chessBoardView.setRotationX(180);
        rotatePieces();
    }

    private void rotatePieces(){

        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                pieces[row][col].setIsFlipped(true);
            }
        }

        /*for (Piece p : piecesOld)
        {
            p.setIsFlipped(true);
        }*/
    }

    public void refresh(Piece[][] pieces){
        updateStatus();

        this.pieces = pieces;
        adapter.setPieces(pieces);
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

        Point tileCoordinate = positionToPoint(position);

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
                    //if (piecesOld[position].getIntColor() == myColor)      // selected his own color
                    if (pieces[tileCoordinate.x][tileCoordinate.y].getIntColor() == myColor)
                    {//then this user is white. needs to be changed to pieces[position].getColor == Player.getColor
                        isSelected = true;
                        moves = /*piecesOld[position]*/pieces[tileCoordinate.x][tileCoordinate.y].getLegalMoves(pieces);
                        if (moves != null && moves.size() != 0)
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
                            destinationPositionOfMovingPiece = position;
                            //copy of the board in case the player hits undo the whole pieces will get the copy value
                            //piecesOldCopy = new Piece[64];
                            piecesCopy = new Piece[8][8];

                            for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
                            {
                                for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
                                {
                                    switch (pieces[row][col].getName()){
                                        case "rook":
                                            piecesCopy[row][col] = new Rook(pieces[row][col]);
                                            break;
                                        case "queen":
                                            piecesCopy[row][col] = new Queen(pieces[row][col]);
                                            break;
                                        case "pawn":
                                            piecesCopy[row][col] = new Pawn(pieces[row][col]);
                                            break;
                                        case "knight":
                                            piecesCopy[row][col] = new Knight(pieces[row][col]);
                                            break;
                                        case "king":
                                            piecesCopy[row][col] = new King(pieces[row][col]);
                                            break;
                                        case "empty":
                                            piecesCopy[row][col] = new Empty(pieces[row][col]);
                                            break;
                                        case "bishop":
                                            piecesCopy[row][col] = new Bishop(pieces[row][col]);
                                            break;
                                    }
                                }
                            }
                            Point pieceCoordinate =  positionToPoint(selectedTile);

                            if (!pieces[tileCoordinate.x][tileCoordinate.y].getName().equals("empty"))//if player eats opponent piece
                            {
                                eatenPiece = pieces[tileCoordinate.x][tileCoordinate.y];
                                comm.setEatenPiece(eatenPiece);
                            }

                            swapPieces(pieces, selectedTile, position);
                            pieces[tileCoordinate.x][tileCoordinate.y].setHasNotMovedYet(false);


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

    private void swapPieces(Piece[][] pieces, int selectedTile, int position) {

        Point selectedTileCoordinate = positionToPoint(selectedTile);
        Point positionTileCoordinate = positionToPoint(position);


        //castling
        if (pieces[selectedTileCoordinate.x][selectedTileCoordinate.y].getName().equals("king"))
        {
            if (selectedTile == 4 && position == 6)
            {
                swapPieces(pieces, 7, 5);
            }
            else if (selectedTile == 4 && position == 2)
            {
                swapPieces(pieces, 0, 3);
            }
            else if (selectedTile == 60 && position == 62)
            {
                swapPieces(pieces, 63, 61);
            }
            else if (selectedTile == 60 && position == 58)
            {
                swapPieces(pieces, 56, 59);
            }
        }

        //pawn turns into a queen when reaches the end
        if (pieces[selectedTileCoordinate.x][selectedTileCoordinate.y].getName().equals("pawn"))
        {
            if (position >= 56 || position <= 7)//pawn reached last row
            {
                pieces[selectedTileCoordinate.x][selectedTileCoordinate.y] = new Queen("queen", pieces[selectedTileCoordinate.x][selectedTileCoordinate.y].getColor(), pieces[selectedTileCoordinate.x][selectedTileCoordinate.y].getPosition(), false);
            }
        }

        //Point tempPoint = new Point(pieces[positionTileCoordinate.x][positionTileCoordinate.y].getPointPosition().x, piecesOld[position].getPointPosition().y);
        Point tempPoint = new Point(positionTileCoordinate.x, positionTileCoordinate.y);

        switch (pieces[selectedTileCoordinate.x][selectedTileCoordinate.y].getName()){
            case "rook":
                pieces[positionTileCoordinate.x][positionTileCoordinate.y] = new Rook(pieces[selectedTileCoordinate.x][selectedTileCoordinate.y]);
                break;
            case "queen":
                pieces[positionTileCoordinate.x][positionTileCoordinate.y] = new Queen(pieces[selectedTileCoordinate.x][selectedTileCoordinate.y]);
                break;
            case "pawn":
                pieces[positionTileCoordinate.x][positionTileCoordinate.y] = new Pawn(pieces[selectedTileCoordinate.x][selectedTileCoordinate.y]);
                break;
            case "knight":
                pieces[positionTileCoordinate.x][positionTileCoordinate.y] = new Knight(pieces[selectedTileCoordinate.x][selectedTileCoordinate.y]);
                break;
            case "king":
                pieces[positionTileCoordinate.x][positionTileCoordinate.y] = new King(pieces[selectedTileCoordinate.x][selectedTileCoordinate.y]);
                break;
            case "empty":
                pieces[positionTileCoordinate.x][positionTileCoordinate.y] = new Empty(pieces[selectedTileCoordinate.x][selectedTileCoordinate.y]);
                break;
            case "bishop":
                pieces[positionTileCoordinate.x][positionTileCoordinate.y] = new Bishop(pieces[selectedTileCoordinate.x][selectedTileCoordinate.y]);
                break;
        }
        pieces[positionTileCoordinate.x][positionTileCoordinate.y].setPointPosition(tempPoint);
        pieces[positionTileCoordinate.x][positionTileCoordinate.y].setPosition(position);
        int x = selectedTileCoordinate.x;
        int y = selectedTileCoordinate.y;
        pieces[selectedTileCoordinate.x][selectedTileCoordinate.y] = new Empty("empty", "white", selectedTile, false);
        pieces[selectedTileCoordinate.x][selectedTileCoordinate.y].setPointPosition(x, y);
        pieces[selectedTileCoordinate.x][selectedTileCoordinate.y].setEmpty(true);

        //fillDoubleArrayFromSingle(piecesOld, pieces); //TODO erase this piece`o`shit!!!!
    }

    public int isChess() {
        Piece myKing = null;
        boolean toBreak = false;
        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            if (toBreak)
                break;
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                if (pieces[row][col].getIntColor() == myColor && pieces[row][col] instanceof King)
                {
                    myKing = pieces[row][col];
                    toBreak = true;
                    break;
                }
            }
        }

        int opponentColor = (myColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

        if (myKing.isThreatened(pieces, opponentColor).size() != 0)//the king is threatened
        {
            //if (!myKing.canMove(pieces))//king can not move
            if (allPiecesCanNotMove(pieces, myKing.getIntColor()))//no piece can move
                return CHECK_MATE;
            else
                return CHECK;
        }
        else//the king is not threatened
        {
            if (allPiecesCanNotMove(pieces, myKing.getIntColor()))//no piece can move
            {
                return DRAW;
            }
        }
        return OK;
    }

    private boolean allPiecesCanNotMove(Piece[][] pieces, int color)
    {
        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col <TILES_NUMBER_IN_A_ROW; col++)
            {
                if (pieces[row][col].getIntColor() == color && pieces[row][col].canMove(pieces))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean getMoveMade() {
        return moveMade;
    }

    public void setMoveMade(boolean moveMade) {
        this.moveMade = moveMade;
    }

    public int getPositionOfLastMovingPiece()
    {
        return destinationPositionOfMovingPiece;
    }

    public Piece[][] undoClicked() {

        if (moveMade)
        {
            adapter.setSelectedTile(-1);
            moveMade = false;
            isSelected = false;
            for (int i = 0; i < possibleMove.length; i++)
                possibleMove[i] = false;
            //piecesOld = piecesOldCopy;// TODO erase!!!
            if (piecesCopy != null)
                pieces = piecesCopy;
            if (eatenPiece != null)
            {
                comm.erasePieceFromEatenPieces(eatenPiece);
            }
            comm.setPieces(pieces);
            adapter.setPieces(pieces);
            adapter.notifyDataSetChanged();
        }
        return pieces;
    }

    public Piece[][] submitCliced(){
        return pieces;
    }

    private Point positionToPoint(int pos)
    {
        Point resPoint = null;
        int counter = 0;
        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                if (counter == pos)
                {
                    resPoint = new Point(row, col);
                }
                counter++;
            }
        }
        return resPoint;
    }
}
