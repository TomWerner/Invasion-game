package com.codeday.invasion;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.codeday.invasion.SoundManager.SoundEffect;

public class Player extends WrapAroundImage
{
	private Drawable left;
	private Drawable middle;
	private Drawable right;

	public static float MAX_VELOCITY = 200;
	private long lastTime;
	private long lastBackTime;
	private long lastTriTime;
	private long shootDelay = 200;
	private Laser laser;
	private float playerAngle;
	private ArrayList<PowerUp> powerUpList = new ArrayList<PowerUp>();
	private Image shield;
	

	public static boolean backShoot = false;
	public static boolean rapidShoot = false;
	public static boolean sideShot = false;
	public static int health = 3;

	public Player(TextureRegion left, TextureRegion middle,
			TextureRegion right, World world, boolean isOffscreen)
	{
		super(middle, world, isOffscreen);
		shield = new Image(world.getAtlas().findRegion("shield"));
		shield.setSize(getWidth(), getHeight() * 3.0f/2.0f);
		shield.setOrigin(shield.getWidth() / 2, shield.getHeight() / 2);
		world.addActor(shield);

		backShoot = false;
		sideShot = false;
		rapidShoot = false;

		this.left = new TextureRegionDrawable(left);
		this.middle = new TextureRegionDrawable(middle);
		this.right = new TextureRegionDrawable(right);

		setOrigin(getWidth() / 2, getHeight() / 2);
		playerAngle = 90;
		velocity = new Vector2(0, 0);

		addCollisionPoint(new Vector2(0, 0), getWidth() / 2);
	}

	private Player(Drawable drawable, World world, boolean isOffscreen)
	{
		super(drawable, world, isOffscreen);
		addCollisionPoint(new Vector2(0, 0), getHeight() * 3.0f / 9.0f);

		setOrigin(getWidth() / 2, getHeight() / 2);
		playerAngle = 90;
		velocity = new Vector2(0, 0);

		shield = new Image(world.getAtlas().findRegion("shield"));
		shield.setSize(getWidth() * 3.0f/2.0f, getHeight() * 3.0f/2.0f);
		shield.setOrigin(shield.getWidth() / 2, shield.getHeight() / 2);
		world.addActor(shield);
	}

	public void update(float delta, World world, float rotate, float thrust,
			ArrayList<Asteroid> asteroids, ArrayList<PowerUp> powerups,
			ArrayList<Laser> enemyLasers)
	{
		super.update(delta);

		if (rotate == 0)
			setDrawable(middle);
		else if (rotate < 0)
			setDrawable(right);
		else
			setDrawable(left);

		rotate(rotate * delta);
		playerAngle += rotate * delta;

		float dxA = (float) (Math.cos(Math.toRadians(playerAngle)) * thrust);
		float dyA = (float) (Math.sin(Math.toRadians(playerAngle)) * thrust);
		velocity.x += dxA;
		velocity.y += dyA;
		velocity.x = Math.min(velocity.x, MAX_VELOCITY);
		velocity.y = Math.min(velocity.y, MAX_VELOCITY);

		setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);
		
		if (isOffscreen)
			return;

		if (checkCollisionAsteroids(asteroids))
			world.playerDeath(getX() + getWidth() / 2, getY() + getHeight() / 2);
		if (checkCollisionPowerUps(powerups))
		{
		}
		if (checkCollisionLasers(enemyLasers))
		{
			health--;
			
			if (health <= 0)
			{
				world.playerDeath(getX(), getY());
			}
		}

		for (int i = 0; i < powerUpList.size();)
		{
			if (powerUpList.get(i).isFinished())
			{
				powerUpList.remove(i).deactivate();
			}
			else
				i++;
		}

	}

	private boolean checkCollisionLasers(ArrayList<Laser> enemyLasers)
	{
		boolean collision = false;

		if (checkLaserCollision(enemyLasers))
		{
			collision = true;
		}
		for (WrapAroundImage a : offscreens)
		{
			((Player) a).checkLaserCollision(enemyLasers);
		}
		return collision;
	}

	public boolean checkLaserCollision(ArrayList<Laser> lasers)
	{
		for (int i = 0; i < lasers.size();)
		{
			Laser laser = lasers.get(i);

			Point laserPoint = new Point(laser.getX() + laser.getOriginX(),
					laser.getY() + laser.getOriginX());
			if (collides(laserPoint))
			{
				laser.remove();
				lasers.remove(i);

				return true;
			}
			else
				i++;
		}
		return false;
	}

	private boolean checkCollisionAsteroids(ArrayList<Asteroid> images)
	{
		boolean collided = false;

		for (Asteroid wai : images)
		{
			// Check real one
			for (int playerI = 0; playerI < keyPoints.size(); playerI++)
			{
				for (int waiI = 0; waiI < wai.getKeyPoints().size(); waiI++)
				{
					Point p1 = getUpdatedKeyPoint(playerI);
					Point a1 = wai.getUpdatedKeyPoint(waiI);
					if (p1.distance(a1) < radia.get(playerI)
							+ wai.getRadia().get(waiI))
						collided = true;
				}
			}
			// Check the asteroid clones
			for (WrapAroundImage aClone : wai.getOffscreens())
			{
				// Check real one
				for (int playerI = 0; playerI < keyPoints.size(); playerI++)
				{
					for (int astI = 0; astI < aClone.getKeyPoints().size(); astI++)
					{
						Point p1 = getUpdatedKeyPoint(playerI);
						Point a1 = aClone.getUpdatedKeyPoint(astI);
						if (p1.distance(a1) < radia.get(playerI)
								+ aClone.getRadia().get(astI))
							collided = true;
					}
				}
			}
		}
		for (WrapAroundImage pClone : offscreens)
		{
			for (WrapAroundImage asteroid : images)
			{
				// Check real one
				for (int playerI = 0; playerI < pClone.keyPoints.size(); playerI++)
				{
					for (int astI = 0; astI < asteroid.getKeyPoints().size(); astI++)
					{
						Point p1 = pClone.getUpdatedKeyPoint(playerI);
						Point a1 = asteroid.getUpdatedKeyPoint(astI);
						if (p1.distance(a1) < pClone.radia.get(playerI)
								+ asteroid.getRadia().get(astI))
							collided = true;
					}
				}
				// Check the asteroid clones
				for (WrapAroundImage aClone : asteroid.getOffscreens())
				{
					// Check real one
					for (int playerI = 0; playerI < pClone.keyPoints.size(); playerI++)
					{
						for (int astI = 0; astI < aClone.getKeyPoints().size(); astI++)
						{
							Point p1 = pClone.getUpdatedKeyPoint(playerI);
							Point a1 = aClone.getUpdatedKeyPoint(astI);
							if (p1.distance(a1) < pClone.radia.get(playerI)
									+ aClone.getRadia().get(astI))
							{
								collided = true;
							}
						}
					}
				}
			}
		}
		return collided;
	}

	private boolean checkCollisionPowerUps(ArrayList<PowerUp> images)
	{
		boolean collided = false;

		for (PowerUp powerup : images)
		{
			// Check real one
			for (int playerI = 0; playerI < keyPoints.size(); playerI++)
			{
				for (int waiI = 0; waiI < powerup.getKeyPoints().size(); waiI++)
				{
					Point p1 = getUpdatedKeyPoint(playerI);
					Point a1 = powerup.getUpdatedKeyPoint(waiI);
					if (p1.distance(a1) < radia.get(playerI)
							+ powerup.getRadia().get(waiI))
					{
						((PowerUp) powerup).setCollected();
						collided = true;
					}
				}
			}
			// Check the asteroid clones
			for (WrapAroundImage aClone : powerup.getOffscreens())
			{
				// Check real one
				for (int playerI = 0; playerI < keyPoints.size(); playerI++)
				{
					for (int astI = 0; astI < aClone.getKeyPoints().size(); astI++)
					{
						Point p1 = getUpdatedKeyPoint(playerI);
						Point a1 = aClone.getUpdatedKeyPoint(astI);
						if (p1.distance(a1) < radia.get(playerI)
								+ aClone.getRadia().get(astI))
						{
							collided = true;
							((PowerUp) powerup).setCollected();
						}
					}
				}
			}
		}
		for (WrapAroundImage pClone : offscreens)
		{
			for (PowerUp powerup : images)
			{
				// Check real one
				for (int playerI = 0; playerI < pClone.keyPoints.size(); playerI++)
				{
					for (int astI = 0; astI < powerup.getKeyPoints().size(); astI++)
					{
						Point p1 = pClone.getUpdatedKeyPoint(playerI);
						Point a1 = powerup.getUpdatedKeyPoint(astI);
						if (p1.distance(a1) < pClone.radia.get(playerI)
								+ powerup.getRadia().get(astI))
						{
							collided = true;
							((PowerUp) powerup).setCollected();
						}
					}
				}
				// Check the asteroid clones
				for (WrapAroundImage aClone : powerup.getOffscreens())
				{
					// Check real one
					for (int playerI = 0; playerI < pClone.keyPoints.size(); playerI++)
					{
						for (int astI = 0; astI < aClone.getKeyPoints().size(); astI++)
						{
							Point p1 = pClone.getUpdatedKeyPoint(playerI);
							Point a1 = aClone.getUpdatedKeyPoint(astI);
							if (p1.distance(a1) < pClone.radia.get(playerI)
									+ aClone.getRadia().get(astI))
							{
								collided = true;
								((PowerUp) powerup).setCollected();
							}
						}
					}
				}
			}
		}
		return collided;
	}

	public float getplayerAngle()
	{
		return playerAngle;
	}

	public void shoot(ArrayList<Laser> lasers)
	{
		if (this.isImageOffscreen())
			return;
		
		if (canShoot())
		{
			laser = new Laser(world.getAtlas().findRegion("laserRed"),
					playerAngle);

			laser.setPosition(getX() + getOriginX() - laser.getWidth() / 2
					+ (float) Math.cos(Math.toRadians(playerAngle))
					* getHeight() / 2,
					getY() + getOriginY() - laser.getHeight() / 2
							+ (float) Math.sin(Math.toRadians(playerAngle))
							* getHeight() / 2);
			world.addActor(laser);
			lasers.add(laser);
		
			if (!isOffscreen)
				world.getGame().getSoundManager().play(SoundEffect.LASER);
		}
		if (backShoot)
		{
			if (canBackShoot())
			{
				laser = new Laser(world.getAtlas().findRegion("laserRed"),
						playerAngle - 180);

				laser.setPosition(getX() + getOriginX() - laser.getWidth() / 2
						- (float) Math.cos(Math.toRadians(playerAngle))
						* getHeight() / 2,
						getY() + getOriginY() - laser.getHeight() / 2
								- (float) Math.sin(Math.toRadians(playerAngle))
								* getHeight() / 2);
				world.addActor(laser);
				lasers.add(laser);
			}
		}
		if (rapidShoot)
		{
			laser = new Laser(world.getAtlas().findRegion("laserRed"),
					playerAngle);

			laser.setPosition(getX() + getOriginX() - laser.getWidth() / 2
					+ (float) Math.cos(Math.toRadians(playerAngle))
					* getHeight() / 2,
					getY() + getOriginY() - laser.getHeight() / 2
							+ (float) Math.sin(Math.toRadians(playerAngle))
							* getHeight() / 2);
			world.addActor(laser);
			lasers.add(laser);
		}
		if (sideShot)
		{
			if (canSideShot())
			{
				Laser laser1 = new Laser(world.getAtlas()
						.findRegion("laserRed"), playerAngle + 90);
				Laser laser2 = new Laser(world.getAtlas()
						.findRegion("laserRed"), playerAngle - 90);

				laser1.setPosition(
						getX()
								+ getOriginX()
								- laser.getWidth()
								/ 2
								+ (float) Math.cos(Math
										.toRadians(playerAngle + 90))
								* getHeight() / 2,
						getY()
								+ getOriginY()
								- laser.getHeight()
								/ 2
								+ (float) Math.sin(Math
										.toRadians(playerAngle + 90))
								* getHeight() / 2);
				world.addActor(laser1);
				lasers.add(laser1);

				laser2.setPosition(
						getX()
								+ getOriginX()
								- laser.getWidth()
								/ 2
								+ (float) Math.cos(Math
										.toRadians(playerAngle - 90))
								* getHeight() / 2,
						getY()
								+ getOriginY()
								- laser.getHeight()
								/ 2
								+ (float) Math.sin(Math
										.toRadians(playerAngle - 90))
								* getHeight() / 2);
				lasers.add(laser2);
				world.addActor(laser2);
			}
		}
		
//		for (WrapAroundImage wai : offscreens)
//			((Player) wai).shoot(lasers);
	}

	@Override
	public boolean collides(Point point)
	{
		for (int i = 0; i < keyPoints.size(); i++)
		{
			Point p1 = getUpdatedKeyPoint(i);

			if (p1.distance(point) < getImageWidth() / 2)
				return true;
		}
		return false;
	}

	@Override
	protected WrapAroundImage getNewObject()
	{
		Player p = new Player(getDrawable(), world, true);
		p.setFrames(left, middle, right);
		return p;
	}

	private void setFrames(Drawable left2, Drawable middle2, Drawable right2)
	{
		this.left = left2;
		this.middle = middle2;
		this.right = right2;
	}
	
	public boolean remove()
	{
		shield.remove();
		return super.remove();
	}

	private boolean canBackShoot()
	{
		if (System.currentTimeMillis() - lastBackTime > shootDelay)
		{
			lastBackTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	private boolean canSideShot()
	{
		if (System.currentTimeMillis() - lastTriTime > shootDelay)
		{
			lastTriTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	private boolean canShoot()
	{
		if (System.currentTimeMillis() - lastTime > shootDelay)
		{
			lastTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	public void addPowerUp(PowerUp powerUp)
	{
		powerUp.activate(this);
		powerUpList.add(powerUp);

	}

	public void addHealth()
	{
		health++;
	}

	
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		
		if (health >= 3)
		{
			shield.setPosition(getX() + getWidth() / 2 - shield.getWidth() / 2,
							   getY() + getHeight() / 2 - shield.getHeight() / 2);
			shield.setRotation(angle);
			shield.setVisible(true);
			shield.draw(batch, parentAlpha);
		}
		else
			shield.setVisible(false);
	}
}
