package com.example.roma.servertest;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Empty extends Piece {

    //constructor - added by jony
    public Empty(int color, Tile tile){
        super(color, tile);
    }

    public Empty(String name, String color, int pos) {
        super(name, color, pos);
        // TODO Auto-generated constructor stub
        if(color=="white")
            image=R.drawable.white;
        else
            image=R.drawable.black;
    }

    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        return null;
    }

    @Override
    ArrayList<Piece> possibleMoves(Game game) {
        return null;
    }


}
