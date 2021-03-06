package com.example.roma.servertest;

import android.graphics.Color;
import android.graphics.Point;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public abstract class Piece{

    //variables
    protected final static int TILES_NUMBER_IN_A_ROW = 8;
    protected int _color;
    protected Point _pointPosition;
    protected boolean _isActive;
    protected boolean _checksKing;
    protected boolean _isFlipped;
    protected String name;
    protected String color;
    protected int image;
    protected int position;
    protected boolean isEmpty;
    protected boolean _hasNotMovedYet;

    //Constructor
    public Piece (String name, String color, int pos, boolean moved){

        this.name = name;
        this.color = color;
        this.position = pos;
        isEmpty = name.equals("empty") ? true : false;
        _pointPosition = positionToPoint(pos);
        _isActive = true;
        _hasNotMovedYet = moved;
        if (color.equals("white"))
            _color = Color.WHITE;
        else
            _color = Color.BLACK;
    }

    //default constructor
    public Piece(int color)
    {
        _color = color;
    }

    //copy constructor
    public Piece (Piece piece){
        _color = piece.getIntColor();
        _pointPosition = new Point(piece.getPointPosition().x, piece.getPointPosition().y);
        _isActive = piece.getActive();
        _checksKing = piece._checksKing;
        _isFlipped = piece.getIsFliped();
        name = piece.getName();
        color = piece.getColor();
        image = piece.getImg();
        position = piece.getPosition();
        isEmpty = piece.isEmpty();
        _hasNotMovedYet = piece.hasNotMovedYet();
    }

    public boolean hasNotMovedYet(){return _hasNotMovedYet;}

    public void setHasNotMovedYet(boolean bool)
    {
        _hasNotMovedYet = bool;
    }

    public boolean canMove(Piece[][] pieces) {
        ArrayList<Piece> opponentPieces = opponentPieces(pieces);
        ArrayList<Piece> opponentPiecesThatDontCheckTheKing = new ArrayList<>();
        for (Piece p : opponentPieces)
        {
            if (!p.checks(pieces))
            {
                opponentPiecesThatDontCheckTheKing.add(p);
            }
        }
        //setting this piece as inactive so we could see if any of the opponent pieces check the king
        //when this piece leaves its position
        setActive(false);
        for (Piece p : opponentPiecesThatDontCheckTheKing)
        {
            if (p.checks(pieces))
            {
                setActive(true);
                return false;
            }
        }
        setActive(true);
        return true;
    }

    public boolean getIsFliped()
    {
        return _isFlipped;
    }

    public void  setIsFlipped(boolean bool)
    {
        _isFlipped = bool;
    }

    public boolean getActive()
    {
        return _isActive;
    }

    public void setActive(boolean bool)
    {
        _isActive = bool;
    }

    //true if this piece checks the king
    public boolean checks(Piece[][] pieces) {
        ArrayList<Piece> possibleMoves = possibleMoves(pieces);
        for (Piece p : possibleMoves)
        {
            if (p instanceof King && p.getIntColor() != getIntColor())
                return true;
        }
        return false;
    }

    public void setCheck(boolean bool)
    {
        _checksKing = bool;
    }

    public int getImg(){
        return image;
    }

    public void setEmpty(boolean b){
        isEmpty=b;
    }

    public String getName() {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getColor(){
        return color;
    }

    public int getIntColor()
    {
        return _color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public Point getPointPosition(){
        return _pointPosition;
    }

    public int getPosition(){
        return position;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    public void setPointPosition(int x, int y)
    {
        _pointPosition.set(x, y);
    }

    public void setPointPosition(Point point) {
        _pointPosition.x = point.x;
        _pointPosition.y = point.y;
    }

    public void setPosition(int pos){
        position=pos;
    }

    //each peace should override this method and return the legal moves from the piece location according to the games piece layout
    public abstract ArrayList<Integer> getLegalMoves(Piece[][] pieces);

    public abstract ArrayList<Piece> possibleMoves(Piece[][] pieces);

    //returns the pieces on board with the same color
    protected ArrayList<Piece> getPiecesByColor(Piece[][] pieces, int color) {
        ArrayList<Piece> resultPieces = new ArrayList<Piece>();

        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                if (pieces[row][col].getIntColor() == color && !(pieces[row][col] instanceof Empty))// add piece if not empty and with specific color
                    resultPieces.add(pieces[row][col]);
            }
        }
        /*for (Piece p : pieces)
        {
            if (p.getIntColor() == color && !(p instanceof Empty))// add piece if not empty and with specific color
                resultPieces.add(p);
        }*/
        return resultPieces;
    }

    //returns all of the opponent pieces of this current piece
    protected ArrayList<Piece> opponentPieces(Piece[][] pieces) {
        //getting the opposed color of this piece
        int opposingColor = (getIntColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;

        //returns all the pieces on board that belongs to the opponent
        return getPiecesByColor(pieces, opposingColor);
    }

    protected Piece getOpponentKing(Piece[][] pieces) {
        ArrayList<Piece> opponentPieces = new ArrayList<>();
        //getting the opposed color of this piece
        int opposingColor = (getIntColor() == Color.WHITE) ? Color.BLACK : Color.WHITE;

        //returns all the pieces on board that belongs to the opponent
        opponentPieces =  getPiecesByColor(pieces, opposingColor);
        for (Piece p : opponentPieces)
        {
            if (p instanceof King)
            {
                return p;
            }
        }
        return null;
    }

    //returns list of pieces that threaten this piece. returns empty list if no one threaten this piece
    public ArrayList<Piece> isThreatened(Piece[][] pieces, int threateningColor) {
        ArrayList<Piece> result = new ArrayList<>();
        ArrayList<Piece> opponentPieces = getPiecesByColor(pieces, threateningColor);

        /*//remove the king
        int counter = 0;
        for (int i = 0; i < opponentPieces.size(); i++)
        {
            if (opponentPieces.get(i) instanceof King)
                counter = i;
        }
        opponentPieces.remove(counter);*/

        for (Piece opponentPiece : opponentPieces)//looping through opponent pieces
        {
            ArrayList<Piece> tileArray;
            if (opponentPiece instanceof Pawn)
            {
                Pawn pawnPiece = (Pawn)opponentPiece;
                tileArray = pawnPiece.possibleEatingMoves(pieces);
            }
            else
            {
                tileArray = opponentPiece.possibleMoves(pieces);
            }
            for (Piece threatenedTile : tileArray)
            {
                //if a pawn is moving forward it`s not really threatening the tile
                /*if (opponentPiece instanceof Pawn && opponentPiece.getPointPosition().y == threatenedTile.getPointPosition().y)
                    continue;*/
                if (threatenedTile.equals(this))
                {
                    result.add(opponentPiece);
                    break;
                }
            }
        }
        return result;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("name", name);
        json.put("color", color);
        json.put("image", image);
        json.put("position", position);
        json.put("hasnotmovedyet",_hasNotMovedYet);
        return json;
    }

    //when a piece checks the king, this method returns the tiles on the way to the king
    protected ArrayList<Piece> wayToTheKing(Piece[][] pieces) {
        //Piece[][] doublePieceArray = toDoubleArray(pieces);
        ArrayList<Piece> result = new ArrayList<>();
        if (this.checks(pieces))
        {
            result.add(this);
            if (this instanceof Knight || this instanceof Pawn)
            {
                return result;
            }
            else
            {
                Point kingPointPosition = getOpponentKing(pieces).getPointPosition();
                Point currentPiecePosition = getPointPosition();
                ArrayList<Point> wayToTheKingAsPoints = pointsBetween(kingPointPosition, currentPiecePosition);
                for (Point p : wayToTheKingAsPoints)
                {
                    result.add(pieces[p.x][p.y]);
                }
                return result;
            }
        }
        else
        {
            return result;
        }
    }

    /*in a grid of points, this method returns an array of points that go from point1
    to point2, we assume that the way between the points is a straight line.
    for example if point1=(3,1) and point2=(7,5) the result is {(4,2),(5,3),(6,4)}*/
    protected ArrayList<Point> pointsBetween(Point point1, Point point2)
    {
        ArrayList<Point> result = new ArrayList<Point>();
        if (point1.x == point2.x && point1.y != point2.y)//horizontal
        {
            int origin = Math.min(point1.y, point2.y);
            int end = Math.max(point1.y, point2.y);
            for (int i = origin+1; i < end; i++)
            {
                result.add(new Point(point1.x, i));
            }
        }
        else if (point1.y == point2.y && point1.x != point2.x)//vertical
        {
            int origin = Math.min(point1.x, point2.x);
            int end = Math.max(point1.x, point2.x);
            for (int i = origin+1; i < end; i++)
            {
                result.add(new Point(i, point1.y));
            }
        }
        else if (point1.x != point2.x && point1.y != point2.y)//diagonal
        {
            int counter = 1;
            if (point1.x < point2.x && point1.y < point2.y)
            {
                for (int x = point1.x+1; x < point2.x; x++)
                {
                    result.add(new Point(x, point1.y+counter));
                    counter++;
                }
            }
            else if (point1.x < point2.x && point1.y > point2.y)
            {
                for (int x = point1.x+1; x < point2.x; x++)
                {
                    result.add(new Point(x, point1.y-counter));
                    counter++;
                }
            }
            else if (point1.x > point2.x && point1.y < point2.y)
            {
                for (int x = point1.x-1; x > point2.x; x--)
                {
                    result.add(new Point(x, point1.y+counter));
                    counter++;
                }
            }
            else//point1.x > point2.x && point1.y > point2.y
            {
                for (int x = point1.x-1; x > point2.x; x--)
                {
                    result.add(new Point(x, point1.y-counter));
                    counter++;
                }
            }
        }//end of diagonal
        return result;
    }

    private Point positionToPoint(int pos)
    {
        Point resPoint = null;
        int counter = 0;
        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                if (counter == pos)
                {
                    resPoint = new Point(row, col);
                }
                counter++;
            }
        }
        return resPoint;
    }
}
