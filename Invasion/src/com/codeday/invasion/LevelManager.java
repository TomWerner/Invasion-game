package com.codeday.invasion;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the levels.
 */
public class LevelManager
{
	private ArrayList<Level> levels;

	/**
	 * Creates the level manager.
	 */
	public LevelManager()
	{
		
	}

	/**
	 * Retrieve all the available levels.
	 */
	public List<Level> getLevels()
	{
		return levels;
	}

	/**
	 * Retrieve the level with the given id, or <code>null</code> if no such
	 * level exist.
	 */
	public Level findLevelById(int id)
	{
		if (id < 0 || id >= levels.size())
		{
			return null;
		}
		return levels.get(id);
	}
}