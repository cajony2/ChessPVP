package com.example.roma.servertest;

/**
 * Created by Roma & Jony on 8/10/2016.
 */
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Game {

    //jony added
    private Tile[][] _tiles;
    private final int TILES_NUMBER_IN_A_ROW = 8;
    private Context _context;
    private Piece[][] _gridPieces;

    //variables
    private Piece[] _pieces;
    private int status;
    private String turn;
    private int gameId;
    private ArrayList<Piece> eatenPieces;
    private int eatenPiecesSize;
    private Player _whitePlayer;
    private Player _blackPlayer;


    //constructor added by jony, it calls a different method (newCreateBoard) that fills Tile[][]
    public Game (Context context, String player1){
        Log.i("chess", "creating game");
        _context = context;
        _whitePlayer = new Player.WhitePlayer(player1);
        _blackPlayer = new Player.BlackPlayer(player1);
        status = 0 ;      // 0=  create new game;
        turn = _whitePlayer.getName();
        newCreateBoard();//jony added
    }

    //constructor NOT IN USE!!!
    public Game (String player1){
		_whitePlayer = new Player.WhitePlayer(player1);
		_blackPlayer = new Player.BlackPlayer(player1);
        status = 0 ;      // 0=  create new game;
        turn = _whitePlayer.getName();
        createBoard();
    }

    //added by jony - this method fills the Tile[][]
    private void newCreateBoard() {
        _tiles = new Tile[TILES_NUMBER_IN_A_ROW][TILES_NUMBER_IN_A_ROW];
        for (int i = 0; i < TILES_NUMBER_IN_A_ROW; i++){
            int colorDivider = 0;
            for (int j = 0; j < TILES_NUMBER_IN_A_ROW; j++){
                if ((i % 2) == 0){
                    if ((colorDivider % 2) == 0){
                        _tiles[i][j] = new Tile.WhiteTile(_context, new Point(i, j));
                    }
                    else{
                        _tiles[i][j] = new Tile.BlackTile(_context, new Point(i, j));
                    }
                }
                else{
                    if ((colorDivider % 2) == 0){
                        _tiles[i][j] = new Tile.BlackTile(_context, new Point(i, j));
                    }
                    else{
                        _tiles[i][j] = new Tile.WhiteTile(_context, new Point(i, j));
                    }
                }
                colorDivider++;
            }
        }
        setPieces();
    }

    //added by jony - setting the pieces on board (beginning of a game)
    private void setPieces() {
        //filling first row from the top with black chess pieces
        _tiles[0][0].setPiece(new Rook(Color.BLACK, _tiles[0][0]));
        _tiles[0][1].setPiece(new Knight(Color.BLACK, _tiles[0][0]));
        _tiles[0][2].setPiece(new Bishop(Color.BLACK, _tiles[0][0]));
        _tiles[0][3].setPiece(new Queen(Color.BLACK, _tiles[0][0]));
        _tiles[0][4].setPiece(new King(Color.BLACK, _tiles[0][0]));
        _tiles[0][5].setPiece(new Bishop(Color.BLACK, _tiles[0][0]));
        _tiles[0][6].setPiece(new Knight(Color.BLACK, _tiles[0][0]));
        _tiles[0][7].setPiece(new Rook(Color.BLACK, _tiles[0][0]));

        //filling second row from the top with black pawns
        for (int i = 0; i < TILES_NUMBER_IN_A_ROW; i++)
        {
            _tiles[1][i].setPiece(new Pawn(Color.BLACK, _tiles[1][i]));
        }

        //filling second row from the bottom with white pawns
        for (int i = 0; i < TILES_NUMBER_IN_A_ROW; i++)
        {
            _tiles[6][i].setPiece(new Pawn(Color.WHITE, _tiles[6][i]));
        }

        //filling first row from the bottom with white chess pieces
        _tiles[7][0].setPiece(new Rook(Color.WHITE, _tiles[7][0]));
        _tiles[7][1].setPiece(new Knight(Color.WHITE, _tiles[7][1]));
        _tiles[7][2].setPiece(new Bishop(Color.WHITE, _tiles[7][2]));
        _tiles[7][3].setPiece(new Queen(Color.WHITE, _tiles[7][3]));
        _tiles[7][4].setPiece(new King(Color.WHITE, _tiles[7][4]));
        _tiles[7][5].setPiece(new Bishop(Color.WHITE, _tiles[7][5]));
        _tiles[7][6].setPiece(new Knight(Color.WHITE, _tiles[7][6]));
        _tiles[7][7].setPiece(new Rook(Color.WHITE, _tiles[7][7]));
    }

    //receiving json from server and create new game object
    public Game (JSONObject gameJson){
        Log.d("ingameConstructor","creating new game from, json");
        try {
			_whitePlayer = new Player.WhitePlayer(gameJson.getString("player1"));
            Log.i("name:", _whitePlayer.getName());
			_blackPlayer = new Player.BlackPlayer(gameJson.getString("player2"));
            status = gameJson.getInt("status");
            turn = gameJson.getString("turn");
			_pieces = new Piece[64];
            _gridPieces = new Piece[8][8];//jony added
            gameId = gameJson.getInt("gameid");
            eatenPiecesSize=gameJson.getInt("eatenpiecessize");
            setEatenPieces(gameJson.getJSONArray("eatenpieces"));       //create eatenpieces array list
            Log.i("chess","game object created player1=" + _whitePlayer.getName() + " player2: " + _blackPlayer.getName());
            JSONArray piecesJson = gameJson.getJSONArray("pieces");
            getPiecesFromJson(piecesJson);

            //jony added
            fillDoubleArrayFromSingle(_pieces, _gridPieces);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void createBoard(){
		_pieces =  new Piece[64];

        //insert the starting pieces
        // init the one dim array  fill in the black and white tiles
        for(int i=0 ; i<_pieces.length ; i++){
            if(i%16 < 8){
                if(i%2 != 0)
					_pieces[i]=new Empty("empty" ,"black",i);
                else
					_pieces[i]=new Empty("empty" ,"white",i);
            }else
            if(i%2 != 0)
				_pieces[i]=new Empty("empty" ,"white",i);
            else
				_pieces[i]=new Empty("empty" ,"black",i);
        }

        // add the pawns
        for(int i =8 ; i<16 ;i++){
			_pieces[i] = new Pawn("pawn", "white", i);
			_pieces[i+40]= new Pawn("pawn", "black", i+40);
        }

        // add white pieces
		_pieces[56]=(new Rook("rook" , "white", 56));
		_pieces[57]=(new Knight("Knight" , "white", 57));
		_pieces[58]=(new Bishop("Bishop" , "white", 58));
		_pieces[59]=(new Queen("queen" , "white", 59));
		_pieces[60]=(new King("king" , "white", 60));
		_pieces[61]=(new Bishop("Bishop" , "white", 61));
		_pieces[62]=(new Knight("Knight" , "white", 62));
		_pieces[63]=(new Rook("rook" , "white", 63));

        // add black pieces
		_pieces[0]=(new Rook("rook", "black", 0));
		_pieces[1]=(new Knight("Knight", "black", 1));
		_pieces[2]=(new Bishop("Bishop", "black", 2));
		_pieces[3]=(new Queen("queen", "black", 3));
		_pieces[4]=(new King("king", "black", 4));
		_pieces[5]=(new Bishop("Bishop", "black", 5));
		_pieces[6]=(new Knight("Knight", "black", 6));
		_pieces[7]=(new Rook("rook", "black", 7));

        //added by jony, not tested yet. suppose to fill the double array from the single array
        fillDoubleArrayFromSingle(_pieces, _gridPieces);
    }

    public String getPlayer1(){
        return _whitePlayer.getName();
    }
    public String getPlayer2(){
        return _blackPlayer.getName();
    }
    public int getStatus(){
        return status;
    }
    public String getTurn(){
        return turn;
    }

    public Piece getPiece(int pos){
        if(pos<64)
            return _pieces[pos];
        else {
            Log.d("getPiece", "no piece at " + pos);
            return null;
        }
    }

    public Piece[] getBoard2(){
        return _pieces;
    }

    public void getPiecesFromJson(JSONArray array) throws JSONException {
        int size = array.length();
        Log.d("numberOfPieces"," "+size);
        JSONObject temp ;
        for(int i=0 ; i<size ;i++) {
            temp = array.getJSONObject(i);
            String piece = temp.getString("name");
            String color = temp.getString("color");
            int position = temp.getInt("position");

            switch (piece){
                case "empty":
					_pieces[i] = new Empty(piece, color, position);
                    break;
                case "bishop":
					_pieces[i] = new Bishop(piece, color, position);
                    break;
                case "king":
					_pieces[i] = new King(piece, color, position);
                    break;
                case "queen":
					_pieces[i] = new Queen(piece, color, position);
                    break;
                case "knight":
					_pieces[i] = new Knight(piece, color, position);
                    break;
                case "pawn":
					_pieces[i] = new Pawn(piece, color, position);
                    break;
                case "rook":
					_pieces[i] = new Rook(piece, color, position);
                    break;
                default:
                    Log.d("error","in default: "+piece);
            }
        }
    }

    public JSONObject toJson()  {

        JSONObject json = new JSONObject();
        try {
            json.put("player1", _whitePlayer.getName());
            json.put("player2", _blackPlayer.getName());
            json.put("status", status);
            json.put("turn", turn);
            json.put("gameid",gameId);
            json.put("pieces", getPiecesJson());
        } catch (JSONException e) {
            Log.d("chess","fucking error creatinf json player name=" + _whitePlayer.getName());
            e.printStackTrace();
        }
        return json;
    }

    public JSONArray getPiecesJson() throws JSONException{
        JSONArray pieces = new JSONArray();

        for(int i=0; i < _pieces.length; i++)
            pieces.put(_pieces[i].toJson());

        return pieces;
    }

    public int getGameId(){
        return gameId;
    }

    public ArrayList<Piece> getEatenPieces() {
        return eatenPieces;
    }
    /*
    Added by Roma
    create the eaten pieces arraylist from a jason array
     */
    private void setEatenPieces(JSONArray eatenPiecesJson) {
        eatenPieces = new ArrayList<Piece>();
        JSONObject temp;
        //Log.i("chess","eaten pieces "+eatenPiecesJson.toString());
        for(int i =0 ; i<eatenPiecesSize ; i++){
            try {
                temp = eatenPiecesJson.getJSONObject(i);
                String piece = temp.getString("name");
                String color = temp.getString("color");
                int position = temp.getInt("position");

                switch (piece){
                    case "empty":
                        eatenPieces.add(new Empty(piece, color, position));
                        break;
                    case "bishop":
                        eatenPieces.add(new Bishop(piece, color, position));
                        break;
                    case "king":
                        eatenPieces.add(new King(piece, color, position));
                        break;
                    case "queen":
                        eatenPieces.add(new Queen(piece, color, position));
                        break;
                    case "knight":
                        eatenPieces.add(new Knight(piece, color, position));
                        break;
                    case "pawn":
                        eatenPieces.add(new Pawn(piece, color, position));
                        break;
                    case "rook":
                        eatenPieces.add(new Rook(piece, color, position));
                        break;
                    default:
                        Log.i("chess","in default: "+piece);
                }
                //Log.i("chess","created new eatenPiece");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
