 package com.codeday.invasion;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Asteroid extends WrapAroundImage 
{
	
	public static float INITIAL_SPEED = 25;
	public static int MAX_HEALTH = 5;
	
	protected boolean destroyed = false;
	protected int health;

	public Asteroid(TextureRegion texture, World world, boolean isOffscreen)
	{
		super(texture, world, isOffscreen);
		health = MAX_HEALTH;
		randomizeDirection();
		setOrigin(getWidth() / 2, getHeight() / 2);

		addCollisionPoint(new Vector2(0, 0), getWidth() / 2);
		
	}
	
	protected Asteroid(Drawable drawable, World world, boolean isOffscreen)
	{
		super(drawable, world, isOffscreen);
		addCollisionPoint(new Vector2(21, -6.5f), 70);
		addCollisionPoint(new Vector2(-32, 3.5f), 50);
	}

	public void update(float delta, World world, ArrayList<Laser> lasers, ArrayList<Asteroid> asteroids)
	{
		super.update(delta);
		
		if (isOffscreen)
			return;
		
		boolean collision = false;
		
		if (checkLaserCollision(lasers))
		{
			collision = true;
		}
		for (WrapAroundImage a : offscreens)
		{
			((Asteroid) a).checkLaserCollision(lasers);
		}
		
		if (collision)
		{
			health--;
			setPosition(getX(), getY());
		}	
		
		if (health <= 0)
		{
			destroyed = true;
			for (int i = 0; i < offscreens.size();)
				offscreens.remove(i).remove();
		}
		
		if (destroyed && !(this instanceof SmallAsteroid) )
		{
			SmallAsteroid small1 = new SmallAsteroid(world.getAtlas().findRegion("meteorSmall"),world,false);
			SmallAsteroid small2 = new SmallAsteroid(world.getAtlas().findRegion("meteorSmall"),world,false);

			small1.setPosition(getX() + getOriginX(), getY() + getOriginY());
			small2.setPosition(getX() + getOriginX(), getY() + getOriginY());
			
			
			asteroids.add(small1);
			world.addActor(small1);
			asteroids.add(small2);
			world.addActor(small2);
			
		}
	}
	
	public boolean checkLaserCollision(ArrayList<Laser> lasers)
	{
		for (int i = 0; i < lasers.size();)
		{
			Laser laser = lasers.get(i);
			
			Point laserPoint = new Point(laser.getX() + laser.getOriginX(), laser.getY() + laser.getOriginX());
			if (laserPoint.distance(new Point(getX() + getOriginX(), getY() + getOriginY())) < getWidth() / 2 + laser.getWidth() / 2)
			{
				float x = laserPoint.getX();
				float y = laserPoint.getY();
				
				laser.remove();
				lasers.remove(i);
				addLaserSplash(x, y);
				return true;
			}
			else
				i++;
		}
		return false;
	}
	
	private void addLaserSplash(float x, float y)
	{
		Image image = new Image(world.getAtlas().findRegion("laserRedShot"));
		image.setPosition(x - image.getWidth() / 2, y - image.getHeight() / 2);
		world.addActor(image);
		image.addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.removeActor()));
	}

	public boolean collides(Point point)
	{
		for (int i = 0; i < keyPoints.size(); i++)
		{
			Point p1 = getUpdatedKeyPoint(i);
			
			if (p1.distance(point) < radia.get(i))
				return true;
		}
		return false;
	}

	public void act(float delta)
	{
		super.act(delta);
		if (isOffscreen)
			return;
		setOrigin(getWidth() / 2, getHeight() / 2);
		float rotation = 360 * 1.0f / 20.0f * delta;
		rotate(rotation);
	}

	public void randomizeDirection()
	{
		float num = ((float) Math.random() * 360);
		while ((num > 45 && num < 135) || (num > 180 + 45 && num < 360 - 45))
			num = ((float) Math.random() * 360);
		
		velocity = new Vector2((float) Math.cos(Math.toRadians(num)) * INITIAL_SPEED,
				(float) Math.sin(Math.toRadians(num)) * INITIAL_SPEED);
	}

	public boolean isDestroyed()
	{
		if (destroyed)
			return true;
		for (WrapAroundImage a : offscreens)
			if (((Asteroid) a).isDestroyed())
				return true;
		return false;
	}
	
	protected WrapAroundImage getNewObject()
	{
		Asteroid a = new Asteroid(getDrawable(), world, true);
		a.setHealth(health);
		return a;
	}
	
	public void setHealth(int health)
	{
		this.health = health;
	}
	
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		if (isOffscreen)
			return;
		
//		for (int k = 0; k < keyPoints.size(); k++)
//		{
//			AtlasRegion ar = world.getAtlas().findRegion("ball");
//			float scale = radia.get(k) / ar.getRegionWidth();
//			Image i = new Image(ar);
//			i.setOrigin(i.getWidth() / 2, i.getHeight() / 2);
//			i.setWidth(100 * scale);
//			i.setHeight(100 * scale);
//			i.setPosition(getX() + getOriginX() - i.getWidth() / 2 + keyPoints.get(k).x, getY() + getOriginY() - i.getHeight() / 2 + keyPoints.get(k).y);
//			System.out.println((i.getX() + i.getWidth() / 2) + " , " + (i.getY() + i.getHeight() / 2));
//			world.addActor(i);
//		}
	}
}