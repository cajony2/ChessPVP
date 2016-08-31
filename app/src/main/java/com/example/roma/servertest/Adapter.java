package com.example.roma.servertest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Image;
import android.media.ToneGenerator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Roma on 8/1/2016.
 */
public class Adapter extends BaseAdapter {

    Context c;
    JSONArray piecesJson;
    ArrayList<Boolean> isSelected;
    boolean[] possibleMove;
    int selectedTile;
    Game game;

    public Adapter(Context c , Game _game, JSONArray piecesJson){
        this.c=c;
        this.game = _game;
        this.piecesJson=piecesJson;
        isSelected = new ArrayList<Boolean>();
        possibleMove = new boolean[64];
        selectedTile=-1;
    }

    public void setSelectedTile(int pos){
        selectedTile=pos;
    }
    public int getSelectedTile(){
        return selectedTile;
    }

    @Override
    public int getCount() {
        return 64;
    }

    @Override
    public Object getItem(int position) {

        return game.getPiece(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Piece piece = game.getPiece(position);
        Piece[] allPieces = game.getBoard2();
        if (game.getTurn().equals("jonjony"))//should be replaced with Color.WHITE
        {
            piece = allPieces[63 - position];//white pieces at the bottom
        }
        else// name == roma
        {
            piece = allPieces[position];//black pieces at the bottom
        }

        if(convertView == null){
            convertView=inflate.inflate(R.layout.singlesquare, parent, false);
        }

        ImageView isSelected = (ImageView) convertView.findViewById(R.id.selected);
        ImageView tileColor = (ImageView) convertView.findViewById(R.id.blacknwhite);
        ImageView pieceImage = (ImageView) convertView.findViewById(R.id.piece);
        pieceImage.setImageDrawable(null);

            int resID = piece.getImg();

            //color black and white tiles
            int black , white;
            if(possibleMove[position]){
                black = R.drawable.blacksel;
                white = R.drawable.whitesel;
            }
            else {
                black = R.drawable.black;
                white = R.drawable.white;
            }

                if (position % 16 < 8) {
                    if (position % 2 == 0) {
                        //tileColor.setImageResource(R.drawable.black);
                        tileColor.setImageResource(black);
                    } else {
                        //tileColor.setImageResource(R.drawable.white);
                        tileColor.setImageResource(white);
                    }
                } else if (position % 2 == 0) {
                    //tileColor.setImageResource(R.drawable.white);
                    tileColor.setImageResource(white);
                } else {
                    //tileColor.setImageResource(R.drawable.black);
                    tileColor.setImageResource(black);
                }


            //TODO:  selected tiles get info from some global list object

            if(!piece.isEmpty){

                pieceImage.setImageResource(resID);
            }

        return convertView;
    }

    public void setPossibleMoves(boolean[] possibleMoves){
        possibleMove=possibleMoves;
    }

    public Game getGame(){
        return game;
    }
    public void setGame(Game _game){
        this.game = _game;
    }
}
