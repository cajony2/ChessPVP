package com.example.roma.servertest;

import android.content.Context;
import android.graphics.Point;
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

    /*//constructor - added by jony
    public  Queen(int color, Tile tile){
        super(color, tile);
    }*/

    public Queen(String name, String color,int pos) {
        super(name, color, pos);
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            image = R.drawable.qlt60;
        }
        else
            image=R.drawable.qdt60;
    }


    public Queen()
    {

    }

    public Queen (Piece piece)
    {
        super(piece.getIntColor());
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
        _checksKing = piece.checks();
        _isFlipped = piece._isFlipped;
        name = piece.getName();
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
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


    private ArrayList<Piece> possibleMoves(Game game) {

        ArrayList<Piece> result = new ArrayList<Piece>();
        Piece[][] pieces = game.getGridPieces();
        int row = _pointPosition.x;
        int col = _pointPosition.y;

        //moving downward
        for (int r = row+1; r < TILES_NUMBER_IN_A_ROW; r++)
        {
            if (!(pieces[r][col].getName().equals("empty")))//piece is not empty
            {
                if (pieces[r][col].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[r][col].getActive())//piece is active
                    {
                        if (pieces[r][col].getName().equals("king"))
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
            else
            {
                result.add(pieces[r][col]);
            }
        }
        //moving upward
        for (int r = row-1; r >= 0; r--)
        {
            if (!(pieces[r][col].getName().equals("empty")))//piece is not empty
            {
                if (pieces[r][col].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[r][col].getActive())//piece is active
                    {
                        if (pieces[r][col].getName().equals("king"))
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
            else
            {
                result.add(pieces[r][col]);
            }
        }
        //moving left
        for (int c = col-1; c >= 0; c--)
        {
            if (!(pieces[row][c].getName().equals("empty")))//piece is not empty
            {
                if (pieces[row][c].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[row][c].getActive())//piece is active
                    {
                        if (pieces[row][c].getName().equals("king"))
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
        //moving right
        for (int c = col+1; c < TILES_NUMBER_IN_A_ROW; c++)
        {
            if (!(pieces[row][c].getName().equals("empty")))//piece is not empty
            {
                if (pieces[row][c].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[row][c].getActive())//piece is active
                    {
                        if (pieces[row][c].getName().equals("king"))
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
        //moving up right
        int r = row-1;
        int c = col+1;
        while (r >= 0 && c < TILES_NUMBER_IN_A_ROW)
        {
            if (!(pieces[r][c].getName().equals("empty")))//piece is not empty
            {
                if (pieces[r][c].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[r][c].getActive())//piece is active
                    {
                        if (pieces[r][c].getName().equals("king"))
                            setCheck(true);
                        result.add(pieces[r][c]);
                        break;
                    }
                    else
                    {
                        result.add(pieces[r][c]);
                    }
                }
            }
            else
            {
                result.add(pieces[r][c]);
                r--;
                c++;
            }
        }

        //moving up left
        r = row-1;
        c = col-1;
        while (r >= 0 && c >= 0)
        {
            if (!(pieces[r][c].getName().equals("empty")))//piece is not empty
            {
                if (pieces[r][c].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[r][c].getActive())//piece is active
                    {
                        if (pieces[r][c].getName().equals("king"))
                            setCheck(true);
                        result.add(pieces[r][c]);
                        break;
                    }
                    else
                    {
                        result.add(pieces[r][c]);
                    }
                }
            }
            else
            {
                result.add(pieces[r][c]);
                r--;
                c--;
            }
        }

        //moving down left
        r = row+1;
        c = col-1;
        while (r < TILES_NUMBER_IN_A_ROW && c >= 0)
        {
            if (!(pieces[r][c].getName().equals("empty")))//piece is not empty
            {
                if (pieces[r][c].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[r][c].getActive())//piece is active
                    {
                        if (pieces[r][c].getName().equals("king"))
                            setCheck(true);
                        result.add(pieces[r][c]);
                        break;
                    }
                    else
                    {
                        result.add(pieces[r][c]);
                    }
                }
            }
            else
            {
                result.add(pieces[r][c]);
                r++;
                c--;
            }
        }

        //moving down right
        r = row+1;
        c = col+1;
        while (r < TILES_NUMBER_IN_A_ROW && c < TILES_NUMBER_IN_A_ROW)
        {
            if (!(pieces[r][c].getName().equals("empty")))//piece is not empty
            {
                if (pieces[r][c].getColor().equals(color))//piece is the same color as the queen
                {
                    break;
                }
                else//piece is in different color
                {
                    if (pieces[r][c].getActive())//piece is active
                    {
                        if (pieces[r][c].getName().equals("king"))
                            setCheck(true);
                        result.add(pieces[r][c]);
                        break;
                    }
                    else
                    {
                        result.add(pieces[r][c]);
                    }
                }
            }
            else
            {
                result.add(pieces[r][c]);
                r++;
                c++;
            }
        }
        return result;
    }

}
