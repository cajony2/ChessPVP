package com.example.roma.servertest;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Bishop extends Piece {

    //constructor - added by jony
    public Bishop(int color, Tile tile){
        super(color, tile);
    }

    public Bishop(String name, String color, int pos) {
        super(name, color, pos);
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            image = R.drawable.blt60;
        }
        else
            image= R.drawable.bdt60;
    }

    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        Piece[] pieces = game.getBoard2();
        int temp = position-7;

        //up right

        while(temp%8 != 0 && temp>0){
            if(!pieces[temp].getName().equals("empty")) {
                if (!pieces[temp].getColor().equals(pieces[position].getColor()))
                    legalMoves.add(temp);
                break;
            }
            legalMoves.add(temp);
            temp-=7;
        }

        //up left

        temp=position-9;

        while( ( temp%8 != 7 ) && ( temp>0 ) ){
            if(!pieces[temp].getName().equals("empty")) {
                if (pieces[temp].getColor().equals(pieces[position].getColor()))
                    legalMoves.add(temp);
                break;
            }
            legalMoves.add(temp);
            temp -= 9;
        }

        //down right

        temp=position+9;

        while( ( temp%8 != 0 ) && ( temp<64 ) ){
            if(!pieces[temp].getName().equals("empty")) {
                if (!pieces[temp].getColor().equals(pieces[position].getColor()))
                    legalMoves.add(temp);
                break;
            }
            legalMoves.add(temp);
            temp+=9;
        }

        //down left

        temp=position+7;

        while( ( temp%8 != 7 ) && ( temp<64 )){
            if(!pieces[temp].getName().equals("empty")) {
                if (!pieces[temp].getColor().equals(pieces[position].getColor()))
                    legalMoves.add(temp);
                break;
            }
            legalMoves.add(temp);
            temp+=7;
        }



        return legalMoves;
    }

}
