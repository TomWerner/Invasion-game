package com.codeday.invasion;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Paddle extends Image
{
	public static float MAX_SPEED = 300;
	
	public Paddle(Texture paddleTexture, Vector2 position)
	{
		super(paddleTexture);
		setPosition(position.x, position.y);
		
		
	}
	
	public void move(boolean up, float delta, float screenHeight)
	{
		if (up)
		{
			if (getY() + MAX_SPEED * delta + getHeight() < screenHeight)
				setY(getY() + MAX_SPEED * delta);
		}
		else
		{
			if (getY() - MAX_SPEED * delta > 0)
				setY(getY() - MAX_SPEED * delta);
		}
	}
	
	public void increaseSpeed()
	{
		MAX_SPEED *= 1.2f;
	}
}
