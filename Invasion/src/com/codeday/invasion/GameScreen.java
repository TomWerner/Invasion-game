package com.codeday.invasion;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class GameScreen extends AbstractScreen 
{

	public GameScreen(Invasion game, int targetLevelId)
	{
		super(game);
		stage = new World(game);

		// play the level music
		game.getMusicManager()
				.play(new BackgroundMusic("music/ObservingTheStar.ogg",
						"Level Music"));
	}

	@Override
	protected boolean isGameScreen()
	{
		return true;
	}

	@Override
	public void show()
	{
		super.show();
		
		// add a fade-in effect to the whole stage
		stage.getRoot().getColor().a = 0f;
		stage.getRoot().addAction(Actions.fadeIn(0.5f));
	}

	@Override
	public void pause()
	{
		((World)stage).pause();
	}
}
