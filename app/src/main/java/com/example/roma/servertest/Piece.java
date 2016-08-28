package com.example.roma.servertest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Roma on 8/2/2016.
 */

public abstract class Piece extends View{//Jony added the View extension

    protected int _color;//added by jony
    protected Tile _tile;//added by jony
    //protected Point _pointPosition;//added by jony
    protected boolean _isActive;//added by jony
    protected String name;
    protected String color;
    protected int image;
    protected int position;
    protected boolean isEmpty;

    //default constructor added by jony
    public Piece(Context context, int color, Tile tile) {
        super(context);
        _color = color;
        _tile = tile;
        _isActive = true;

    }

    //jony added the context for the view class
    public Piece (Context context, String name , String color, int pos){
        super(context);//added by jony

        this.name = name;
        this.color = color;
        this.position=pos;
        isEmpty = name.equals("empty") ? true : false ;
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

    public String getColor(){
        return color;
    }

    public int getPosition(){
        return position;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    public void setPosition(int pos){
        position=pos;
    }

    // each peace should override this method and return  the legal moves from the piece location according to the games piece layout
    abstract  ArrayList<Integer> getLegalMoves(Game game);

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("name", name);
        json.put("color", color);
        json.put("image", image);
        json.put("position", position);
        return json;
    }
}
