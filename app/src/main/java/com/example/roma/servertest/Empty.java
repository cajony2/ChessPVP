package com.example.roma.servertest;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Empty extends Piece {

    //constructor - added by jony
    public Empty(Context context, int color, Tile tile){
        super(context, color, tile);
    }

    public Empty(Context context, String name, String color, int pos) {
        super(context, name, color, pos);
        // TODO Auto-generated constructor stub
        if(color=="white")
            this.image=R.drawable.white;
        else
            this.image=R.drawable.black;
    }

    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        return null;
    }


}
