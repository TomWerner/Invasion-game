package com.codeday.invasion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
		

		String textureFile = "images/pages-info.atlas";
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(textureFile),
				Gdx.files.internal("images"));
		int tileWidth = atlas.findRegion("starBackground").getRegionWidth();
		int tileHeight = atlas.findRegion("starBackground").getRegionHeight();
		Image background;
		
		for (int x = 0; x * tileWidth < game.getWidth(); x++)
		{
			for (int y = 0; y * tileHeight < game.getHeight(); y++)
			{

				background = new Image(atlas.findRegion(
						"starBackground"));
				background.setPosition(x * tileWidth, y * tileHeight);
				table.addActor(background);
			}
		}
		Image t = new Image(new Texture(
				Gdx.files.internal("images/title.png")));
		t.setFillParent(true);
//		t.setPosition(game.getWidth() / 2 - t.getWidth() / 2, game.getHeight() / 2 - t.getHeight() / 2);
		table.addActor(t);
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
	}
	
	@Override
	public void pause()
	{
		
	}
}