package com.codeday.invasion;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Player extends Image
{
	private Drawable left;
	private Drawable middle;
	private Drawable right;
	private float angle;
	
	public static float MAX_SPEED = 100;
	public static float MAX_ACCELERATION_SQUARED = 5;
	private Vector2 velocity = new Vector2(0, 0);
	private Vector2 acceleration = new Vector2(0, 0);

	public Player(TextureRegion left, TextureRegion middle, TextureRegion right)
	{
		super(middle);
		this.left = new TextureRegionDrawable(left);
		this.middle = new TextureRegionDrawable(middle);
		this.right = new TextureRegionDrawable(right);
		angle = 0;
	}
	
	public void update(float delta, World world, float rotate, float thrust)
	{
		if (rotate == 0)
			setDrawable(middle);
		else if (rotate < 0)
			setDrawable(left);
		else
			setDrawable(right);
			
		setOrigin(getWidth() / 2, getHeight() / 2);
		rotate(rotate * delta);
		angle += rotate * delta;

		float dxA = (float) (Math.cos(Math.toRadians(angle)) * thrust);
		float dyA = (float) (Math.sin(Math.toRadians(angle)) * thrust);
		Vector2 accelPotential = acceleration.add(dxA, dxA);
		if (accelPotential.len2() <= MAX_ACCELERATION_SQUARED)
			acceleration.add(dxA, dyA);
		
		velocity.add(acceleration.cpy().scl(delta));
		
		Vector2 dV = velocity.cpy().scl(delta);
		setX(getX() + dV.x);
		setY(getY() + dV.y);
	}
	
	public float getAngle()
	{
		return angle;
	}
}
