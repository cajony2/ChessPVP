package com.example.roma.servertest;

import android.graphics.Point;
import java.util.ArrayList;

public class Knight extends Piece {

    //Constructor
    public Knight(String name, String color, int pos,boolean moved) {
        super(name, color, pos, moved);
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            image = R.drawable.nlt60;
        }
        else
            image=R.drawable.ndt60;
    }

    //Constructor
    public Knight (Piece piece) {
        super(piece.getIntColor());
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
        _checksKing = piece._checksKing;
        _isFlipped = piece._isFlipped;
        name = "knight";
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
        _hasNotMovedYet = piece.hasNotMovedYet();
    }


    @Override
    public ArrayList<Integer> getLegalMoves(Piece[][] pieces) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        ArrayList<Piece> possibleMoves = possibleMoves(pieces);
        ArrayList<Piece> opponentPieces = opponentPieces(pieces);
        for (Piece p : opponentPieces)
        {
            if (p.checks(pieces))
            {
                ArrayList<Piece> theWayToTheKing = p.wayToTheKing(pieces);
                if (canMove(pieces))//knight can move
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
                else//knight can not move
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

    public ArrayList<Piece> possibleMoves(Piece[][] pieces) {
        ArrayList<Piece> result = new ArrayList<Piece>();
        if (!getActive())
        {
            return result;
        }
        int row = _pointPosition.x;
        int col = _pointPosition.y;

        //moving down
        if (row-2 >= 0)
        {
            if (col-1 >= 0)//turn left
            {
                if (!(pieces[row-2][col-1].getName().equals("empty")))//piece is not empty
                {
                    if (!(pieces[row-2][col-1].getColor().equals(color)) || !pieces[row-2][col-1].getActive())//piece is different color or inactive
                    {
                        if (pieces[row-2][col-1].getName().equals("king"))
                            setCheck(true);
                        result.add(pieces[row-2][col-1]);
                    }
                }
                else//piece is empty
                {
                    result.add(pieces[row-2][col-1]);
                }
            }
            if (col+1 < TILES_NUMBER_IN_A_ROW)//turn right
            {
                if (!(pieces[row-2][col+1].getName().equals("empty")))//piece is not empty
                {
                    if (!(pieces[row-2][col+1].getColor().equals(color)) || !pieces[row-2][col+1].getActive())//piece is different color or inactive
                    {
                        if (pieces[row-2][col+1].getName().equals("king"))
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
        //moving up
        if (row+2 < TILES_NUMBER_IN_A_ROW)
        {
            if (col-1 >= 0)//turn right
            {
                if (!(pieces[row+2][col-1].getName().equals("empty")))//piece is not empty
                {
                    if (!(pieces[row+2][col-1].getColor().equals(color)) || !pieces[row+2][col-1].getActive())//piece is different color or inactive
                    {
                        if (pieces[row+2][col-1].getName().equals("king"))
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
                if (!(pieces[row+2][col+1].getName().equals("empty")))//piece is not empty
                {
                    if (!(pieces[row+2][col+1].getColor().equals(color)) || !pieces[row+2][col+1].getActive())//piece is different color or inactive
                    {
                        if (pieces[row+2][col+1].getName().equals("king"))
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
                if (!(pieces[row-1][col+2].getName().equals("empty")))//piece is not empty
                {
                    if (!(pieces[row-1][col+2].getColor().equals(color)) || !pieces[row-1][col+2].getActive())//piece is different color or inactive
                    {
                        if (pieces[row-1][col+2].getName().equals("king"))
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
                if (!(pieces[row+1][col+2].getName().equals("empty")))//piece is not empty
                {
                    if (!(pieces[row+1][col+2].getColor().equals(color)) || !pieces[row+1][col+2].getActive())//piece is different color or inactive
                    {
                        if (pieces[row+1][col+2].getName().equals("king"))
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
                if (!(pieces[row-1][col-2].getName().equals("empty")))//piece is not empty
                {
                    if (!(pieces[row-1][col-2].getColor().equals(color)) || !pieces[row-1][col-2].getActive())//piece is different color or inactive
                    {
                        if (pieces[row-1][col-2].getName().equals("king"))
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
                if (!(pieces[row+1][col-2].getName().equals("empty")))//piece is not empty
                {
                    if (!(pieces[row+1][col-2].getColor().equals(color)) || !pieces[row+1][col-2].getActive())//piece is different color or inactive
                    {
                        if (pieces[row+1][col-2].getName().equals("king"))
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

