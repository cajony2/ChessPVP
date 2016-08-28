package com.example.roma.servertest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.widget.FrameLayout;

/**
 * Created by Jony on 28/08/2016.
 */
public class Tile extends FrameLayout {

	protected final Point _position;
	protected int _color;
	protected Piece _piece;

	//constructor
	public Tile(Context context, Point position) {
		super(context);
		_position = position;
		setClickable(true);
	}

	public Point getPosition(){
		return _position;
	}

	public Piece getPiece(){
		return _piece;
	}

	public int getColor(){
		return _color;
	}

	public void setPiece(Piece piece){
		_piece = piece;
		addView(_piece);
	}

	//inner class that represents a white tile
	public static final class WhiteTile extends Tile{
		//white tile constructor
		public WhiteTile(Context context, Point position) {
			super(context, position);
			_color = Color.WHITE;
			this.setBackgroundResource(R.drawable.white);
		}
	}

	//inner class that represents a black tile
	public static final class BlackTile extends Tile{
		//black tile constructor
		public BlackTile(Context context, Point position){
			super(context, position);
			_color = Color.BLACK;
			this.setBackgroundResource(R.drawable.black);

		}
	}
}
