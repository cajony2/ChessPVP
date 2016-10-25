package com.example.roma.servertest;

/**
 * Created by Roma & Jony on 8/10/2016.
 */

import android.util.Log;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Game {

    //variables
    private final int TILES_NUMBER_IN_A_ROW = 8;
    private Piece[][] _pieces;
    private int status;
    private String turn;
    private int gameId;
    private ArrayList<Piece> eatenPieces;
    private int eatenPiecesSize;
    private Player _whitePlayer;
    private Player _blackPlayer;

    //Constructor receiving json from server and create new game object
    public Game (JSONObject gameJson){
        Log.d("ingameConstructor","creating new game from, json");
        try {
			_whitePlayer = new Player.WhitePlayer(gameJson.getString("player1"));
            Log.i("name:", _whitePlayer.getName());
			_blackPlayer = new Player.BlackPlayer(gameJson.getString("player2"));
            status = gameJson.getInt("status");
            turn = gameJson.getString("turn");
            _pieces = new Piece[8][8];
            gameId = gameJson.getInt("gameid");
            eatenPiecesSize=gameJson.getInt("eatenpiecessize");
            setEatenPieces(gameJson.getJSONArray("eatenpieces"));
            Log.i("chess","game object created player1=" + _whitePlayer.getName() + " player2: " + _blackPlayer.getName());
            JSONArray piecesJson = gameJson.getJSONArray("pieces");
            getPiecesFromJson(piecesJson);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

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

    public void setPieces (Piece[][] pieces){
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
            boolean moved = temp.getBoolean("hasnotmovedyet");

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
                                _pieces[row][col] = new Rook(piece, color, position,moved);
                                break;
                            case "queen":
                                _pieces[row][col] = new Queen(piece, color, position,moved);
                                break;
                            case "pawn":
                                _pieces[row][col] = new Pawn(piece, color, position,moved);
                                break;
                            case "knight":
                                _pieces[row][col] = new Knight(piece, color, position,moved);
                                break;
                            case "king":
                                _pieces[row][col] = new King(piece, color, position,moved);
                                break;
                            case "empty":
                                _pieces[row][col] = new Empty(piece, color, position,moved);
                                break;
                            case "bishop":
                                _pieces[row][col] = new Bishop(piece, color, position,moved);
                                break;
                        }
                        toBreak = true;
                    }
                    counter++;
                    if (toBreak)
                        break;
                }
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
        Log.i("chess","eaten pieces "+eatenPiecesJson.toString());
        for(int i =0 ; i<eatenPiecesSize ; i++){
            try {
                temp = eatenPiecesJson.getJSONObject(i);
                String piece = temp.getString("name");
                String color = temp.getString("color");
                int position = temp.getInt("position");
                boolean moved  =temp.getBoolean("hasnotmovedyet");

                switch (piece){
                    case "empty":
                        eatenPieces.add(new Empty(piece, color, position,moved));
                        break;
                    case "bishop":
                        eatenPieces.add(new Bishop(piece, color, position,moved));
                        break;
                    case "king":
                        eatenPieces.add(new King(piece, color, position,moved));
                        break;
                    case "queen":
                        eatenPieces.add(new Queen(piece, color, position,moved));
                        break;
                    case "knight":
                        eatenPieces.add(new Knight(piece, color, position,moved));
                        break;
                    case "pawn":
                        eatenPieces.add(new Pawn(piece, color, position,moved));
                        break;
                    case "rook":
                        eatenPieces.add(new Rook(piece, color, position,moved));
                        break;
                    default:
                        Log.i("chess","in default: "+piece);
                }
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
        eatenPieces.remove(p);
    }

    public void setStatus(int check) {
        status = check;
    }

}
