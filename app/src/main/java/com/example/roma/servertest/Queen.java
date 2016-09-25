package com.example.roma.servertest;

import android.graphics.Point;
import android.util.Log;
import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(String name, String color,int pos) {
        super(name, color, pos);
        if (color.equals("white")){
            image = R.drawable.qlt60;
        }
        else
            image=R.drawable.qdt60;
    }

    public Queen (Piece piece) {
        super(piece.getIntColor());
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
        _checksKing = piece._checksKing;
        _isFlipped = piece._isFlipped;
        name = "queen";
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
        _hasNotMovedYet = piece.hasNotMovedYet();
    }


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

    public ArrayList<Piece> possibleMoves(Piece[][] pieces) {

        ArrayList<Piece> result = new ArrayList<Piece>();
        if (!getActive())
        {
            return result;
        }
        int row = _pointPosition.x;
        int col = _pointPosition.y;
        Log.i("chess","in queen possibleMoves");
        //moving downward
        for (int r = row+1; r < TILES_NUMBER_IN_A_ROW; r++)
        {
            if (!(pieces[r][col].getName().equals("empty")))//piece is not empty
            {
                if (!pieces[r][col].getActive())//add to possibleMoves if not active
                    result.add(pieces[r][col]);
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
                if (!pieces[r][col].getActive())//add to possibleMoves if not active
                    result.add(pieces[r][col]);
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
                if (!pieces[row][c].getActive())//add to possibleMoves if not active
                    result.add(pieces[row][c]);
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
                if (!pieces[row][c].getActive())//add to possibleMoves if not active
                    result.add(pieces[row][c]);
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
                if (!pieces[r][c].getActive())//add to possibleMoves if not active
                    result.add(pieces[r][c]);
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
