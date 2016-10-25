package com.example.roma.servertest;

import android.graphics.Point;
import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(String name, String color,int pos,boolean moved) {
        super(name, color, pos, moved);
        if (color.equals("white")){
            image = R.drawable.rlt60;
        }
        else
            image=R.drawable.rdt60;
    }

    public Rook (Piece piece) {
        super(piece.getIntColor());
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
        _checksKing = piece._checksKing;
        _isFlipped = piece._isFlipped;
        name = "rook";
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
        _hasNotMovedYet = piece.hasNotMovedYet();
    }

    @Override
    public ArrayList<Integer> getLegalMoves(Piece[][] pieces){

        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        ArrayList<Piece> possibleMoves = possibleMoves(pieces);
        ArrayList<Piece> opponentPieces = opponentPieces(pieces);
        for (Piece p : opponentPieces)
        {
            if (p.checks(pieces))
            {
                ArrayList<Piece> theWayToTheKing = p.wayToTheKing(pieces);
                if (canMove(pieces))//rook can move
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
                else//rook can not move
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

        //moving up
        for (int r = row+1; r < TILES_NUMBER_IN_A_ROW; r++)
        {
            if (!(pieces[r][col].getName().equals("empty")))//piece is not empty
            {
                if (!pieces[r][col].getActive())//add to possibleMoves if not active
                    result.add(pieces[r][col]);
                else
                if (pieces[r][col].getColor().equals(color))//piece is the same color as the rook
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
            else//piece is empty
            {
                result.add(pieces[r][col]);
            }
        }
        //moving down
        for (int r = row-1; r >= 0; r--)
        {
            if (!(pieces[r][col].getName().equals("empty")))//piece is not empty
            {
                if (!pieces[r][col].getActive())//add to possibleMoves if not active
                    result.add(pieces[r][col]);
                else
                if (pieces[r][col].getColor().equals(color))//piece is the same color as the rook
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
            else//piece is empty
            {
                result.add(pieces[r][col]);
            }
        }
        //moving left
        for (int c = col-1; c >= 0; c--)
        {
            if (!(pieces[row][c].getName().equals("empty")))//piece is not empty
            {
                if (!pieces[row][c].getActive())//add to possibleMoves if not active
                    result.add(pieces[row][c]);
                else
                if (pieces[row][c].getColor().equals(color))//piece is the same color as the rook
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
            else//piece is empty
            {
                result.add(pieces[row][c]);
            }
        }
        //moving right
        for (int c = col+1; c < TILES_NUMBER_IN_A_ROW; c++)
        {
            if (!(pieces[row][c].getName().equals("empty")))//piece is not empty
            {
                if (!pieces[row][c].getActive())//add to possibleMoves if not active
                    result.add(pieces[row][c]);
                else
                if (pieces[row][c].getColor().equals(color))//piece is the same color as the rook
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

        return result;
    }
}
