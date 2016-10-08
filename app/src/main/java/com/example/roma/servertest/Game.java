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

    //variables
    private final int TILES_NUMBER_IN_A_ROW = 8;
    private Piece[][] _pieces;
    //private Piece[] pieces;
    private int status;
    private String turn;
    private int gameId;
    private ArrayList<Piece> eatenPieces;
    private int eatenPiecesSize;
    private Player _whitePlayer;
    private Player _blackPlayer;


    //constructor NOT IN USE!!!
    /*public Game (String player1){
		_whitePlayer = new Player.WhitePlayer(player1);
		_blackPlayer = new Player.BlackPlayer(player1);
        status = 0 ;      // 0=  create new game;
        turn = _whitePlayer.getName();
        createBoard();
    }*/

    //Constructor receiving json from server and create new game object
    public Game (JSONObject gameJson){
        Log.d("ingameConstructor","creating new game from, json");
        try {
			_whitePlayer = new Player.WhitePlayer(gameJson.getString("player1"));
            Log.i("name:", _whitePlayer.getName());
			_blackPlayer = new Player.BlackPlayer(gameJson.getString("player2"));
            status = gameJson.getInt("status");
            turn = gameJson.getString("turn");
			//pieces = new Piece[64];
            _pieces = new Piece[8][8];//jony added
            gameId = gameJson.getInt("gameid");
            eatenPiecesSize=gameJson.getInt("eatenpiecessize");
            setEatenPieces(gameJson.getJSONArray("eatenpieces"));       //create eatenpieces array list
            Log.i("chess","game object created player1=" + _whitePlayer.getName() + " player2: " + _blackPlayer.getName());
            JSONArray piecesJson = gameJson.getJSONArray("pieces");
            getPiecesFromJson(piecesJson);

            //jony added
            //fillpiecesFromSingle(pieces, _pieces);

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

    //NOT IN USE!!!
    /*private void createBoard(){
		pieces =  new Piece[64];

        //insert the starting pieces
        // init the one dim array  fill in the black and white tiles
        for(int i=0 ; i<pieces.length ; i++){
            if(i%16 < 8){
                if(i%2 != 0)
					pieces[i]=new Empty("empty" ,"black",i);
                else
					pieces[i]=new Empty("empty" ,"white",i);
            }else
            if(i%2 != 0)
				pieces[i]=new Empty("empty" ,"white",i);
            else
				pieces[i]=new Empty("empty" ,"black",i);
        }

        // add the pawns
        for(int i =8 ; i<16 ;i++){
			pieces[i] = new Pawn("pawn", "white", i);
			pieces[i+40]= new Pawn("pawn", "black", i+40);
        }

        // add white pieces
		pieces[56]=(new Rook("rook" , "white", 56));
		pieces[57]=(new Knight("Knight" , "white", 57));
		pieces[58]=(new Bishop("Bishop" , "white", 58));
		pieces[59]=(new Queen("queen" , "white", 59));
		pieces[60]=(new King("king" , "white", 60));
		pieces[61]=(new Bishop("Bishop" , "white", 61));
		pieces[62]=(new Knight("Knight" , "white", 62));
		pieces[63]=(new Rook("rook" , "white", 63));

        // add black pieces
		pieces[0]=(new Rook("rook", "black", 0));
		pieces[1]=(new Knight("Knight", "black", 1));
		pieces[2]=(new Bishop("Bishop", "black", 2));
		pieces[3]=(new Queen("queen", "black", 3));
		pieces[4]=(new King("king", "black", 4));
		pieces[5]=(new Bishop("Bishop", "black", 5));
		pieces[6]=(new Knight("Knight", "black", 6));
		pieces[7]=(new Rook("rook", "black", 7));

        //added by jony, not tested yet. suppose to fill the double array from the single array
        fillDoubleArrayFromSingle(pieces, _pieces);
    }*/

    public String getWhitePlayerName(){
        return _whitePlayer.getName();
    }

    public String getBlackPlayerName(){
        return _blackPlayer.getName();
    }

    public int getStatus(){
        return status;
    }

    public String getTurn(){
        return turn;
    }

    public Piece getPiece(int pos){

        if (pos >= 0 && pos < 64)
        {
            int counter = 0;
            for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
            {
                for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
                {
                    if (pos == counter)
                    {
                        return _pieces[row][col];
                    }
                    counter++;
                }
            }
        }
        return null;
        /*if(pos<64)
            return pieces[pos];
        else {
            Log.d("getPiece", "no piece at " + pos);
            return null;
        }*/
    }

	public Piece[][] getGridPieces()
	{
		return _pieces;
	}

    /*public Piece[] getBoard2(){
        return pieces;
    }*/

    public void setPieces (Piece[][] pieces)
    {
        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                switch (pieces[row][col].getName()){
                    case "rook":
                        _pieces[row][col] = new Rook(pieces[row][col]);
                        break;
                    case "queen":
                        _pieces[row][col] = new Queen(pieces[row][col]);
                        break;
                    case "pawn":
                        _pieces[row][col] = new Pawn(pieces[row][col]);
                        break;
                    case "knight":
                        _pieces[row][col] = new Knight(pieces[row][col]);
                        break;
                    case "king":
                        _pieces[row][col] = new King(pieces[row][col]);
                        break;
                    case "empty":
                        _pieces[row][col] = new Empty(pieces[row][col]);
                        break;
                    case "bishop":
                        _pieces[row][col] = new Bishop(pieces[row][col]);
                        break;
                }
            }
        }
    }


    /*public void setPieces (Piece[] pieces)
    {
        for (int i = 0; i < 64; i++)
        {
            switch (pieces[i].getName()){
                case "rook":
                    pieces[i] = new Rook(pieces[i]);
                    break;
                case "queen":
                    pieces[i] = new Queen(pieces[i]);
                    break;
                case "pawn":
                    pieces[i] = new Pawn(pieces[i]);
                    break;
                case "knight":
                    pieces[i] = new Knight(pieces[i]);
                    break;
                case "king":
                    pieces[i] = new King(pieces[i]);
                    break;
                case "empty":
                    pieces[i] = new Empty(pieces[i]);
                    break;
                case "bishop":
                    pieces[i] = new Bishop(pieces[i]);
                    break;
            }
        }
        fillDoubleArrayFromSingle(pieces, _pieces);
    }*/

    public void getPiecesFromJson(JSONArray array) throws JSONException {
        int size = array.length();
        Log.d("numberOfPieces"," "+size);
        JSONObject temp ;
        for(int i=0 ; i<size ;i++)
        {
            temp = array.getJSONObject(i);
            String piece = temp.getString("name");
            String color = temp.getString("color");
            int position = temp.getInt("position");

            int counter = 0;
            boolean toBreak = false;
            for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
            {
                if (toBreak)
                    break;
                for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
                {
                    if (counter == position)
                    {
                        switch (piece) {
                            case "rook":
                                _pieces[row][col] = new Rook(piece, color, position);
                                break;
                            case "queen":
                                _pieces[row][col] = new Queen(piece, color, position);
                                break;
                            case "pawn":
                                _pieces[row][col] = new Pawn(piece, color, position);
                                break;
                            case "knight":
                                _pieces[row][col] = new Knight(piece, color, position);
                                break;
                            case "king":
                                _pieces[row][col] = new King(piece, color, position);
                                break;
                            case "empty":
                                _pieces[row][col] = new Empty(piece, color, position);
                                break;
                            case "bishop":
                                _pieces[row][col] = new Bishop(piece, color, position);
                                break;
                        }
                        toBreak = true;

                    }
                    counter++;
                    if (toBreak)
                        break;
                }
            }


            /*switch (piece){
                case "empty":
					pieces[i] = new Empty(piece, color, position);
                    break;
                case "bishop":
					pieces[i] = new Bishop(piece, color, position);
                    break;
                case "king":
					pieces[i] = new King(piece, color, position);
                    break;
                case "queen":
					pieces[i] = new Queen(piece, color, position);
                    break;
                case "knight":
					pieces[i] = new Knight(piece, color, position);
                    break;
                case "pawn":
					pieces[i] = new Pawn(piece, color, position);
                    break;
                case "rook":
					pieces[i] = new Rook(piece, color, position);
                    break;
                default:
                    Log.d("error","in default: "+piece);
            }*/
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
            json.put("pieces", getPiecesJson());//
            json.put("eatenpieces",getEatenPiecesJson());
            json.put("eatenpiecessize",eatenPieces.size());
        } catch (JSONException e) {
            Log.d("chess","fucking error creatinf json player name=" + _whitePlayer.getName()+" turn:"+turn);
            e.printStackTrace();
        }
        return json;
    }

    public JSONArray getEatenPiecesJson() throws JSONException{
        JSONArray pieces = new JSONArray();

        for(int i=0; i < eatenPieces.size(); i++)
            pieces.put(eatenPieces.get(i).toJson());

        return pieces;
    }

    public JSONArray getPiecesJson() throws JSONException{
        JSONArray pieces = new JSONArray();

        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                pieces.put(_pieces[row][col].toJson());
            }
        }

        /*for(int i=0; i < pieces.length; i++)
            pieces.put(pieces[i].toJson());*/

        return pieces;
    }

    public int getGameId(){
        return gameId;
    }

    public ArrayList<Piece> getEatenPieces() {
        return eatenPieces;
    }

    public void setEatenPieces(ArrayList<Piece> p){
        eatenPieces=p;
    }
    /*
    Added by Roma
    create the eaten pieces arraylist from a jason array
     */
    private void setEatenPieces(JSONArray eatenPiecesJson) {
        eatenPieces = new ArrayList<Piece>();
        JSONObject temp;
        Log.i("chess","eaten pieces "+eatenPiecesJson.toString());
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

    public void addEatenPiece(Piece p) {
        eatenPieces.add(p);
        eatenPiecesSize++;
        Log.i("chess","eaten piece added");
    }

    public void erasePieceFromEatenPieces(Piece p)
    {
        //Piece lastAdded = eatenPieces.get(eatenPiecesSize);
        eatenPieces.remove(p);
        //return lastAdded;
    }

    public void setStatus(int check) {
        status = check;
    }

}
