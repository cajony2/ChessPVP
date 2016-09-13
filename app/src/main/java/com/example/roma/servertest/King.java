package com.example.roma.servertest;

import android.content.Context;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public class King extends Piece {

    boolean hasNotMovedYet;

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
        _checksKing = piece.checks();
        _isFlipped = piece._isFlipped;
        name = piece.getName();
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
    }

    @Override
    ArrayList<Integer> getLegalMoves(Game game) {
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        Piece[] pieces = game.getBoard2();

        //castling option should be added

        if (!(canMove(game)))
        {
            return legalMoves;//if the king can`t move it`ll return empty List
        }
        int temp ;

        // up
        temp =position-8;
        if (temp>=0 && (pieces[temp].isEmpty() || !pieces[temp].getColor().equals(pieces[position].getColor())) )
            legalMoves.add(temp);

        // up left
        temp =position-9;
        if (temp>=0 && ( temp%8 != 7 ) && (pieces[temp].isEmpty() || !pieces[temp].getColor().equals(pieces[position].getColor())) )
            legalMoves.add(temp);

        // up right
        temp =position-7;
        if (temp>=0 && ( temp%8 != 0 ) && ((pieces[temp].isEmpty()) || !pieces[temp].getColor().equals(pieces[position].getColor())) )
            legalMoves.add(temp);

        //  right
        temp =position+1;
        if (temp>=0 && ( temp%8 != 0 ) && ((pieces[temp].isEmpty()) || !pieces[temp].getColor().equals(pieces[position].getColor())) )
            legalMoves.add(temp);

        // left
        temp =position-1;
        if (temp>=0 && ( temp%8 != 7 ) && ((pieces[temp].isEmpty()) || !pieces[temp].getColor().equals(pieces[position].getColor())) )
            legalMoves.add(temp);

        // down left
        temp =position+7;
        if (temp<64 && ( temp%8 != 7 ) && ((pieces[temp].isEmpty()) || !pieces[temp].getColor().equals(pieces[position].getColor())) )
            legalMoves.add(temp);

        // left
        temp =position+9;
        if (temp<64 && ( temp%8 != 0 ) && ((pieces[temp].isEmpty()) || !pieces[temp].getColor().equals(pieces[position].getColor())) )
            legalMoves.add(temp);

        // left
        temp =position+8;
        if (temp<64 && (temp%8 != 7) && ((pieces[temp].isEmpty()) || !pieces[temp].getColor().equals(pieces[position].getColor())) )
            legalMoves.add(temp);


        return legalMoves;
    }

    @Override
    protected boolean canMove(Game game)
    {
        Piece[] pieces = game.getBoard2();

        //extracting only opponent pieces
        ArrayList<Piece> opponentPieces = new ArrayList<Piece>();
        for (Piece p : pieces)
        {
            if (p.getIntColor() != getIntColor() && !(p instanceof Empty))// add piece if not empty and with opponent color
                opponentPieces.add(p);
        }

        boolean canMove;
        ArrayList<Integer> kingPossibleMoves = getLegalMoves(game);
        for (Integer i : kingPossibleMoves)
        {
            if (kingCanMoveThere(game, opponentPieces, i))
            {
                return true;
            }
            /*canMove = true;
            for (Piece p : opponentPieces)
            {
                if (!(p.getLegalMoves(game).contains(pieces[i])))
                {
                    continue;
                }
                else
                {
                    canMove = false;
                    break;
                }
            }
            if (canMove)
            {
                return true;
            }*/
        }
        return false;
    }

    private boolean kingCanMoveThere(Game game, ArrayList<Piece> opponentPieces, int tileLocation) {
        Piece[] pieces = game.getBoard2();

        for (Piece p : opponentPieces)
        {
            if (p.getLegalMoves(game).contains(pieces[tileLocation]))
            {
                return false;
            }
        }
        return true;
    }


    /*ArrayList<Piece> possibleMoves(Game game) {
        return null;
    }*/


}

