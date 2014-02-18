package com.codeday.invasion;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public abstract class WrapAroundImage extends Image
{
	protected Vector2 velocity;
	
	protected float angle;
	protected boolean destroyed = false;
	protected ArrayList<WrapAroundImage> offscreens = new ArrayList<WrapAroundImage>();
	protected boolean isOffscreen;
	protected World world;
	protected ArrayList<Vector2> keyPoints = new ArrayList<Vector2>();

	protected ArrayList<Float> radia = new ArrayList<Float>();

	private ShapeRenderer renderer;

	public WrapAroundImage(TextureRegion texture, World world, boolean isOffscreen)
	{
		super(texture);
		
		this.world = world;
		this.isOffscreen = isOffscreen;
		renderer = new ShapeRenderer();
	}
	
	public WrapAroundImage(Drawable drawable, World world, boolean isOffscreen)
	{
		super(drawable);
		
		this.world = world;
		this.isOffscreen = isOffscreen;
		renderer = new ShapeRenderer();
	}
	
	public abstract boolean collides(Point point);
	
	protected void addCollisionPoint(Vector2 offset, float radius)
	{
		keyPoints.add(offset);
		this.radia.add(radius);
	}
	
	public void update(float delta)
	{
		move(delta);
		
		if (!isOffscreen)
		{
			for (WrapAroundImage a : offscreens)
				a.update(delta);
			
			//check if we're totally offscreen
			if (isImageOffscreen())
			{
				//find the offscreen WrapAroundImage that is onscreen

				float x = 0;
				float y = 0;
				boolean found = false;
				for (int i = 0; i < offscreens.size() && !found; i++)
				{
					WrapAroundImage offscreen = offscreens.get(i);
					if ((offscreen.getX() > 0 && offscreen.getX() + getWidth() < world.getWidth()) &&
						(offscreen.getY() > 0 && offscreen.getY() + getHeight() < world.getHeight()))
					{
						x = offscreen.getX();
						y = offscreen.getY();
						found = true;
					}
				}
				if (found)
				{
					for (int i = 0; i < offscreens.size();)
					{
						offscreens.remove(i).remove();
					}
					setPosition(x, y);
				}
			}
			
		}
	}
	

	public boolean isImageOffscreen()
	{
		boolean offscreenX = (getX() > world.getWidth() || getX() + getWidth() < 0);
		boolean offscreenY = (getY() > world.getHeight() || getY() + getHeight() < 0);
		return offscreenX || offscreenY;
	}
		
	public void setVelocity(Vector2 newVelocity)
	{
		this.velocity = newVelocity;
	}
	
	public void setPosition(float x, float y)
	{
		
		super.setPosition(x, y);
		
		
		
		if (isOffscreen)
			return;

		float factor = 1.0f;// / 4.0f;
		float factor2 = 2.0f;// / 4.00f;
		if (offscreens.size() > 0)
		{
			if ((x < 0 && velocity.x >= 0) ||
				(x + getWidth() > world.getWidth() && velocity.x <= 0) ||
				(y < 0 && velocity.y >= 0) ||
				(y + getHeight() > world.getHeight() && velocity.y <= 0))
			{
				factor = -1;
				factor2 = -2;
			}
		}
		
		for (int i = 0; i < offscreens.size();)
			offscreens.remove(i).remove();
		
		WrapAroundImage offscreen1 = getNewObject();
		offscreen1.setAngle(angle);
		WrapAroundImage offscreen2 = getNewObject();
		offscreen2.setAngle(angle);
		WrapAroundImage offscreen3 = getNewObject();
		offscreen3.setAngle(angle);
		WrapAroundImage offscreen4 = getNewObject();
		offscreen4.setAngle(angle);
		WrapAroundImage offscreen5 = getNewObject();
		offscreen5.setAngle(angle);
		
		if (velocity.y < 0)
		{
			offscreen1.setPosition(getX(), getY() + world.getHeight() * factor);
			offscreen1.setVelocity(velocity);
			offscreen3.setPosition(getX(), getY() + world.getHeight() * factor2);
			offscreen3.setVelocity(velocity);
		}
		else
		{
			offscreen1.setPosition(getX(), getY() - world.getHeight() * factor);
			offscreen1.setVelocity(velocity);
			offscreen3.setPosition(getX(), getY() - world.getHeight() * factor2);
			offscreen3.setVelocity(velocity);
		}
		
		if (velocity.x < 0)
		{
			offscreen2.setPosition(getX() + world.getWidth() * factor, getY());
			offscreen2.setVelocity(velocity);
			offscreen4.setPosition(getX() + world.getWidth() * factor2, getY());
			offscreen4.setVelocity(velocity);
		}
		else
		{
			offscreen2.setPosition(getX() - world.getWidth() * factor, getY());
			offscreen2.setVelocity(velocity);
			offscreen4.setPosition(getX() - world.getWidth() * factor2, getY());
			offscreen4.setVelocity(velocity);
		}
		offscreen5.setPosition(offscreen2.getX(), offscreen1.getY());
		offscreen5.setVelocity(velocity);

		offscreens.add(offscreen1);
		offscreens.add(offscreen2);
		offscreens.add(offscreen3);
		offscreens.add(offscreen4);
		offscreens.add(offscreen5);
		
		for (WrapAroundImage a : offscreens)
		{
			world.addActor(a);
			a.setOrigin(a.getWidth() / 2, a.getHeight() / 2);
		}
		
	}
	
	protected abstract WrapAroundImage getNewObject();

	public void setAngle(float angle)
	{
		this.angle = angle;
		rotate(angle);
	}
	
	public void move(float delta)
	{
		float xPotential = getX() + velocity.x * delta;
		float yPotential = getY() + velocity.y * delta;
	
		setX(xPotential);
		setY(yPotential);
	}
	
	public void rotate(float rotation)
	{
		super.rotate(rotation);
		angle += rotation;
		
		if (!isOffscreen)
		{
			for (WrapAroundImage wai : offscreens)
			{
				wai.rotate(rotation);
			}
		}
	}
	
	public ArrayList<Vector2> getKeyPoints()
	{
		return keyPoints;
	}

	public ArrayList<Float> getRadia()
	{
		return radia;
	}
	
	public Point getUpdatedKeyPoint(int index)
	{
		Vector2 temp = keyPoints.get(index).cpy().rotate(angle);
		float x1 = getX() + getOriginX() + temp.x;
		float y1 = getY() + getOriginY() + temp.y;
		Point p1 = new Point(x1, y1);
		return p1;
	}

	public ArrayList<WrapAroundImage> getOffscreens()
	{
		return offscreens;
	}
	
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		batch.end();
		renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeType.Line);
        renderer.circle(getOriginX(), getOriginY(), getWidth() / 2);
        renderer.end();
        batch.begin();
		super.draw(batch, parentAlpha);
	}

}
