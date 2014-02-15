package com.codeday.invasion;

import java.util.HashMap;

public class WorldController
{
	private HashMap<Integer, Boolean> keyboard = new HashMap<Integer, Boolean>();

	public WorldController()
	{
	}

	public void setKeyDown(int keycode)
	{
		keyboard.put(keycode, true);
	}

	public void setKeyUp(int keycode)
	{
		keyboard.put(keycode, false);
	}
	
	public boolean isKeyDown(int keycode)
	{
		if (keyboard.containsKey(keycode))
			return keyboard.get(keycode);
		return false;
	}

}
