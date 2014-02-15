package com.codeday.invasion;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Ball extends Image
{
	private Vector2 velocity;
	public static final float INITIAL_SPEED = 300;

	public Ball(Texture texture)
	{
		super(texture);
		
		randomizeDirection();
	}

	public void update(float delta, World world,
			Paddle p1, Paddle p2)
	{
		float screenWidth = world.getWidth();
		float screenHeight = world.getHeight();
		float xPotential = getX() + velocity.x * delta;
		float yPotential = getY() + velocity.y * delta;
		
		//Check for paddle collisionX
		if ((xPotential < p1.getX() + p1.getWidth() && xPotential > p1.getX() || //From right
			 xPotential + getWidth() < p1.getX() + p1.getWidth() && xPotential + getWidth() > p1.getX()) //From left
			 &&
			yPotential + getHeight() > p1.getY() && yPotential < p1.getY() + p1.getHeight()) //In y range
		{
			velocity.x *= -1;
			velocity.scl(1.1f);
		}
		else if ((xPotential < p2.getX() + p2.getWidth() && xPotential > p2.getX() || //From right
			 xPotential + getWidth() < p2.getX() + p2.getWidth() && xPotential + getWidth() > p2.getX()) //From left
			 &&
			yPotential + getHeight() > p2.getY() && yPotential < p2.getY() + p2.getHeight()) //In y range
		{
			velocity.x *= -1;
			velocity.scl(1.1f);
		}
		else if (xPotential < 0)
		{
			world.rightPlayerScore();
		}
		else if (xPotential + getWidth() > screenWidth)
		{
			world.leftPlayerScore();
		}
		else
			setX(xPotential);
		
		if (yPotential < 0 || yPotential + getHeight() > screenHeight)
			velocity.y *= -1;
		else
			setY(yPotential);
	}
	
	public void act(float delta)
	{
		float x = this.getX();
		float y = this.getY();

		setOrigin(getWidth() / 2, getHeight() / 2);
		rotate(15);
	}

	public void randomizeDirection()
	{
		float num = 0;
		while (num < 15 && num > -15)
			num = ((float) Math.random() * 90) - 45;
		
		velocity = new Vector2((float) Math.cos(Math.toRadians(num)) * INITIAL_SPEED,
				(float) Math.sin(Math.toRadians(num)) * INITIAL_SPEED);
		boolean flip = (Math.random() < .5f);
		if (flip)
			velocity.scl(-1);
	}

	public void increaseSpeed()
	{
		velocity.scl(1.2f);
	}
}
