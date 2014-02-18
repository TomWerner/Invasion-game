package com.codeday.invasion;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ShieldPowerUp extends PowerUp 
{

	protected Player player;
	public ShieldPowerUp(TextureRegion texture, World world, boolean isOffscreen, Player player)
	{
		super(texture, world, isOffscreen, player);
		time = new Timer(5000);
	}
	public ShieldPowerUp(Drawable texture, World world, boolean isOffscreen)
	{
		super(texture, world, isOffscreen);
	}
	
	protected WrapAroundImage getNewObject() 
	{
		return new ShieldPowerUp(getDrawable(), world, true);
	}
	
	public void deactivate()
	{
	}
	@Override
	public void activate(Player player)
	{
		System.out.println(player);
		player.addHealth();
	}
}
