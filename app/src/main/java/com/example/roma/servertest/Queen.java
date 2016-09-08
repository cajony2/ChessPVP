package com.example.roma.servertest;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Roma on 8/2/2016.
 */

public class Queen extends Piece {

    //constructor - added by jony
    public  Queen(int color, Tile tile){
        super(color, tile);
    }

    public Queen(String name, String color,int pos) {
        super(name, color, pos);
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            image = R.drawable.qlt60;
        }
        else
            image=R.drawable.qdt60;
    }

    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        Piece[] pieces = game.getBoard2();
        return null;
    }

    @Override
    ArrayList<Piece> possibleMoves(Game game) {

        ArrayList<Piece> result = new ArrayList<Piece>();
        Piece[][] pieces = game.getGridPieces();
        int row = _pointPosition.x;
        int col = _pointPosition.y;

        //moving downward
        for (int r = row; r < TILES_NUMBER_IN_A_ROW; r++)
        {
            if (!(pieces[r][col] instanceof Empty))//piece is not empty
            {
                if (pieces[r][col].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[r][col] instanceof King)
                        setCheck(true);
                    result.add(pieces[r][col]);
                    break;
                }
            }
            else
            {
                result.add(pieces[r][col]);
            }
        }
        //moving upward
        for (int r = row; r > 0; r--)
        {
            if (!(pieces[r][col] instanceof Empty))//piece is not empty
            {
                if (pieces[r][col].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[r][col] instanceof King)
                        setCheck(true);
                    result.add(pieces[r][col]);
                    break;
                }
            }
            else
            {
                result.add(pieces[r][col]);
            }
        }
        //moving left
        for (int c = col; c > 0; c--)
        {
            if (!(pieces[row][c] instanceof Empty))//piece is not empty
            {
                if (pieces[row][c].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[row][c] instanceof King)
                        setCheck(true);
                    result.add(pieces[row][c]);
                    break;
                }
            }
            else
            {
                result.add(pieces[row][c]);
            }
        }
        //moving right
        for (int c = col; c < TILES_NUMBER_IN_A_ROW; c++)
        {
            if (!(pieces[row][c] instanceof Empty))//piece is not empty
            {
                if (pieces[row][c].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[row][c] instanceof King)
                        setCheck(true);
                    result.add(pieces[row][c]);
                    break;
                }
            }
            else
            {
                result.add(pieces[row][c]);
            }
        }
        return result;
    }

}
