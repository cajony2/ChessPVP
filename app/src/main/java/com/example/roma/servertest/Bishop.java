package com.example.roma.servertest;

import android.content.Context;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roma on 8/2/2016.
 */

public class Bishop extends Piece {

    /*//constructor - added by jony
    public Bishop(int color, Tile tile){
        super(color, tile);
    }*/

    public Bishop(String name, String color, int pos) {
        super(name, color, pos);
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            image = R.drawable.blt60;
        }
        else
            image= R.drawable.bdt60;
    }

    public Bishop(){}

    public Bishop (Piece piece)
    {
        super(piece.getIntColor());
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
        //_checksKing = piece.checks();
        _isFlipped = piece._isFlipped;
        name = "bishop";
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
    }

    //done by Jony
    @Override
    public ArrayList<Integer> getLegalMoves(Piece[] pieces) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        ArrayList<Piece> possibleMoves = possibleMoves(toDoubleArray(pieces));
        ArrayList<Piece> opponentPieces = opponentPieces(pieces);
        for (Piece p : opponentPieces)
        {
            if (p.checks(pieces))
            {
                ArrayList<Piece> theWayToTheKing = p.wayToTheKing(pieces);
                if (canMove(pieces))//queen can move
                {
                    ArrayList<Piece> mergedList = new ArrayList<>();
                    for (Piece piece : possibleMoves)
                    {
                        for (Piece tile : theWayToTheKing)
                        {
                            if (piece.equals(tile))
                            {
                                mergedList.add(piece);
                            }
                        }
                    }
                    for (Piece ml : mergedList)
                    {
                        legalMoves.add(ml.getPosition());
                    }
                    return legalMoves;
                }
                else//queen can not move
                {
                    return legalMoves;
                }
            }
        }
        for (Piece p : possibleMoves)
        {
            legalMoves.add(p.getPosition());
        }
        return legalMoves;
    }

    /*@Override
    protected boolean canMove(Piece[] pieces) {
        return false;
    }*/

    public ArrayList<Piece> possibleMoves(Piece[][] pieces) {

        ArrayList<Piece> result = new ArrayList<Piece>();
        if (!getActive())
        {
            return result;
        }
        int row = _pointPosition.x;
        int col = _pointPosition.y;

        //moving up right
        int r = row-1;
        int c = col+1;
        while (r >= 0 && c < TILES_NUMBER_IN_A_ROW)
        {
            if (!(pieces[r][c].getName().equals("empty")))//piece is not empty
            {
                if (!pieces[r][c].getActive())//add to possibleMoves if not active
                    result.add(pieces[r][c]);
                else
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
                if (!pieces[r][c].getActive())//add to possibleMoves if not active
                    result.add(pieces[r][c]);
                else
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
                if (!pieces[r][c].getActive())//add to possibleMoves if not active
                    result.add(pieces[r][c]);
                else
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
                if (!pieces[r][c].getActive())//add to possibleMoves if not active
                    result.add(pieces[r][c]);
                else
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
