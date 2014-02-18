package com.codeday.invasion;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.codeday.invasion.SoundManager.SoundEffect;

public class World extends Stage
{
	private Invasion game;
	private WorldController controller;
	private Image one;
	private Image two;
	private Image three;
	private GameState gameState;
	private TextureAtlas atlas;
	private Player player;
	private int lives;
	private ArrayList<Image> lifeIcons = new ArrayList<Image>();
	private ArrayList<Level> levels = new ArrayList<Level>();
	private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	private ArrayList<PowerUp> powerups = new ArrayList<PowerUp>();
	private ArrayList<Laser> playerLasers = new ArrayList<Laser>();
	private ArrayList<Laser> enemyLasers = new ArrayList<Laser>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public static BackgroundMusic laserSound = new BackgroundMusic("music/laser1.mp3", "laser");
	
	private int currentLevel;
	private long pauseTime;
	private BitmapFont fontBig;
	private BitmapFont fontMedium;
	private BitmapFont fontSmall;
	private Image pauseButton;
	
	
	private Image forward, backward, shoot;

	enum GameState
	{
		COUNTDOWN, PLAYING, SCORE, WINNER, PLAYER_KILLED, GAME_OVER, NEXT_LEVEL, PAUSED
	}

	public World(Invasion game)
	{
		this.game = game;
		this.controller = new WorldController();
		Gdx.input.setInputProcessor(this);
		lives = 3;
		
		FileHandle fh = Gdx.files.internal("data/levels.dat");
		for (String str : fh.readString().split("\n"))
			levels.add(new Level(str));

		String textureFile = "images/pages-info.atlas";
		atlas = new TextureAtlas(Gdx.files.internal(textureFile),
				Gdx.files.internal("images"));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/ARDARLING.ttf"));
		fontBig = generator.generateFont((int) (getWidth() / 5.0));
		fontMedium = generator.generateFont((int) (getWidth() / 20.0));
		fontSmall = generator.generateFont((int) (getWidth() / 30.0));
		generator.dispose();
		
		pauseButton = new Image(atlas.findRegion("pauseButton"));
		pauseButton.setPosition(getWidth() - pauseButton.getWidth(), getHeight() - pauseButton.getHeight());

		forward = new Image(atlas.findRegion("androidButton"));
		forward.setSize(getWidth() / 2, getHeight() / 2);
		forward.setPosition(0, getHeight() / 2);
		
		backward = new Image(atlas.findRegion("androidButton"));
		backward.setSize(getWidth() / 2, getHeight() / 2);
		backward.setPosition(0, 0);
		
		shoot = new Image(atlas.findRegion("androidButton"));
		shoot.setSize(getWidth() / 2, getHeight());
		shoot.setPosition(getWidth() / 2, 0);
		
		setupCountdown();
		setupLevel();
		
		addActor(forward);
		addActor(backward);
		addActor(shoot);
		forward.addAction(Actions.sequence(Actions.fadeOut(20), Actions.removeActor()));
		backward.addAction(Actions.sequence(Actions.fadeOut(20), Actions.removeActor()));
		shoot.addAction(Actions.sequence(Actions.fadeOut(20), Actions.removeActor()));
	}
	
	protected void setupLevel()
	{
		gameState = GameState.PLAYING;
		pauseButton.remove();
		
		for (int i = 0; i < getActors().size; )
			getActors().removeIndex(i).remove();
		asteroids = new ArrayList<Asteroid>();
		powerups = new ArrayList<PowerUp>();
		enemyLasers = new ArrayList<Laser>();
		playerLasers = new ArrayList<Laser>();
		enemies = new ArrayList<Enemy>();
		Asteroid.INITIAL_SPEED = (float) (25 * Math.pow(1.1, currentLevel));
		Enemy.INITIAL_SPEED = (float) (100 * Math.pow(1.3, currentLevel));
		Enemy.SHOOT_DELAY = (long) (500 * Math.pow(.9, currentLevel));
		
		int tileWidth = atlas.findRegion("starBackground").getRegionWidth();
		int tileHeight = atlas.findRegion("starBackground").getRegionHeight();
		Image background;
		for (int x = 0; x * tileWidth < this.getWidth(); x++)
		{
			for (int y = 0; y * tileHeight < this.getHeight(); y++)
			{

				background = new Image(this.getAtlas().findRegion(
						"starBackground"));
				background.setPosition(x * tileWidth, y * tileHeight);
				addActor(background);
			}
		}

		for (int x = 0; x < lives; x++)
		{
			Image life = new Image(atlas.findRegion("life"));
			life.setPosition(x * life.getWidth(),
					getHeight() - life.getHeight());
			lifeIcons.add(life);
			addActor(lifeIcons.get(x));
		}
		
		levels.get(currentLevel).buildLevel(this);
		
		
		player = new Player(atlas.findRegion("playerLeft"),
				atlas.findRegion("player"), atlas.findRegion("playerRight"),
				this, false);
		player.setPosition(getWidth() / 2 - player.getWidth() / 2, getHeight()
				/ 2 - player.getWidth());
		addActor(player);
		
		addActor(pauseButton);
		if (!Invasion.DEV_MODE)
		{
			startCountdown();
		}
	}

	public void act(float delta)
	{
		super.act(delta);
		if (gameState.equals(GameState.PLAYING))
		{
			playerControls(delta);
			updateObjects(delta);
			
			if (asteroids.size() == 0 && enemies.size() == 0)
			{
				if (currentLevel + 1 <= levels.size() - 1)
					nextLevel();
				else
					youWin();
			}
		}
		else if (gameState.equals(GameState.PAUSED))
		{
			if ((Gdx.input.justTouched() || controller.isKeyDown()) &&
					System.currentTimeMillis() - pauseTime > 1000)
			{
				System.out.println(System.currentTimeMillis() - pauseTime);
				resume();
			}
		}
	}
	
	private void youWin()
	{
		Image youWin = new Image(this.getAtlas().findRegion("youWin"));
		youWin.setSize(game.getWidth() * .75f, game.getWidth() / 4);
		youWin.setPosition(game.getWidth() / 2 - youWin.getWidth() / 2,
				game.getHeight() / 2 - youWin.getHeight() / 2);

		addActor(youWin);
		gameState = GameState.WINNER;

		youWin.addAction(Actions.sequence(Actions.fadeIn(.25f),
		Actions.delay(3f), Actions.fadeOut(.25f), new Action()
		{
			public boolean act(float delta)
			{
				game.setScreen(new MenuScreen(game));
				return true;
			}
		}));
	}
	
	private void nextLevel()
	{
		Image nextLevel = new Image(this.getAtlas().findRegion("nextLevel"));
		nextLevel.setSize(game.getWidth() * .75f, game.getWidth() / 4);
		nextLevel.setPosition(game.getWidth() / 2 - nextLevel.getWidth() / 2,
				game.getHeight() / 2 - nextLevel.getHeight() / 2);

		addActor(nextLevel);
		gameState = GameState.NEXT_LEVEL;

		nextLevel.addAction(Actions.sequence(Actions.fadeIn(.25f),
		Actions.delay(3f), Actions.fadeOut(.25f), new Action()
		{
			public boolean act(float delta)
			{
				gameState = GameState.PLAYING;
				currentLevel++;
				setupLevel();
				return true;
			}
		}));
	}
	
	private void updateObjects(float delta)
	{
		for (int i = 0; i < playerLasers.size();)
		{
			Laser l = playerLasers.get(i);
			l.update(delta, this);

			if (l.isDestroyed())
				playerLasers.remove(i).remove();
			else
				i++;
		}

		for (int i = 0; i < asteroids.size();)
		{
			Asteroid a = asteroids.get(i);
			a.update(delta, this, playerLasers, asteroids);
//			a.update(delta, this, enemyLasers, asteroids);

			if (a.isDestroyed())
				asteroids.remove(i).remove();
			else
				i++;
		}
		
		for (int i = 0; i < powerups.size();)
		{
			PowerUp pu = powerups.get(i);
			pu.update(delta);
			
			if (pu.isCollected())
			{
				pu.remove();
				player.addPowerUp(powerups.remove(i));
			}
			else
				i++;
		}
		
		for (int i = 0; i < enemies.size();)
		{
			Enemy a = enemies.get(i);
			a.shoot(enemyLasers, this);
			a.update(delta, this, playerLasers, asteroids, enemies);

			if (a.isDestroyed())
				enemies.remove(i).remove();
			else
				i++;
		}
		
		for (int i = 0; i < enemyLasers.size();)
		{
			Laser l = enemyLasers.get(i);
			l.update(delta, this);

			if (l.isDestroyed())
				enemyLasers.remove(i).remove();
			else
				i++;
		}


	}
	
	private void playerControls(float delta)
	{
		float rotate = 0;
		if (controller.isKeyDown(Keys.LEFT))
			rotate += 270;
		if (controller.isKeyDown(Keys.RIGHT))
			rotate -= 270;
		
		rotate -= 45 * Gdx.input.getAccelerometerY();
		
		float thrust = 0;
		if (controller.isKeyDown(Keys.UP) && (!Gdx.input.isPeripheralAvailable(Peripheral.MultitouchScreen) || Gdx.input.isTouched()))
			thrust = 2;
		if (controller.isKeyDown(Keys.DOWN) && (!Gdx.input.isPeripheralAvailable(Peripheral.MultitouchScreen) || Gdx.input.isTouched()))
			thrust = -2;


		player.update(delta, this, rotate, thrust, asteroids, powerups, enemyLasers);

		if (controller.isKeyDown(Keys.SPACE) && (!Gdx.input.isPeripheralAvailable(Peripheral.MultitouchScreen) || Gdx.input.isTouched()))
			player.shoot(playerLasers);
		
		
		if (controller.isKeyDown(Keys.P))
		{
			pause();
		}
	}

	public void draw()
	{
		super.draw();
		this.getSpriteBatch().begin();
		
		if (gameState.equals(GameState.PAUSED))
		{
			fontBig.setColor(1, 1, 1, 1);
			String string = "Paused";
			TextBounds bounds = fontBig.getBounds(string);
			fontBig.draw(getSpriteBatch(), string, getWidth() / 2 - bounds.width / 2, getHeight() / 2 + bounds.height / 2);
			
			fontMedium.setColor(1, 1, 1, 1);
			string = "Press any key or touch to resume";
			bounds = fontMedium.getBounds(string);
			fontMedium.draw(getSpriteBatch(), string, getWidth() / 2 - bounds.width / 2, getHeight() / 4 + bounds.height / 2);
		}
		this.getSpriteBatch().end();
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
			controller.setKeyDown(Keys.DOWN);
		else
			controller.setKeyUp(Keys.DOWN);
		
		if (screenX < getWidth() / 2 && screenY < getHeight() / 2)
			controller.setKeyDown(Keys.UP);
		else
			controller.setKeyUp(Keys.UP);

		if (screenX > getWidth() / 2)
			controller.setKeyDown(Keys.SPACE);
		else
			controller.setKeyUp(Keys.SPACE);
		
		screenY = (int) (getHeight() - screenY);
		if (screenX > pauseButton.getX() && screenX < pauseButton.getX() + pauseButton.getWidth() &&
			screenY > pauseButton.getY() && screenY < pauseButton.getY() + pauseButton.getHeight())
		{
			pause();
		}
		
		return true;
	}

	public Invasion getGame()
	{
		return game;
	}

	public TextureAtlas getAtlas()
	{
		return atlas;
	}

	public void addPowerUp(float x, float y) // Adds power up
	{
		if (Math.random() > 0.5) // Randomly selects if it is shield or shooting
									// power up
		{
			ShootPowerUp spu = new ShootPowerUp(
					atlas.findRegion("powerupRedWeapon"), this, false, player);
			spu.setPosition(x, y);
			powerups.add(spu);
			addActor(spu);
		}
		else
		{
			ShieldPowerUp spu = new ShieldPowerUp(
					atlas.findRegion("shieldGold"), this, false, player);
			spu.setPosition(x, y);
			powerups.add(spu);
			addActor(spu);
		}
	}

	public void loseLives()
	{
		lives -= 1;
		lifeIcons.get(lives).remove();
		lifeIcons.remove(lives);
		gameState = GameState.PLAYER_KILLED;

		if (lives == 0)
			gameOver();
		else
			playerDeath();
	}

	public void playerDeath(float x, float y)
	{
		loseLives();
	}

	public void addAsteroid(Asteroid a)
	{
		asteroids.add(a); 
		addActor(a);
	}
	
	public void addEnemy(Enemy e)
	{
		addActor(e);
		enemies.add(e);
	}
	
	private void gameOver()
	{
		Image gameOver = new Image(this.getAtlas().findRegion("gameOver"));
		gameOver.setSize(game.getWidth() * .75f, game.getWidth() / 4);
		gameOver.setPosition(game.getWidth() / 2 - gameOver.getWidth() / 2,
				game.getHeight() / 2 - gameOver.getHeight() / 2);

		addActor(gameOver);
		game.getSoundManager().play(SoundEffect.GAME_OVER);

		gameOver.addAction(Actions.sequence(Actions.fadeIn(.25f),
		Actions.delay(3f), Actions.fadeOut(.25f), new Action()
		{
			public boolean act(float delta)
			{
				gameState = GameState.GAME_OVER;
				game.setScreen(new MenuScreen(game));

				return true;
			}
		}));
	}

	private void playerDeath()
	{
		gameState = GameState.PLAYER_KILLED;
		
		Image playerDied = new Image(this.getAtlas().findRegion("playerDied"));
		playerDied.setSize(game.getWidth() * .75f, game.getWidth() / 4);
		playerDied.setPosition(game.getWidth() / 2 - playerDied.getWidth() / 2,
				game.getHeight() / 2 - playerDied.getHeight() / 2);

		addActor(playerDied);
		game.getSoundManager().play(SoundEffect.YOU_DIED);

		playerDied.addAction(Actions.sequence(Actions.fadeIn(.25f),
		Actions.delay(3f), Actions.fadeOut(.25f), new Action()
		{
			public boolean act(float delta)
			{
				gameState = GameState.PLAYING;
				setupLevel();

				return true;
			}
		}));
	}


	private void startCountdown()
	{
		addActor(three);

		gameState = GameState.COUNTDOWN;
		three.addAction(Actions.sequence(Actions.fadeIn(.25f),
				Actions.delay(.5f), Actions.fadeOut(.25f), new Action()
				{
					public boolean act(float delta)
					{
						addActor(two);
						two.addAction(Actions.sequence(Actions.fadeIn(.25f),
								Actions.delay(.5f), Actions.fadeOut(.25f),
								new Action()
								{
									public boolean act(float delta)
									{
										addActor(one);
										one.addAction(Actions.sequence(
												Actions.fadeIn(.25f),
												Actions.delay(.5f),
												Actions.fadeOut(.25f),
												new Action()
												{
													public boolean act(
															float delta)
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
	
	private void setupCountdown()
	{
		one = new Image(atlas.findRegion("1"));
		one.setSize(game.getWidth() / 3, game.getWidth() / 3);
		one.setPosition(game.getWidth() / 2 - one.getWidth() / 2,
				game.getHeight() / 2 - one.getHeight() / 2);

		two = new Image(atlas.findRegion("2"));
		two.setSize(game.getWidth() / 3, game.getWidth() / 3);
		two.setPosition(game.getWidth() / 2 - two.getWidth() / 2,
				game.getHeight() / 2 - two.getHeight() / 2);

		three = new Image(atlas.findRegion("3"));
		three.setSize(game.getWidth() / 3, game.getWidth() / 3);
		three.setPosition(game.getWidth() / 2 - three.getWidth() / 2,
				game.getHeight() / 2 - three.getHeight() / 2);

		one.getColor().a = 0f;
		two.getColor().a = 0f;
		three.getColor().a = 0f;
	}

	public void pause()
	{
		gameState = GameState.PAUSED;
		pauseTime = System.currentTimeMillis();
	}
	
	public void resume()
	{
		gameState = GameState.COUNTDOWN;
		startCountdown();
	}
}
