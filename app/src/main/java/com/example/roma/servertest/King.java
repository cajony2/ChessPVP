package com.example.roma.servertest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class King extends Piece {

    public boolean hasNotMovedYet;

    /*//constructor - added by jony
    public  King(int color, Tile tile){
        super(color, tile);
    }*/

    public King(String name, String color, int pos) {
        super(name, color, pos);
        boolean hasNotMovedYet = true;
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            image =R.drawable.klt60;
        }
        else
            image=R.drawable.kdt60;
    }

    public King(){}

    public King (Piece piece)
    {
        super(piece.getIntColor());
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
        //_checksKing = piece.checks();
        _isFlipped = piece._isFlipped;
        name = "king";
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
    }

    @Override
    public ArrayList<Integer> getLegalMoves(Piece[] pieces)
	{
		int opponentColor = (getIntColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        ArrayList<Piece> possibleMoves = possibleMoves(toDoubleArray(pieces));
		for (Piece p : possibleMoves)
		{
			if (p.isThreatened(pieces, opponentColor).size() == 0)
				legalMoves.add(p.getPosition());
		}
		return legalMoves;
    }

	//overrides the Piece`s canMove method
    public boolean canMove(Piece[] pieces)
    {
		int opponentColor = (getIntColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;
        //extracting only opponent pieces
        //ArrayList<Piece> opponentPieces = opponentPieces(pieces);
		ArrayList<Piece> result = new ArrayList<>();
		ArrayList<Piece> kingPossibleMoves = possibleMoves(toDoubleArray(pieces));
		for (Piece p : kingPossibleMoves)
		{
			result = p.isThreatened(pieces, opponentColor);
			if (result.size() != 0)//someone threatens this tile
			{
				return false;
			}
		}
        return true;
    }

    private boolean kingCanMoveThere(Piece[] pieces, ArrayList<Piece> opponentPieces, int tileLocation) {


        for (Piece p : opponentPieces)
        {
            if (p.getLegalMoves(pieces).contains(pieces[tileLocation]))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public ArrayList<Piece> possibleMoves(Piece[][] pieces){
        ArrayList<Piece> result = new ArrayList<Piece>();
		if (!getActive())
		{
			return result;
		}
        int row = _pointPosition.x;
        int col = _pointPosition.y;
        //getting the opposed color of this piece
        int opposingColor = (getIntColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;

        if (row+1 < TILES_NUMBER_IN_A_ROW)//can move up
        {
			if (pieces[row+1][col].isEmpty())//piece is empty
			{
				result.add(pieces[row+1][col]);
			}
			else//piece is not empty
			{
				if (pieces[row+1][col].getIntColor() != getIntColor())//piece is in different color
				{
					result.add(pieces[row+1][col]);
				}
			}
        }
        if (col+1 < TILES_NUMBER_IN_A_ROW)//can move right
        {
			if (pieces[row][col+1].isEmpty())//piece is empty
			{
				result.add(pieces[row][col+1]);
			}
			else//piece is not empty
			{
				if (pieces[row][col+1].getIntColor() != getIntColor())//piece is in different color
				{
					result.add(pieces[row][col+1]);
				}
			}
        }
        if (col-1 >= 0)//can move left
        {
			if (pieces[row][col-1].isEmpty())//piece is empty
			{
				result.add(pieces[row][col-1]);
			}
			else//piece is not empty
			{
				if (pieces[row][col-1].getIntColor() != getIntColor())//piece is in different color
				{
					result.add(pieces[row][col-1]);
				}
			}
        }
        if (row-1 >= 0)//can move down
        {
			if (pieces[row-1][col].isEmpty())//piece is empty
			{
				result.add(pieces[row-1][col]);
			}
			else//piece is not empty
			{
				if (pieces[row-1][col].getIntColor() != getIntColor())//piece is in different color
				{
					result.add(pieces[row-1][col]);
				}
			}
        }
        if (row+1 < TILES_NUMBER_IN_A_ROW && col+1 < TILES_NUMBER_IN_A_ROW)//can move up right
        {
			if (pieces[row+1][col+1].isEmpty())//piece is empty
			{
				result.add(pieces[row+1][col+1]);
			}
			else//piece is not empty
			{
				if (pieces[row+1][col+1].getIntColor() != getIntColor())//piece is in different color
				{
					result.add(pieces[row+1][col+1]);
				}
			}
        }
        if (row+1 < TILES_NUMBER_IN_A_ROW && col-1 >= 0)//can move up left
        {
			if (pieces[row+1][col-1].isEmpty())//piece is empty
			{
				result.add(pieces[row+1][col-1]);
			}
			else//piece is not empty
			{
				if (pieces[row+1][col-1].getIntColor() != getIntColor())//piece is in different color
				{
					result.add(pieces[row+1][col-1]);
				}
			}
        }
        if (row-1 >= 0 && col-1 >= 0)//can move down left
        {
			if (pieces[row-1][col-1].isEmpty())//piece is empty
			{
				result.add(pieces[row-1][col-1]);
			}
			else//piece is not empty
			{
				if (pieces[row-1][col-1].getIntColor() != getIntColor())//piece is in different color
				{
					result.add(pieces[row-1][col-1]);
				}
			}
        }
        if (row-1 >= 0 && col+1 < TILES_NUMBER_IN_A_ROW)//can move down right
        {
			if (pieces[row-1][col+1].isEmpty())//piece is empty
			{
				result.add(pieces[row-1][col+1]);
			}
			else//piece is not empty
			{
				if (pieces[row-1][col+1].getIntColor() != getIntColor())//piece is in different color
				{
					result.add(pieces[row-1][col+1]);
				}
			}
        }
        return result;
    }
}

