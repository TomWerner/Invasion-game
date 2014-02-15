package com.codeday.invasion;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class InvasionDesktop
{
	public static void main(String[] args)
	{
		new LwjglApplication(new Invasion(), "Invasion", 800, 600, true);
	}
}
