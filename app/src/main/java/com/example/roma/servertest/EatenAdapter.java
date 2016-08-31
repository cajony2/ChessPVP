package com.example.roma.servertest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Roma on 8/31/2016.
 * this is the adapter for the eaten pieces gridview.
 */
public class EatenAdapter extends BaseAdapter {

    private ArrayList<Piece> eatenPieces;
    private Context c;

    public EatenAdapter(Context _c,ArrayList<Piece> _eatenPieces){
        eatenPieces=_eatenPieces;
        c=_c;
    }
    @Override
    public int getCount() {
        return eatenPieces.size();
    }

    @Override
    public Object getItem(int position) {
        return eatenPieces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.i("chess","in eatenAdapter pos:"+position);
        if(convertView == null){
            convertView=inflate.inflate(R.layout.eaten_image, parent, false);
        }

        ImageView eatenPiece = (ImageView) convertView.findViewById(R.id.eatenPiece);
        eatenPiece.setImageResource(eatenPieces.get(position).getImg());

        return convertView;
    }
}
