package com.codeday.invasion;

import com.badlogic.gdx.audio.Music;

public class BackgroundMusic
{
	private String fileName;
	private Music musicResource;
	private String name;

	public BackgroundMusic(String fileName, String name)
	{
		this.fileName = fileName;
		this.name = name;
	}

	public String getFileName()
	{
		return fileName;
	}

	public Music getMusicResource()
	{
		return musicResource;
	}

	public void setMusicResource(Music musicBeingPlayed)
	{
		this.musicResource = musicBeingPlayed;
	}
	
	public String getName()
	{
		return name;
	}
}
