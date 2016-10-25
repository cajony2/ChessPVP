package com.example.roma.servertest;

import android.graphics.Color;

/**
 * This class represents a player in the game
 */
public abstract class Player {
	protected String _name;
	protected int _color;

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
