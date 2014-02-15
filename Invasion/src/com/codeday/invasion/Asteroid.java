package com.codeday.invasion;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Asteroid extends Image {
	
	private Vector2 velocity;
	public static final float INITIAL_SPEED = 300;

	public Asteroid(TextureRegion texture) {
		super(texture);
		
		randomizeDirection();
	}
	
	public void update(float delta, World world, ArrayList<Laser> list)
	{
		float screenWidth = world.getWidth();
		float screenHeight = world.getHeight();
		float xPotential = getX() + velocity.x * delta;
		float yPotential = getY() + velocity.y * delta;
	
		setX(xPotential);
		setY(yPotential);
		
		Iterator itr = list.iterator();
		
		/*while(itr.hasNext() ) {
			
			
			
		}*/
		
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