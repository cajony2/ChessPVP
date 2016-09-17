package com.example.roma.servertest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Pawn extends Piece {

    //boolean hasNotMovedYet;

    /*//constructor - added by jony
    public  Pawn(int color, Tile tile){
        super(color, tile);
    }*/

    public Pawn(String name, String color, int pos) {
        super(name, color, pos);
        //hasNotMovedYet = true;
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            image = R.drawable.plt60;
        }
        else
            image=R.drawable.pdt60;
    }

    public Pawn(){}

    public Pawn (Piece piece)
    {
        super(piece.getIntColor());
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
        //_checksKing = piece.checks();
        _isFlipped = piece._isFlipped;
        name = "pawn";
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
    }

    //Jony added: pawn can move 2 squares at the beginning and setCheck if can eat the king(not tested yet)
    @Override
    public ArrayList<Integer> getLegalMoves(Piece[] pieces) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        ArrayList<Piece> possibleMoves = possibleMoves(toDoubleArray(pieces));
        for (Piece p : possibleMoves)
        {
            legalMoves.add(p.getPosition());
        }
        return legalMoves;
        /*ArrayList<Piece> obligatedTiles = obligativeTiles(pieces);//the tiles this piece have to go to in order to block check
        if (obligativeTiles(pieces) == null)//if it moves the king is exposed, so can`t move
        {
            return legalMoves;
        }
        if (obligatedTiles.size() == 0)//no restrictions on the moves
        {
            for (Piece p : possibleMoves)
            {
                legalMoves.add(p.getPosition());
            }
            return legalMoves;
        }
        else//merge between obligatedTiles and possibleMoves to see where this piece can move
        {
            ArrayList<Piece> mergedList = new ArrayList<>();
            for (Piece pm : possibleMoves)
            {
                for (Piece ot : obligatedTiles)
                {
                    if (pm.equals(ot))
                    {
                        mergedList.add(pm);
                    }
                }
                //legalMoves.add(pm.getPosition());
            }
            for (Piece p : mergedList)
            {
                legalMoves.add(p.getPosition());
            }
            return legalMoves;
        }*/
    }

    public ArrayList<Piece> possibleMoves(Piece[][] pieces)
    {
        ArrayList<Piece> result = new ArrayList<Piece>();
        if (!getActive())
        {
            return result;
        }
        int row = _pointPosition.x;
        int col = _pointPosition.y;

        if (getIntColor() == Color.WHITE)
        {
            if (pieces[row+1][col].getName().equals("empty"))
            {
                result.add(pieces[row+1][col]);
            }
            if (row == 1)//pawn hasn`t moved yet, can move 2 tiles
            {
                if (pieces[row+2][col].getName().equals("empty"))
                {
                    result.add(pieces[row+2][col]);
                }
            }
            if (row+1 < TILES_NUMBER_IN_A_ROW && col+1 < TILES_NUMBER_IN_A_ROW)
            {
                if ((!pieces[row+1][col+1].getName().equals("empty") && pieces[row+1][col+1].getIntColor() != getIntColor()))
                {
                    result.add(pieces[row+1][col+1]);
                }
            }
            if (row+1 < TILES_NUMBER_IN_A_ROW && col-1 >= 0)
            {
                if ((!pieces[row+1][col-1].getName().equals("empty") && pieces[row+1][col-1].getIntColor() != getIntColor()))
                {
                    result.add(pieces[row+1][col-1]);
                }
            }
        }
        else//pawn is black
        {
            if (pieces[row-1][col].getName().equals("empty"))
            {
                result.add(pieces[row-1][col]);
            }
            if (row == 7)//pawn hasn`t moved yet, can move 2 tiles
            {
                if (pieces[row-2][col].getName().equals("empty"))
                {
                    result.add(pieces[row-2][col]);
                }
            }
            if (col+1 < TILES_NUMBER_IN_A_ROW)
            {
                if ((!pieces[row-1][col+1].getName().equals("empty") && pieces[row-1][col+1].getIntColor() != getIntColor()) || (pieces[row-1][col+1].getActive()))
                {
                    result.add(pieces[row-1][col+1]);
                }
            }
            if (col-1 >= 0)
            {
                if ((!pieces[row-1][col-1].getName().equals("empty") && pieces[row-1][col-1].getIntColor() != getIntColor()) || (pieces[row-1][col-1].getActive()))
                {
                    result.add(pieces[row-1][col-1]);
                }
            }
        }
        return result;
    }

}

