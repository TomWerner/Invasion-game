package com.codeday.invasion;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SmallAsteroid extends Asteroid {
	
	public SmallAsteroid(TextureRegion texture) {
		super(texture);
		
		randomizeDirection();
	}
}
