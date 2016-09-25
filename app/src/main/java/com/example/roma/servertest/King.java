package com.example.roma.servertest;

import android.graphics.Color;
import android.graphics.Point;
import java.util.ArrayList;

public class King extends Piece {

    //public boolean _hasNotMovedYet = true;

	//Constructor
    public King(String name, String color, int pos) {
        super(name, color, pos);
        //boolean hasNotMovedYet = true;
        // TODO Auto-generated constructor stub
        if (color.equals("white")){
            image =R.drawable.klt60;
        }
        else
            image=R.drawable.kdt60;
    }

	//Constructor
    public King (Piece piece) {
        super(piece.getIntColor());
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
		_checksKing = piece._checksKing;
        _isFlipped = piece._isFlipped;
        name = "king";
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
		_hasNotMovedYet = false;
    }


    @Override
    public ArrayList<Integer> getLegalMoves(Piece[] pieces) {
		int opponentColor = (getIntColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
		ArrayList<Piece> castlingMoves = castlingMoves(pieces);
        ArrayList<Piece> possibleMoves = possibleMoves(toDoubleArray(pieces));
		possibleMoves.addAll(castlingMoves);
		for (Piece p : possibleMoves)//allow the king to move only where it`s safe
		{
			if (p.isThreatened(pieces, opponentColor).size() == 0)
				legalMoves.add(p.getPosition());
		}
		return legalMoves;
    }

	//overrides the Piece`s canMove method
    public boolean canMove(Piece[] pieces) {
		int opponentColor = (getIntColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;
		ArrayList<Piece> kingPossibleMoves = possibleMoves(toDoubleArray(pieces));
		for (Piece p : kingPossibleMoves)
		{
			if (p.isThreatened(pieces, opponentColor).size() != 0)//someone threatens this tile
			{
				continue;
			}
			return true;
		}
        return false;
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

	public boolean hasNotMovedYet(){return _hasNotMovedYet;}

	private ArrayList<Piece> castlingMoves(Piece[] pieces)
	{
		ArrayList<Piece> result = new ArrayList<Piece>();
		Piece[][] doubleArray = toDoubleArray(pieces);
		if (hasNotMovedYet())
		{
			//the king is white
			if (getIntColor() == Color.WHITE)
			{
				if (isThreatened(pieces, Color.BLACK).size() == 0)//if the king is not threatened
				{
					//castling right
					if (doubleArray[0][7].hasNotMovedYet())//if rook has not moved yet
					{
						//the tiles between the king and the rook are empty and not threatened
						if (doubleArray[0][5] instanceof Empty && doubleArray[0][5].isThreatened(pieces, Color.BLACK).size() == 0)
						{
							if (doubleArray[0][6] instanceof Empty && doubleArray[0][6].isThreatened(pieces, Color.BLACK).size() == 0)
							{
								result.add(doubleArray[0][6]);
							}
						}
					}
					//castling left
					if (doubleArray[0][0].hasNotMovedYet())
					{
						//the tiles between the king and the rook are empty and not threatened
						if (doubleArray[0][1] instanceof Empty && doubleArray[0][1].isThreatened(pieces, Color.BLACK).size() == 0)
						{
							if (doubleArray[0][2] instanceof Empty && doubleArray[0][2].isThreatened(pieces, Color.BLACK).size() == 0)
							{
								if (doubleArray[0][3] instanceof Empty && doubleArray[0][3].isThreatened(pieces, Color.BLACK).size() == 0)
								{
									result.add(doubleArray[0][2]);
								}
							}
						}
					}
				}
			}
			else//the king is black
			{
				if (isThreatened(pieces, Color.WHITE).size() == 0)//if the king is not threatened
				{
					//castling right
					if (doubleArray[7][7].hasNotMovedYet())//if rook has not moved yet
					{
						//the tiles between the king and the rook are empty and not threatened
						if (doubleArray[7][5] instanceof Empty && doubleArray[7][5].isThreatened(pieces, Color.WHITE).size() == 0)
						{
							if (doubleArray[7][6] instanceof Empty && doubleArray[7][6].isThreatened(pieces, Color.WHITE).size() == 0)
							{
								result.add(doubleArray[7][6]);
							}
						}
					}
					//castling left
					if (doubleArray[7][0].hasNotMovedYet())
					{
						//the tiles between the king and the rook are empty and not threatened
						if (doubleArray[7][1] instanceof Empty && doubleArray[7][1].isThreatened(pieces, Color.WHITE).size() == 0)
						{
							if (doubleArray[7][2] instanceof Empty && doubleArray[7][2].isThreatened(pieces, Color.WHITE).size() == 0)
							{
								if (doubleArray[7][3] instanceof Empty && doubleArray[7][3].isThreatened(pieces, Color.WHITE).size() == 0)
								{
									result.add(doubleArray[7][2]);
								}
							}
						}
					}
				}
			}
		}
		return result;
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
        //int opposingColor = (getIntColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;

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

