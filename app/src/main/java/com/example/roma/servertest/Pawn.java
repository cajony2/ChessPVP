package com.example.roma.servertest;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Pawn extends Piece {

    public Pawn(String name, String color,int pos) {
        super(name, color,pos);
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            this.image = R.drawable.plt60;
        }
        else
            this.image=R.drawable.pdt60;
    }

    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        Piece[] pieces = game.getBoard2();
        int temp = position;
        String color = pieces[position].getColor();


        if(color.equals("white")) {
            temp=position+8;
            //move forward
            if(pieces[temp].isEmpty)
                legalMoves.add(temp);

            //eat right
            temp = position+9;
            if(temp%8 != 0 && temp<64 && !(pieces[temp].isEmpty)  &&  !(pieces[temp].equals(color)) )
                legalMoves.add(temp);
            //eat left
            temp = position+7;
            if(temp%8 != 7 && temp<64 && !(pieces[temp].isEmpty)  &&  !(pieces[temp].equals(color)) )
                legalMoves.add(temp);

        }else{
            temp=position-8;
            //move forward
            if(pieces[temp].isEmpty)
                legalMoves.add(temp);
            //eat right
            temp = position-7;
            if(temp%8 != 0 && temp>=0 && !(pieces[temp].isEmpty)  &&  !(pieces[temp].equals(color)) )
                legalMoves.add(temp);
            //eat left
            temp = position-9;
            if(temp%8 != 7 &&  temp>=0 && !(pieces[temp].isEmpty)  &&  !(pieces[temp].equals(color)) )
                legalMoves.add(temp);
        }




        return legalMoves;
    }


}

