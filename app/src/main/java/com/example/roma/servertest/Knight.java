package com.example.roma.servertest;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */


public class Knight extends Piece {

    //constructor - added by jony
    public  Knight(int color, Tile tile){
        super(color, tile);
    }

    public Knight(String name, String color, int pos) {
        super(name, color, pos);
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            image = R.drawable.nlt60;
        }
        else
            image=R.drawable.ndt60;
    }

    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        return null;
    }

}

