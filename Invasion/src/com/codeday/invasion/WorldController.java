package com.codeday.invasion;

import java.util.HashMap;

public class WorldController
{
	private HashMap<Integer, Boolean> keyboard = new HashMap<Integer, Boolean>();
	private int keysDown;

	public WorldController()
	{
	}

	public void setKeyDown(int keycode)
	{
		keyboard.put(keycode, true);
		keysDown++;
	}

	public void setKeyUp(int keycode)
	{
		keyboard.remove(keycode);
		keysDown--;
	}
	
	public boolean isKeyDown(int keycode)
	{
		if (keyboard.containsKey(keycode))
			return keyboard.get(keycode);
		return false;
	}

	public boolean isKeyDown()
	{
		return keysDown > 0;
	}

}
