package com.codeday.invasion;

public class Timer {
	private long length;
	private long startTime;

	public Timer(long length)
	{
		this.length = length;
		startTime = System.currentTimeMillis();
	}
	
	public boolean isReady()
	{
		if (System.currentTimeMillis() > startTime + length)
		{
			startTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}
}
