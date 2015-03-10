package com.codeday.invasion;

public class Point
{
	private float x;
	private float y;

	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Point(double x, double y)
	{
		this.x = (float)x;
		this.y = (float)y;
	}

	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}

	public float distance(Point a1)
	{
		return (float) Math.sqrt(Math.pow(a1.x - x, 2) + Math.pow(a1.y - y , 2));
	}
}

