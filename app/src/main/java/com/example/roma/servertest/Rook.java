package com.example.roma.servertest;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Rook extends Piece {

    //constructor - added by jony
    public  Rook(int color, Tile tile){
        super(color, tile);
    }


    public Rook(String name, String color,int pos) {
        super(name, color, pos);
        // TODO Auto-generated constructor stub
        //Log.i("chess","creating new piece "+name+" color:"+color+" pos "+pos );
        if (color.equals("white")){
            image = R.drawable.rlt60;
        }
        else
            image=R.drawable.rdt60;
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
