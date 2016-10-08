package com.example.roma.servertest;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by Roma on 9/12/2016.
 * 
 */
public interface Communicator {
    String getOpponentName();
    ArrayList<Piece> getEatenPieces(int color);
    String getUserName();
    void makeMove();
    Piece[][] getPieces();
    //Piece[] getPiecesOld();
    void setEatenPiece(Piece p );
    void erasePieceFromEatenPieces(Piece p);
    int getColor();
    void setGame(String gameJson);
    void moveMade(String answerType , String message);
    void timerFinished();
    void setPieces(Piece[][] pieces);

    boolean canClick();

    void gameEnded();

    void setInfo(String message);

    void undo();

    boolean getWin();
}
