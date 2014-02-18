package com.codeday.invasion;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Laser extends Image
{
	private Vector2 velocity;
	private boolean destroyed;
	private ShapeRenderer renderer;
	public static final float INITIAL_SPEED = 500;

	public Laser(TextureRegion texture, float direction)
	{

		super(texture);
		float direct = direction;
		velocity = new Vector2((float) Math.cos(Math.toRadians(direct))
				* INITIAL_SPEED, (float) Math.sin(Math.toRadians(direction))
				* INITIAL_SPEED);
		setOrigin(getWidth() / 2, getHeight() / 2);
		rotate(90);
		rotate(direction);
		
		renderer = new ShapeRenderer();
	}

	public void update(float delta, World world)
	{
		float xPotential = getX() + velocity.x * delta;
		float yPotential = getY() + velocity.y * delta;
		setX(xPotential);
		setY(yPotential);

		if (getX() + getOriginX() > world.getWidth()
				|| getX() + getOriginX() < 0)
			destroyed = true;
		if (getY() + getOriginY() > world.getHeight()
				|| getY() + getOriginY() < 0)
			destroyed = true;
		if (isDestroyed())
			remove();
	}

	public boolean isDestroyed()
	{
		return destroyed;
	}
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		batch.end();
		renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeType.Line);
        renderer.circle(getOriginX(), getOriginY(), getHeight() / 2);
        renderer.end();
        batch.begin();
		super.draw(batch, parentAlpha);
	}

	public Vector2 getVelocity()
	{
		return velocity;
	}

}
