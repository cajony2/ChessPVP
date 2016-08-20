package com.example.roma.servertest;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Empty extends Piece {


    public Empty(String name, String color,int pos) {
        super(name, color,pos);
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