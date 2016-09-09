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

    //done by Jony
    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        ArrayList<Piece> pieceArr = possibleMoves(game);
        for (Piece p : pieceArr)
        {
            legalMoves.add(p.getPosition());
        }
        return legalMoves;
    }

    private ArrayList<Piece> possibleMoves(Game game)
    {
        ArrayList<Piece> result = new ArrayList<Piece>();
        Piece[][] pieces = game.getGridPieces();
        int row = _pointPosition.x;
        int col = _pointPosition.y;

        //moving up
        if (row-2 >= 0)
        {
            if (col-1 >= 0)//turn left
            {
                if (!(pieces[row-2][col-1] instanceof Empty))//piece is not empty
                {
                    if (!(pieces[row-2][col-1].getColor().equals(color)))//piece is not the same color as the knight
                    {
                        if (pieces[row-2][col-1] instanceof King)
                            setCheck(true);
                        result.add(pieces[row-2][col-1]);
                    }
                }
                else
                {
                    result.add(pieces[row-2][col-1]);
                }
            }
            if (col+1 < TILES_NUMBER_IN_A_ROW)//turn right
            {
                if (!(pieces[row-2][col+1] instanceof Empty))//piece is not empty
                {
                    if (!(pieces[row-2][col+1].getColor().equals(color)))//piece is not the same color as the knight
                    {
                        if (pieces[row-2][col+1] instanceof King)
                            setCheck(true);
                        result.add(pieces[row-2][col+1]);
                    }
                }
                else
                {
                    result.add(pieces[row-2][col+1]);
                }
            }
        }
        //moving down
        if (row+2 < TILES_NUMBER_IN_A_ROW)
        {
            if (col-1 >= 0)//turn right
            {
                if (!(pieces[row+2][col-1] instanceof Empty))//piece is not empty
                {
                    if (!(pieces[row+2][col-1].getColor().equals(color)))//piece is not the same color as the knight
                    {
                        if (pieces[row+2][col-1] instanceof King)
                            setCheck(true);
                        result.add(pieces[row+2][col-1]);
                    }
                }
                else
                {
                    result.add(pieces[row+2][col-1]);
                }
            }
            if (col+1 < TILES_NUMBER_IN_A_ROW)//turn left
            {
                if (!(pieces[row+2][col+1] instanceof Empty))//piece is not empty
                {
                    if (!(pieces[row+2][col+1].getColor().equals(color)))//piece is not the same color as the knight
                    {
                        if (pieces[row+2][col+1] instanceof King)
                            setCheck(true);
                        result.add(pieces[row+2][col+1]);
                    }
                }
                else
                {
                    result.add(pieces[row+2][col+1]);
                }
            }
        }
        //moving right
        if (col+2 < TILES_NUMBER_IN_A_ROW)
        {
            if (row-1 >= 0)//turn left
            {
                if (!(pieces[row-1][col+2] instanceof Empty))//piece is not empty
                {
                    if (!(pieces[row-1][col+2].getColor().equals(color)))//piece is not the same color as the knight
                    {
                        if (pieces[row-1][col+2] instanceof King)
                            setCheck(true);
                        result.add(pieces[row-1][col+2]);
                    }
                }
                else
                {
                    result.add(pieces[row-1][col+2]);
                }
            }
            if (row+1 < TILES_NUMBER_IN_A_ROW)//turn right
            {
                if (!(pieces[row+1][col+2] instanceof Empty))//piece is not empty
                {
                    if (!(pieces[row+1][col+2].getColor().equals(color)))//piece is not the same color as the knight
                    {
                        if (pieces[row+1][col+2] instanceof King)
                            setCheck(true);
                        result.add(pieces[row+1][col+2]);
                    }
                }
                else
                {
                    result.add(pieces[row+1][col+2]);
                }
            }
        }
        //moving left
        if (col-2 >=0)
        {
            if (row-1 >= 0)//turn right
            {
                if (!(pieces[row-1][col-2] instanceof Empty))//piece is not empty
                {
                    if (!(pieces[row-1][col-2].getColor().equals(color)))//piece is not the same color as the knight
                    {
                        if (pieces[row-1][col-2] instanceof King)
                            setCheck(true);
                        result.add(pieces[row-1][col-2]);
                    }
                }
                else
                {
                    result.add(pieces[row-1][col-2]);
                }
            }
            if (row+1 < TILES_NUMBER_IN_A_ROW)//turn left
            {
                if (!(pieces[row+1][col-2] instanceof Empty))//piece is not empty
                {
                    if (!(pieces[row+1][col-2].getColor().equals(color)))//piece is not the same color as the knight
                    {
                        if (pieces[row+1][col-2] instanceof King)
                            setCheck(true);
                        result.add(pieces[row+1][col-2]);
                    }
                }
                else
                {
                    result.add(pieces[row+1][col-2]);
                }
            }
        }
        return result;
    }
}

