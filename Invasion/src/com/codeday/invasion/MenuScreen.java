package com.codeday.invasion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen extends AbstractScreen
{
	public MenuScreen(Invasion game)
	{
		super(game);
	}

	@Override
	public void show()
	{
		super.show();

		// retrieve the default table actor
		Table table = super.getTable();
		
		TextureRegion t = new TextureRegion(new Texture(
				Gdx.files.internal("data/splashscreen.png")));
		Drawable splashDrawable = new TextureRegionDrawable(t);
		table.setBackground(splashDrawable);
		table.add("").spaceBottom(300);
		table.row();

		// register the button "start game"
		TextButton startGameButton = new TextButton("Start game", getSkin());
		startGameButton.addListener(new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
			{
				game.setScreen(new GameScreen(game, 1));
		    }
		});
		
		table.add(startGameButton).size(300, 60).uniform().spaceBottom(10);
		table.row();

		// register the button "options"
		TextButton optionsButton = new TextButton("Options", getSkin());
		optionsButton.addListener(new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
			{
				System.out.println("options");
		    }
		});
		table.add(optionsButton).uniform().fill().spaceBottom(10);
		table.row();
	}
}