package com.example.roma.servertest;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class Rook extends Piece {

    boolean hasNotMovedYet;

    /*//constructor - added by jony
    public  Rook(int color, Tile tile){
        super(color, tile);
    }*/

    public Rook(String name, String color,int pos) {
        super(name, color, pos);
        hasNotMovedYet = true;
        // TODO Auto-generated constructor stub
        //Log.i("chess","creating new piece "+name+" color:"+color+" pos "+pos );
        if (color.equals("white")){
            image = R.drawable.rlt60;
        }
        else
            image=R.drawable.rdt60;
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

    @Override
    protected boolean canMove(Game game) {
        return false;
    }

    private ArrayList<Piece> possibleMoves(Game game) {
        ArrayList<Piece> result = new ArrayList<Piece>();
        Piece[][] pieces = game.getGridPieces();
        int row = _pointPosition.x;
        int col = _pointPosition.y;

        //moving downward
        for (int r = row+1; r < TILES_NUMBER_IN_A_ROW; r++)
        {
            if (!(pieces[r][col] instanceof Empty))//piece is not empty
            {
                if (pieces[r][col].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[r][col].getActive())//piece is active
                    {
                        if (pieces[r][col] instanceof King)
                            setCheck(true);
                        result.add(pieces[r][col]);
                        break;
                    }
                    else
                    {
                        result.add(pieces[r][col]);
                    }
                }
            }
            else//piece is empty
            {
                result.add(pieces[r][col]);
            }
        }
        //moving upward
        for (int r = row-1; r > 0; r--)
        {
            if (!(pieces[r][col] instanceof Empty))//piece is not empty
            {
                if (pieces[r][col].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[r][col].getActive())//piece is active
                    {
                        if (pieces[r][col] instanceof King)
                            setCheck(true);
                        result.add(pieces[r][col]);
                        break;
                    }
                    else
                    {
                        result.add(pieces[r][col]);
                    }
                }
            }
            else//piece is empty
            {
                result.add(pieces[r][col]);
            }
        }
        //moving left
        for (int c = col-1; c > 0; c--)
        {
            if (!(pieces[row][c] instanceof Empty))//piece is not empty
            {
                if (pieces[row][c].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[row][c].getActive())//piece is active
                    {
                        if (pieces[row][c] instanceof King)
                            setCheck(true);
                        result.add(pieces[row][c]);
                        break;
                    }
                    else
                    {
                        result.add(pieces[row][c]);
                    }
                }
            }
            else//piece is empty
            {
                result.add(pieces[row][c]);
            }
        }
        //moving right
        for (int c = col+1; c < TILES_NUMBER_IN_A_ROW; c++)
        {
            if (!(pieces[row][c] instanceof Empty))//piece is not empty
            {
                if (pieces[row][c].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[row][c].getActive())//piece is active
                    {
                        if (pieces[row][c] instanceof King)
                            setCheck(true);
                        result.add(pieces[row][c]);
                        break;
                    }
                    else
                    {
                        result.add(pieces[row][c]);
                    }
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
