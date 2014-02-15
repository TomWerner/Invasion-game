package com.codeday.invasion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class World extends Stage
{
	private Invasion game;
	private Texture paddleTex;
	private Paddle player1;
	private Paddle player2;
	private Image middleWall;
	private WorldController controller;
	private Texture middleTexture;
	private Texture ballTexture;
	private Ball ball;
	private Image one;
	private Image two;
	private Actor three;
	private GameState gameState;
	private int rightPlayerScore;
	private int leftPlayerScore;
	private int rightPlayerGame;
	private int leftPlayerGame;
	private Image p1Score;
	private Image p2Score;
	private Image p1Win;
	private Image p2Win;
	private BitmapFont font;
	
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
		
		font = new BitmapFont();

		paddleTex = new Texture(Gdx.files.internal("images/paddle.png"));
		middleTexture = new Texture(
				Gdx.files.internal("images/middle-wall.png"));
		ballTexture = new Texture(Gdx.files.internal("images/ball.png"));

		int width = game.getWidth();
		int height = game.getHeight();

		player1 = new Paddle(paddleTex, new Vector2(0, 0));
		player1.setSize(width / 50, height / 4);
		player1.setPosition(-player1.getWidth() / 2, height / 2
				- player1.getHeight() / 2);

		player2 = new Paddle(paddleTex, new Vector2(0, 0));
		player2.setSize(width / 50, height / 4);
		player2.setPosition(width - player2.getWidth() / 2, height
				/ 2 - player2.getHeight() / 2);

		middleWall = new Image(middleTexture);
		middleWall.setSize(height / 50, height);
		middleWall.setPosition(width / 2 - middleWall.getWidth() / 2, 0);
		
		ball = new Ball(ballTexture);

		this.addActor(player1);
		this.addActor(player2);
		this.addActor(middleWall);
		this.addActor(ball);

		p1Score = new Image(new Texture(Gdx.files.internal("images/player-1-score.png")));
		p1Score.setSize(game.getWidth() * 2.0f / 3.0f, game.getWidth() * 2.0f / 3.0f / 2.5f);
		p1Score.setPosition(game.getWidth() / 2 - p1Score.getWidth() / 2, game.getHeight() / 2 - p1Score.getHeight() / 2);

		p2Score = new Image(new Texture(Gdx.files.internal("images/player-2-score.png")));
		p2Score.setSize(game.getWidth() * 2.0f / 3.0f, game.getWidth() * 2.0f / 3.0f / 2.5f);
		p2Score.setPosition(game.getWidth() / 2 - p2Score.getWidth() / 2, game.getHeight() / 2 - p2Score.getHeight() / 2);

		p1Win = new Image(new Texture(Gdx.files.internal("images/player-1-win.png")));
		p1Win.setSize(game.getWidth() * 2.0f / 3.0f, game.getWidth() * 2.0f / 3.0f / 2.5f);
		p1Win.setPosition(game.getWidth() / 2 - p1Win.getWidth() / 2, game.getHeight() / 2 - p1Win.getHeight() / 2);

		p2Win = new Image(new Texture(Gdx.files.internal("images/player-2-win.png")));
		p2Win.setSize(game.getWidth() * 2.0f / 3.0f, game.getWidth() * 2.0f / 3.0f / 2.5f);
		p2Win.setPosition(game.getWidth() / 2 - p2Win.getWidth() / 2, game.getHeight() / 2 - p2Win.getHeight() / 2);
		
		
		
		one = new Image(new Texture(Gdx.files.internal("images/1.png")));
		one.setSize(game.getWidth() / 3, game.getWidth() / 3);
		one.setPosition(game.getWidth() / 2 - one.getWidth() / 2, game.getHeight() / 2 - one.getHeight() / 2);
		two = new Image(new Texture(Gdx.files.internal("images/2.png")));
		two.setSize(game.getWidth() / 3, game.getWidth() / 3);
		two.setPosition(game.getWidth() / 2 - two.getWidth() / 2, game.getHeight() / 2 - two.getHeight() / 2);
		three = new Image(new Texture(Gdx.files.internal("images/3.png")));
		three.setSize(game.getWidth() / 3, game.getWidth() / 3);
		three.setPosition(game.getWidth() / 2 - three.getWidth() / 2, game.getHeight() / 2 - three.getHeight() / 2);

		one.getColor().a = 0f;
		two.getColor().a = 0f;
		three.getColor().a = 0f;
		startCountdown();
	}
	
	private void startCountdown()
	{
		ball.setSize(getHeight() / 25, getHeight() / 25);
		ball.setPosition(getWidth() / 2 - ball.getWidth() / 2, getHeight() / 2 - ball.getHeight() / 2);
		ball.randomizeDirection();
		ball.increaseSpeed();		
		
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
			if (controller.isKeyDown(Keys.A))
				player1.move(true, delta, getHeight());
			else if (controller.isKeyDown(Keys.Z))
				player1.move(false, delta, getHeight());
			if (controller.isKeyDown(Keys.UP))
				player2.move(true, delta, getHeight());
			else if (controller.isKeyDown(Keys.DOWN))
				player2.move(false, delta, getHeight());
//			ball.update(delta, this, player1, player2);
		}
	}
	
	public void draw()
	{
		this.getSpriteBatch().begin();
		String score1 = leftPlayerGame + " - " + leftPlayerScore;
		String score2 = rightPlayerGame + " - " + rightPlayerScore;
		float size1 = score1.length() * font.getSpaceWidth();
		float size2 = score2.length() * font.getSpaceWidth();
		font.draw(this.getSpriteBatch(), score1, getWidth() / 4 - size1 / 2, getHeight() - getHeight() / 30);
		font.draw(this.getSpriteBatch(), score2, getWidth() - getWidth() / 4 - size2 / 2, getHeight() - getHeight() / 30);
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

	public void rightPlayerScore()
	{
		rightPlayerScore++;
		if (rightPlayerScore >= 3)
		{
			gameState = GameState.WINNER;
			rightPlayerGame++;
			rightPlayerScore = 0;
			player2.setSize(player1.getWidth(), player2.getHeight() * .9f);
			splashText(p2Win);
			player1.increaseSpeed();
		}
		else
		{
			gameState = GameState.SCORE;
			splashText(p2Score);
		}
	}
	
	public void leftPlayerScore()
	{
		leftPlayerScore++;
		if (leftPlayerScore >= 3)
		{
			gameState = GameState.WINNER;
			leftPlayerGame++;
			leftPlayerScore = 0;
			player1.setSize(player1.getWidth(), player1.getHeight() * .9f);
			splashText(p1Win);
			player2.increaseSpeed();
		}
		else
		{
			gameState = GameState.SCORE;
			splashText(p1Score);
		}
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
}
