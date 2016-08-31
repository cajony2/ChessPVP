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

    //variables
    private Piece[] tiles;
    private String whitePlayer;
    private String blackPlayer;
    private int status;
    private String turn;
    private int gameId;
    private ArrayList<Piece> eatenPieces;
    private int eatenPiecesSize;

    //constructor added by jony, it calls a different method (newCreateBoard) that fills Tile[][]
    public Game (Context context, String _player1){
        Log.i("chess", "creating game");
        _context = context;
        whitePlayer=_player1;
        blackPlayer="";
        status = 0 ;      // 0=  create new game;
        turn = whitePlayer;
        newCreateBoard();//jony added
    }

    //constructor
    public Game (String _player1){
        whitePlayer=_player1;
        blackPlayer="";
        status = 0 ;      // 0=  create new game;
        turn = whitePlayer;
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
            whitePlayer = gameJson.getString("player1");
            Log.d("name:",whitePlayer);
            blackPlayer=gameJson.getString("player2");
            status = gameJson.getInt("status");
            turn = gameJson.getString("turn");
            tiles = new Piece[64];
            gameId = gameJson.getInt("gameid");
            eatenPiecesSize=gameJson.getInt("eatenpiecessize");
            setEatenPieces(gameJson.getJSONArray("eatenpieces"));       //create eatenpieces array list
            Log.d("chess","game object created player1="+whitePlayer+" player2: "+blackPlayer);
            JSONArray piecesJson = gameJson.getJSONArray("pieces");
            getPiecesFromJson(piecesJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createBoard(){
        tiles =  new Piece[64];

        //insert the starting pieces
        // init the one dim array  fill in the black and white tiles
        for(int i=0 ; i<tiles.length ; i++){
            if(i%16 < 8){
                if(i%2 != 0)
                    tiles[i]=new Empty("empty" ,"black",i);
                else
                    tiles[i]=new Empty("empty" ,"white",i);
            }else
            if(i%2 != 0)
                tiles[i]=new Empty("empty" ,"white",i);
            else
                tiles[i]=new Empty("empty" ,"black",i);
        }

        // add the pawns
        for(int i =8 ; i<16 ;i++){
            tiles[i] = new Pawn("pawn", "white", i);
            tiles[i+40]= new Pawn("pawn", "black", i+40);
        }

        // add white pieces
        tiles[56]=(new Rook("rook" , "white", 56));
        tiles[57]=(new Knight("Knight" , "white", 57));
        tiles[58]=(new Bishop("Bishop" , "white", 58));
        tiles[59]=(new Queen("queen" , "white", 59));
        tiles[60]=(new King("king" , "white", 60));
        tiles[61]=(new Bishop("Bishop" , "white", 61));
        tiles[62]=(new Knight("Knight" , "white", 62));
        tiles[63]=(new Rook("rook" , "white", 63));

        // add black pieces
        tiles[0]=(new Rook("rook", "black", 0));
        tiles[1]=(new Knight("Knight", "black", 1));
        tiles[2]=(new Bishop("Bishop", "black", 2));
        tiles[3]=(new Queen("queen", "black", 3));
        tiles[4]=(new King("king", "black", 4));
        tiles[5]=(new Bishop("Bishop", "black", 5));
        tiles[6]=(new Knight("Knight", "black", 6));
        tiles[7]=(new Rook("rook", "black", 7));
    }

    public String getPlayer1(){
        return whitePlayer;
    }
    public String getPlayer2(){
        return blackPlayer;
    }
    public int getStatus(){
        return status;
    }
    public String getTurn(){
        return turn;
    }

    public Piece getPiece(int pos){
        if(pos<64)
            return tiles[pos];
        else {
            Log.d("getPiece", "no piece at " + pos);
            return null;
        }
    }

    public Piece[] getBoard2(){
        return tiles;
    }

    private void getPiecesFromJson(JSONArray array) throws JSONException {
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
                    tiles[i] = new Empty(piece, color, position);
                    break;
                case "bishop":
                    tiles[i] = new Bishop(piece, color, position);
                    break;
                case "king":
                    tiles[i] = new King(piece, color, position);
                    break;
                case "queen":
                    tiles[i] = new Queen(piece, color, position);
                    break;
                case "knight":
                    tiles[i] = new Knight(piece, color, position);
                    break;
                case "pawn":
                    tiles[i] = new Pawn(piece, color, position);
                    break;
                case "rook":
                    tiles[i] = new Rook(piece, color, position);
                    break;
                default:
                    Log.d("error","in default: "+piece);
            }
        }
    }

    public JSONObject toJson()  {

        JSONObject json = new JSONObject();
        try {
            json.put("player1", whitePlayer);
            json.put("player2", blackPlayer);
            json.put("status", status);
            json.put("turn", turn);
            json.put("gameid",gameId);
            json.put("pieces", getPiecesJson());
        } catch (JSONException e) {
            Log.d("chess","fucking error creatinf json player name="+whitePlayer);
            e.printStackTrace();
        }
        return json;
    }

    public JSONArray getPiecesJson() throws JSONException{
        JSONArray pieces = new JSONArray();

        for(int i=0; i<tiles.length ;i++)
            pieces.put(tiles[i].toJson());

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
                        Log.d("error","in default: "+piece);
                }
                Log.i("chess","created new eatenPiece");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
