package com.example.roma.servertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

/**
 * This adapter handles the view of the grid board
 *
 */
public class Adapter extends BaseAdapter {

    private final int TILES_NUMBER_IN_A_ROW = 8;
    Context c;
    ArrayList<Boolean> isSelected;
    boolean[] possibleMove;
    int selectedTile;
    Piece[][] _pieces;

    public Adapter(Context c , Piece[][] pieces){
        this.c=c;
        _pieces = pieces;
        isSelected = new ArrayList<>();
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
        if (position >= 0 && position < 64)
        {
            int counter = 0;
            for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
            {
                for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
                {
                    if (position == counter)
                    {
                        return _pieces[row][col];
                    }
                    counter++;
                }
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Piece piece = null;

        int counter = 0;
        for (int row = 0; row < TILES_NUMBER_IN_A_ROW; row++)
        {
            for (int col = 0; col < TILES_NUMBER_IN_A_ROW; col++)
            {
                if (position == counter)
                {
                    piece = _pieces[row][col];
                }
                counter++;
            }
        }

        if(convertView == null){
            convertView=inflate.inflate(R.layout.singlesquare, parent, false);
        }

        //ImageView isSelected = (ImageView) convertView.findViewById(R.id.selected);
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
                tileColor.setImageResource(black);
            } else {
                tileColor.setImageResource(white);
            }
        } else if (position % 2 == 0) {
            tileColor.setImageResource(white);
        } else {
            tileColor.setImageResource(black);
        }

        //TODO:  selected tiles get info from some global list object

        if(!piece.isEmpty){

            pieceImage.setImageResource(resID);
            if (piece._isFlipped)
                pieceImage.setRotationX(180);
        }
        return convertView;
    }

    public void setPossibleMoves(boolean[] possibleMoves){
        possibleMove=possibleMoves;
    }

    public void setPieces(Piece[][] pieces){
        _pieces = pieces;
    }

}
