package com.example.roma.servertest;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Pawn extends Piece {

    boolean hasNotMovedYet;

    /*//constructor - added by jony
    public  Pawn(int color, Tile tile){
        super(color, tile);
    }*/

    public Pawn(String name, String color, int pos) {
        super(name, color, pos);
        hasNotMovedYet = true;
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            image = R.drawable.plt60;
        }
        else
            image=R.drawable.pdt60;
    }

    //Jony added: pawn can move 2 squares at the beginning and setCheck if can eat the king(not tested yet)
    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        Piece[] pieces = game.getBoard2();
        int temp = position;
        int beginTemp = position;
        String color = pieces[position].getColor();

        if(color.equals("white")) {
            temp=position+8;
            beginTemp = temp + 8;//at the beginning the pawn can move two squares
            //move forward
            if(pieces[temp].isEmpty)
            {
                if(hasNotMovedYet && pieces[beginTemp].isEmpty)
                {
                    legalMoves.add(beginTemp);
                }
                legalMoves.add(temp);
            }

            //eat right
            temp = position+9;
            if(temp%8 != 0 && temp<64 && !(pieces[temp].isEmpty)  &&  !(pieces[temp].equals(color)))
            {
                legalMoves.add(temp);
                if (pieces[temp] instanceof King)
                {
                    setCheck(true);
                }
            }
            //eat left
            temp = position+7;
            if(temp%8 != 7 && temp<64 && !(pieces[temp].isEmpty)  &&  !(pieces[temp].equals(color)))
            {
                legalMoves.add(temp);
                if (pieces[temp] instanceof King)
                {
                    setCheck(true);
                }
            }
        }else{
            temp=position-8;
            beginTemp = temp - 8;//at the beginning the pawn can move two squares
            //move forward
            if(pieces[temp].isEmpty)
            {
                if(hasNotMovedYet && pieces[beginTemp].isEmpty)
                {
                    legalMoves.add(beginTemp);
                }
                legalMoves.add(temp);
            }
            //eat right
            temp = position-7;
            if(temp%8 != 0 && temp>=0 && !(pieces[temp].isEmpty)  &&  !(pieces[temp].equals(color)))
            {
                legalMoves.add(temp);
                if (pieces[temp] instanceof King)
                {
                    setCheck(true);
                }
            }
            //eat left
            temp = position-9;
            if(temp%8 != 7 &&  temp>=0 && !(pieces[temp].isEmpty)  &&  !(pieces[temp].equals(color)))
            {
                legalMoves.add(temp);
                if (pieces[temp] instanceof King)
                {
                    setCheck(true);
                }
            }
        }
        return legalMoves;
    }


    /*ArrayList<Piece> possibleMoves(Game game) {
        return null;
    }*/


}

