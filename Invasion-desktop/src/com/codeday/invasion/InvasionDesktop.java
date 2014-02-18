package com.codeday.invasion;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class InvasionDesktop
{
	public static void main(String[] args)
	{
		Utilities.packTextures();
		new LwjglApplication(new Invasion(), "Invasion", 1280, 720, true);
	}
}
