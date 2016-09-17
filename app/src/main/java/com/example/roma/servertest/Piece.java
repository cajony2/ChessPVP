package com.example.roma.servertest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roma on 8/2/2016.
 */

public abstract class Piece{

        protected final static int TILES_NUMBER_IN_A_ROW = 8;

        protected int _color;//added by jony
        protected Point _pointPosition;//added by jony
        protected boolean _isActive;//added by jony
        protected boolean _checksKing;
        protected boolean _isFlipped;
        protected String name;
        protected String color;
        protected int image;
        protected int position;
        protected boolean isEmpty;


    public Piece (String name, String color, int pos){

        this.name = name;
        this.color = color;
        this.position = pos;
        isEmpty = name.equals("empty") ? true : false;
        _pointPosition = new Point();
        _isActive = true;
        if (color.equals("white"))
            _color = Color.WHITE;
        else
            _color = Color.BLACK;
    }

    //empty Ctor
    public Piece()
    {

    }

    //default constructor
    public Piece(int color)
    {
        _color = color;
    }
    
    /*//constructor that copies the piece`s contents
    public Piece (Piece piece)
    {
         _color = piece.getIntColor();
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
        _checksKing = piece.checks();
        _isFlipped = piece._isFlipped;
        name = piece.getName();
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
    }*/


    public boolean getIsFliped()
    {
        return _isFlipped;
    }

    public void  setIsFlipped(boolean bool)
    {
        _isFlipped = bool;
    }

    public boolean getActive()
    {
        return _isActive;
    }

    public void setActive(boolean bool)
    {
        _isActive = bool;
    }

    public boolean checks(Piece[] pieces)
    {
        ArrayList<Piece> possibleMoves = possibleMoves(toDoubleArray(pieces));
        for (Piece p : possibleMoves)
        {
            if (p instanceof King && p.getIntColor() != getIntColor())
                return true;
        }
        return false;
    }

    public void setCheck(boolean bool)
    {
        _checksKing = bool;
    }

    public int getImg(){
        return image;
    }

    public void setImg(int img)
    {
        image = img;
    }

    public void setEmpty(boolean b){
        isEmpty=b;
    }

    public String getName() {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getColor(){
        return color;
    }

    public int getIntColor()
    {
        return _color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public void setIntColor(int color)
    {
        _color = color;
    }

    public Point getPointPosition(){
        return _pointPosition;
    }

    public int getPosition(){
        return position;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    public void setPointPosition(int x, int y)
    {
        _pointPosition.set(x, y);
    }

    public void setPointPosition(Point point)
    {
        _pointPosition.x = point.x;
        _pointPosition.y = point.y;
    }

    public void setPosition(int pos){
        position=pos;
    }

    // each peace should override this method and return  the legal moves from the piece location according to the games piece layout
    public abstract ArrayList<Integer> getLegalMoves(Piece[] pieces);

    public abstract ArrayList<Piece> possibleMoves(Piece[][] pieces);

    //returns the pieces on board with the same color
    protected ArrayList<Piece> getPiecesByColor(Piece[] pieces, int color)
    {
        ArrayList<Piece> resultPieces = new ArrayList<Piece>();
        for (Piece p : pieces)
        {
            if (p.getIntColor() == color && !(p instanceof Empty))// add piece if not empty and with specific color
                resultPieces.add(p);
        }
        return resultPieces;
    }

    //returns all of the opponent pieces
    protected ArrayList<Piece> opponentPieces(Piece[] pieces)
    {
        //getting the opposed color of this piece
        int opposingColor = (getIntColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;

        //returns all the pieces on board that belongs to the opponent
        return getPiecesByColor(pieces, opposingColor);
    }

    /*this method returns a list of Pieces that has to be occupied in order to block a check, even tiles that this piece can not reach
    * if it returns null, it means it can`t block the check = this piece can not move
    * if it returns an empty list, it means there are no obligations, there is no check
    * the king himself overrides this method*/
    /*protected ArrayList<Piece> obligativeTiles(Piece[] pieces)
    {
        ArrayList<Piece> result = new ArrayList<>();
        //if this piece is empty it can`t move!
        if (this instanceof Empty)
            return null;

        //getting the opposed color of this piece
        int opposingColor = (getIntColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;

        //returns all the pieces on board that belongs to the opponent
        ArrayList<Piece> opponentPieces = getPiecesByColor(pieces, opposingColor);

        Piece threatenigPiece = null;
        for (Piece p : opponentPieces)//looping through opponent pieces, see if there`s a check by opponent
        {
            ArrayList<Piece> threatenedTiles = p.possibleMoves(toDoubleArray(pieces));
            if (p.checks())
            {
                threatenigPiece = p;
                if (p instanceof Knight)
                    return null;
                result = threatenedTiles;
                result.add(p);//this piece can eat the piece that checks the king
            }
        }
        setActive(false);//set this piece as inactive (hollow) to see if other opponent pieces check the king if it moves
        for (Piece p : opponentPieces)//looping through opponent pieces
        {
            if (p == threatenigPiece)
                continue;
            ArrayList<Piece> threatenedTiles = p.possibleMoves(toDoubleArray(pieces));
            if (p.checks())
            {
                setActive(true);
                return null;
                //result = threatenedTiles;
            }
        }
        setActive(true);
        return result;
    }*/

    //returns list of pieces that threaten this piece. returns empty list if no one threaten this piece
    protected ArrayList<Piece> isThreatened(Piece[] pieces, int threateningColor)
    {
        ArrayList<Piece> result = new ArrayList<>();
        ArrayList<Piece> opponentPieces = getPiecesByColor(pieces, threateningColor);

        /*//remove the king
        int counter = 0;
        for (int i = 0; i < opponentPieces.size(); i++)
        {
            if (opponentPieces.get(i) instanceof King)
                counter = i;
        }
        opponentPieces.remove(counter);*/

        setActive(false);
        for (Piece opponentPiece : opponentPieces)//looping through opponent pieces
        {
            ArrayList<Piece> tileArray = opponentPiece.possibleMoves(toDoubleArray(pieces));
            for (Piece threatenedTile : tileArray)
            {
                if (threatenedTile.equals(this))
                {
                     result.add(opponentPiece);
                }
            }
        }
        setActive(true);
        return result;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("name", name);
        json.put("color", color);
        json.put("image", image);
        json.put("position", position);
        return json;
    }

    protected static Piece[][] toDoubleArray(Piece[] pieces){
        Piece[][] doublePieces = new Piece[8][8];
        int counter = 0;
        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                doublePieces[row][col] = pieces[counter];
                doublePieces[row][col].setPointPosition(row, col);
                counter++;
            }
        }
        return doublePieces;
    }

    protected static Piece[] toSingleArray(Piece[][] pieces)
    {
        Piece[] singleArray = new Piece[64];
        int counter = 0;
        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                singleArray[counter] = pieces[row][col];
                counter++;
            }
        }
        return singleArray;
    }
}
