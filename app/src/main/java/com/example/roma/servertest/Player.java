package com.example.roma.servertest;

import android.graphics.Color;

/**
 * Created by Jony on 31/08/2016.
 */
public abstract class Player {
	protected String _name;
	protected int _color;
	//protected int _score;
	//protected bool _turn;

	//Constructor
	public Player(String name)
	{
		_name = name;
	}

	public String getName()
	{
		return _name;
	}

	public int getColor()
	{
		return _color;
	}

	public static class WhitePlayer extends Player{

		//Constructor
		public WhitePlayer(String name)
		{
			super(name);
			_color = Color.WHITE;
		}
	}

	public static class BlackPlayer extends Player{

		//Constructor
		public BlackPlayer(String name)
		{
			super(name);
			_color = Color.BLACK;
		}
	}
}
