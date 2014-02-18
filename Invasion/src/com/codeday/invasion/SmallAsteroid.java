package com.codeday.invasion;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SmallAsteroid extends Asteroid {
	

	public SmallAsteroid(TextureRegion texture, World world, boolean isOffscreen) {
		super(texture, world, isOffscreen);
		
		setOrigin(getWidth() / 2, getHeight()  / 2);
		keyPoints = new ArrayList<Vector2>();
		radia = new ArrayList<Float>();
		addCollisionPoint(new Vector2(0, 0), getWidth() / 2);
		INITIAL_SPEED = 0;
		health = MAX_HEALTH / 2;
	}
	
	public void update(float delta, World world, ArrayList<Laser> lasers, ArrayList<Asteroid> asteroids)
	{
		super.update(delta, world, lasers, asteroids);
		if (destroyed)
			if (Math.random() < .2f)
				world.addPowerUp(getX() + getWidth() / 2, getY() + getHeight() / 2);
	}
}
