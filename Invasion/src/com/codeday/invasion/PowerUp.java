package com.codeday.invasion;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public abstract class PowerUp extends WrapAroundImage
{
	public static final float INITIAL_SPEED = 50;
	private boolean collected;
	protected Player player;
	protected Timer time;
	
	public PowerUp(TextureRegion texture, World world, boolean isOffscreen, Player player)
	{
		super(texture, world, isOffscreen);
		
		this.player = player;
		
		addCollisionPoint(new Vector2(0, 0), 20);
		setOrigin(getWidth() / 2, getHeight() / 2);
		
		randomizeDirection();
	}

	
	
	protected PowerUp(Drawable drawable, World world, boolean isOffscreen)
	{
		super(drawable, world, isOffscreen);
		addCollisionPoint(new Vector2(0, 0), 20);
		setOrigin(getWidth() / 2, getHeight() / 2);
	}
	
	public void update(float delta, World world)
	{
		super.update(delta);
		float xPotential = getX() + velocity.x * delta;
		float yPotential = getY() + velocity.y * delta;
		setX(xPotential);
		setY(yPotential);
	}

	
	
	public void randomizeDirection()
	{
		float num = ((float) Math.random() * 360);
		
		velocity = new Vector2((float) Math.cos(Math.toRadians(num)) * INITIAL_SPEED,
				(float) Math.sin(Math.toRadians(num)) * INITIAL_SPEED);
	}



	public abstract void deactivate();
	public abstract void activate(Player player);


	public boolean isFinished() 
	{
		return time.isReady();
	}


	@Override
	protected abstract WrapAroundImage getNewObject();

	public boolean collides(Point point)
	{
		return false;
	}

	public void setCollected()
	{
		collected = true;
	}
	
	public boolean isCollected()
	{
		return collected;
	}
}
