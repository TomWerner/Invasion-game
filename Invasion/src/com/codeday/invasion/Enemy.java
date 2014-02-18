package com.codeday.invasion;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Enemy extends Asteroid
{
	public static float INITIAL_SPEED = 100;
	public static long SHOOT_DELAY = 500;
	
	protected boolean destroyed = false;
	protected int health;
	private long lastTime;
	private Laser laser;
	
	
	public Enemy(TextureRegion texture, World world, boolean isOffscreen)
	{
		super(texture, world, isOffscreen);
		health = 5;
		randomizeDirection();
		setOrigin(getWidth() / 2, getHeight() / 2);

		addCollisionPoint(new Vector2(0, 0), getWidth() / 2);
		
	}
	
	protected Enemy(Drawable drawable, World world, boolean isOffscreen)
	{
		super(drawable, world, isOffscreen);
		addCollisionPoint(new Vector2(0, 0), getWidth() / 2);
	}

	public void update(float delta, World world, ArrayList<Laser> lasers, ArrayList<Asteroid> asteroids, ArrayList<Enemy> enemies)
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
			for (int i = 0; i < lasers.size();)
			{
				Laser laser = lasers.get(i);
				
				Point laserPoint = new Point(laser.getX() + laser.getOriginX(), laser.getY() + laser.getOriginX());
				if (a.collides(laserPoint))
				{
					collision = true;
					float x = laser.getX();
					float y = laser.getY();
					laser.remove();
					lasers.remove(i);
					addLaserSplash(x, y);
				}
				else
					i++;
			}
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
	}
	
	public boolean checkLaserCollision(ArrayList<Laser> lasers)
	{
		for (int i = 0; i < lasers.size();)
		{
			Laser laser = lasers.get(i);
			
			Point laserPoint = new Point(laser.getX() + laser.getOriginX(), laser.getY() + laser.getOriginX());
			if (collides(laserPoint))
			{
				float x = laser.getX();
				float y = laser.getY();
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
		float rotation = 360 * 1.0f / 30.0f * delta;
		rotate(rotation);
	}

	public void randomizeDirection()
	{
		float num = ((float) Math.random() * 360);
		
		velocity = new Vector2((float) Math.cos(Math.toRadians(num)) * INITIAL_SPEED,
				(float) Math.sin(Math.toRadians(num)) * INITIAL_SPEED);
	}

	public boolean isDestroyed()
	{
		if (destroyed)
			return true;
		for (WrapAroundImage a : offscreens)
			if (((Enemy) a).isDestroyed())
				return true;
		return false;
	}
	
	protected WrapAroundImage getNewObject()
	{
		Enemy a = new Enemy(getDrawable(), world, true);
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

	
	//SHOOTING METHODS
	
	public void shoot(ArrayList<Laser> lasers, World world)
	{
		if (canShoot())
		{
			laser = new Laser(world.getAtlas().findRegion("laserGreen"), (float) (Math.random() * 360));

			laser.setPosition(getX() + getOriginX() ,getY() + getOriginY());
			world.addActor(laser);
			lasers.add(laser);
		}
	}
		
	private boolean canShoot()
	{
		if (System.currentTimeMillis() - lastTime > SHOOT_DELAY )
		{
			lastTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	
	private void addLaserSplash(float x, float y)
	{
		Image image = new Image(world.getAtlas().findRegion("laserRedShot"));
		image.setPosition(x, y);
		world.addActor(image);
		image.addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.removeActor()));
	}
}
