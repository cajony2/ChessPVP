package com.example.roma.servertest;

import android.graphics.Point;
import java.util.ArrayList;

public class Empty extends Piece {

    //Constructor
    public Empty(String name, String color, int pos,boolean moved) {
        super(name, color, pos, moved);
        if(color=="white")
            image=R.drawable.white;
        else
            image=R.drawable.black;
    }

    //Constructor
    public Empty (Piece piece) {
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
        _hasNotMovedYet = true;
    }

    @Override
    public ArrayList<Integer> getLegalMoves(Piece[][] pieces) {
        return null;
    }

    @Override
    public ArrayList<Piece> possibleMoves(Piece[][] pieces) {
        return null;
    }
}
