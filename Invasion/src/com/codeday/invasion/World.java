package com.codeday.invasion;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class World extends Stage
{
	private Invasion game;
	private WorldController controller;
	private Image one;
	private Image two;
	private Image three;
	private GameState gameState;
	private BitmapFont font;
	private ArrayList<Asteroid> asteroidList;
	private TextureAtlas atlas;
	private Player player;
	private Laser laser;
	enum GameState{
		COUNTDOWN,
		PLAYING,
		SCORE,
		WINNER
	}

	public World(Invasion game)
	{
		this.game = game;
		this.controller = new WorldController();
		Gdx.input.setInputProcessor(this);
		gameState = GameState.PLAYING;
		
		font = new BitmapFont();
		Utilities.packTextures();
		
		asteroidList = new ArrayList<Asteroid>();
		
		
		String textureFile = "images/pages-info.atlas";
        atlas = new TextureAtlas(Gdx.files.internal(textureFile), Gdx.files.internal("images"));
        
        AtlasRegion ar = atlas.findRegion("meteorBig");
        asteroidList.add(new Asteroid(ar));
        
//        for (AtlasRegion a : atlas.getRegions())
//        	System.out.println(a.name);
        
//        for (Asteroid a : asteroidList)
//        {
//        	addActor(a);
//        	a.setPosition(getWidth() / 2, getHeight() / 2);
//        }
        
        player = new Player(atlas.findRegion("playerLeft"), 
			        		atlas.findRegion("playerLeft"), 
			        		atlas.findRegion("playerLeft"));
        player.setPosition(getWidth() / 2 - player.getWidth() / 2, getHeight() / 2 - player.getWidth() );
        addActor(player);
//        setupCountdown();
//        startCountdown();
        
	}
	
	private void setupCountdown()
	{
		one = new Image(atlas.findRegion("1"));
		one.setSize(game.getWidth() / 3, game.getWidth() / 3);
		one.setPosition(game.getWidth() / 2 - one.getWidth() / 2, game.getHeight() / 2 - one.getHeight() / 2);

		two = new Image(atlas.findRegion("2"));
		two.setSize(game.getWidth() / 3, game.getWidth() / 3);
		two.setPosition(game.getWidth() / 2 - two.getWidth() / 2, game.getHeight() / 2 - two.getHeight() / 2);
		
		three = new Image(atlas.findRegion("3"));
		three.setSize(game.getWidth() / 3, game.getWidth() / 3);
		three.setPosition(game.getWidth() / 2 - three.getWidth() / 2, game.getHeight() / 2 - three.getHeight() / 2);

		one.getColor().a = 0f;
		two.getColor().a = 0f;
		three.getColor().a = 0f;
	}

	private void startCountdown()
	{
		addActor(three);
		
		gameState = GameState.COUNTDOWN;
		three.addAction(Actions.sequence(Actions.fadeIn(.25f), Actions.delay(.5f), Actions.fadeOut(.25f), new Action()
		{
			public boolean act(float delta)
			{
				addActor(two);
				two.addAction(Actions.sequence(Actions.fadeIn(.25f), Actions.delay(.5f), Actions.fadeOut(.25f), new Action()
				{
					public boolean act(float delta)
					{
						addActor(one);
						one.addAction(Actions.sequence(Actions.fadeIn(.25f), Actions.delay(.5f), Actions.fadeOut(.25f), new Action()
						{
							public boolean act(float delta)
							{
								gameState = GameState.PLAYING;
								
								
								
								
								return true;
							}
						}, Actions.removeActor()));
						return true;
					}
				}, Actions.removeActor()));
				return true;
				
			}
		}, Actions.removeActor()));
	}

	public WorldController getController()
	{
		return controller;
	}

	public void act(float delta)
	{
		super.act(delta);
		
		if (gameState.equals(GameState.PLAYING))
		{

			float rotate = 0;
			if (controller.isKeyDown(Keys.LEFT))
				rotate -= 15;
			if (controller.isKeyDown(Keys.RIGHT));
			player.update(delta, this, rotate, 0);
			if(controller.isKeyDown(Keys.SPACE ))
			{
				laser = new Laser(atlas.findRegion("laserRed"), player.getAngle());
		        laser.setPosition(player.getWidth(), player.getHeight());
		        addActor(laser);
				laser.update(delta, this);
				
			}
//			if (controller.isKeyDown(Keys.A))
//				player1.move(true, delta, getHeight());
//			else if (controller.isKeyDown(Keys.Z))
//				player1.move(false, delta, getHeight());
//			if (controller.isKeyDown(Keys.UP))
//				player2.move(true, delta, getHeight());
//			else if (controller.isKeyDown(Keys.DOWN))
//				player2.move(false, delta, getHeight());
//			ball.update(delta, this, player1, player2);
		}
	}
	
	public void draw()
	{
		this.getSpriteBatch().begin();
	    this.getSpriteBatch().end();
		super.draw();
	}

	@Override
	public boolean keyDown(int keycode)
	{
		controller.setKeyDown(keycode);
		return true;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		controller.setKeyUp(keycode);
		return true;
	}
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if (screenX < getWidth() / 2 && screenY > getHeight() / 2)
			controller.setKeyDown(Keys.Z);
		if (screenX < getWidth() / 2 && screenY < getHeight() / 2)
			controller.setKeyDown(Keys.A);
		if (screenX > getWidth() / 2 && screenY > getHeight() / 2)
			controller.setKeyDown(Keys.DOWN);
		if (screenX > getWidth() / 2 && screenY < getHeight() / 2)
			controller.setKeyDown(Keys.UP);
		
		return true;
	}
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if (screenX < getWidth() / 2 && screenY > getHeight() / 2)
			controller.setKeyUp(Keys.A);
		if (screenX < getWidth() / 2 && screenY < getHeight() / 2)
			controller.setKeyUp(Keys.Z);
		if (screenX > getWidth() / 2 && screenY > getHeight() / 2)
			controller.setKeyUp(Keys.UP);
		if (screenX > getWidth() / 2 && screenY < getHeight() / 2)
			controller.setKeyUp(Keys.DOWN);
		
		return true;
	}

	
	private void splashText(Image image)
	{
		addActor(image);
		
		image.addAction(Actions.sequence(Actions.fadeIn(.25f), Actions.delay(2f), Actions.fadeOut(.25f), new Action()
		{
			public boolean act(float delta)
			{
				startCountdown();
				return true;
			}
		}));
	}
	
	public Invasion getGame()
	{
		return game;
	}
	
	public TextureAtlas getAtlas()
	{
		return atlas;
	}
}
