package com.example.roma.servertest;

import android.content.Context;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Empty extends Piece {

    /*//constructor - added by jony
    public Empty(int color, Tile tile){
        super(color, tile);
    }*/

    public Empty(String name, String color, int pos) {
        super(name, color, pos);
        // TODO Auto-generated constructor stub
        if(color=="white")
            image=R.drawable.white;
        else
            image=R.drawable.black;
    }

    public Empty(){}

    public Empty (Piece piece)
    {
        super(piece.getIntColor());
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = true;
        _checksKing = false;
        _isFlipped = piece._isFlipped;
        name = "empty";
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = true;
    }

    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        return null;
    }

    ArrayList<Piece> possibleMoves(Game game) {
        return null;
    }


}
