package com.example.roma.servertest;

import android.graphics.Color;
import android.graphics.Point;
import java.util.ArrayList;

public class Pawn extends Piece {

    //Constructor
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

    //Constructor
    public Pawn (Piece piece) {
        super(piece.getIntColor());
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
        _checksKing = piece._checksKing;
        _isFlipped = piece._isFlipped;
        name = "pawn";
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
        _hasNotMovedYet = piece.hasNotMovedYet();
    }


    @Override
    public ArrayList<Integer> getLegalMoves(Piece[] pieces) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        ArrayList<Piece> possibleMoves = possibleMoves(toDoubleArray(pieces));//where the pawn can move
        ArrayList<Piece> possibleEatingMoves = possibleEatingMoves(toDoubleArray(pieces));//where the pawn can eat
        for (Piece p : possibleEatingMoves)
        {
            if (!(p.getName().equals("empty")) && p.getIntColor() != getIntColor())
            {
                possibleMoves.add(p);
            }
        }
        ArrayList<Piece> opponentPieces = opponentPieces(pieces);
        for (Piece p : opponentPieces)
        {
            if (p.checks(pieces))
            {
                ArrayList<Piece> theWayToTheKing = p.wayToTheKing(pieces);
                if (canMove(pieces))//pawn can move
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
                else//pawn can not move
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

        if (getIntColor() == Color.WHITE)
        {
            if (row+1 < TILES_NUMBER_IN_A_ROW)
            {
                if (pieces[row + 1][col].getName().equals("empty")) {
                    result.add(pieces[row + 1][col]);
                    if (row == 1)//pawn hasn`t moved yet, can move 2 tiles
                    {
                        if (pieces[row + 2][col].getName().equals("empty")) {
                            result.add(pieces[row + 2][col]);
                        }
                    }
                }
            }
        }
        else//pawn is black
        {
            if (row-1 > 0)
            {
                if (pieces[row - 1][col].getName().equals("empty")) {
                    result.add(pieces[row - 1][col]);
                    if (row == 6)//pawn hasn`t moved yet, can move 2 tiles
                    {
                        if (pieces[row - 2][col].getName().equals("empty")) {
                            result.add(pieces[row - 2][col]);
                        }
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Piece> possibleEatingMoves(Piece[][] pieces) {
        ArrayList<Piece> result = new ArrayList<Piece>();
        if (!getActive())
        {
            return result;
        }
        int row = _pointPosition.x;
        int col = _pointPosition.y;

        if (getIntColor() == Color.WHITE)
        {
            if (row+1 < TILES_NUMBER_IN_A_ROW && col+1 < TILES_NUMBER_IN_A_ROW)
            {
                result.add(pieces[row+1][col+1]);
            }
            if (row+1 < TILES_NUMBER_IN_A_ROW && col-1 >= 0)
            {
                result.add(pieces[row+1][col-1]);
            }
        }
        else//pawn is black
        {
            if (row-1 > 0 && col+1 < TILES_NUMBER_IN_A_ROW)
            {
                result.add(pieces[row-1][col+1]);
            }
            if (row-1 > 0 && col-1 >= 0)
            {
                result.add(pieces[row-1][col-1]);
            }
        }
        return result;
    }

}

