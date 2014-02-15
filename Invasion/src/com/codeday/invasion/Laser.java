package com.codeday.invasion;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Laser extends Image 
{
	private Vector2 velocity;
	public static final float INITIAL_SPEED = 300;
	
	
	public Laser(TextureRegion texture, float direction)
	{
		
		super(texture);
		float direct = direction;
		velocity = new Vector2((float) Math.cos(Math.toRadians(direct)) * INITIAL_SPEED,
				(float) Math.sin(Math.toRadians(direction)) * INITIAL_SPEED);
		setOrigin(getWidth() / 2, getHeight() / 2);
		rotate(90);
		rotate(direction);
		}
	public void update(float delta, World world)
	{
		
		float xPotential = getX() + velocity.x * delta;
		float yPotential = getY() + velocity.y * delta;
		setX(xPotential);
		setY(yPotential);
	}
	
}
