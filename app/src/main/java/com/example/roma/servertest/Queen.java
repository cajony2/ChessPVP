package com.example.roma.servertest;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Queen extends Piece {

    //constructor - added by jony
    public  Queen(Context context, int color, Tile tile){
        super(context, color, tile);
    }

    public Queen(Context context, String name, String color,int pos) {
        super(context, name, color, pos);
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            this.image = R.drawable.qlt60;
        }
        else
            this.image=R.drawable.qdt60;
    }

    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        return null;
    }

}
