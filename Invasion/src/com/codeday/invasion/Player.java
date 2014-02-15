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
		angle = 90;
	}
	
	public void update(float delta, World world, float rotate, float thrust)
	{
		if (rotate == 0)
			setDrawable(middle);
		else if (rotate < 0)
			setDrawable(right);
		else
			setDrawable(left);
			
		setOrigin(getWidth() / 2, getHeight() / 2);
		rotate(rotate * delta);
		angle += rotate * delta;

		float dxA = (float) (Math.cos(Math.toRadians(angle)) * thrust);
		float dyA = (float) (Math.sin(Math.toRadians(angle)) * thrust);
		
		setX(getX() + dxA);
		setY(getY() + dyA);
	}
	
	public float getAngle()
	{
		return angle;
	}
}
