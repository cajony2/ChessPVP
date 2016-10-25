package com.example.roma.servertest;

import java.util.ArrayList;

/**
 * Interface to comunicate between the model and view
 */
public interface Communicator {
    String getOpponentName();
    ArrayList<Piece> getEatenPieces(int color);
    String getUserName();
    void makeMove();
    Piece[][] getPieces();
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
