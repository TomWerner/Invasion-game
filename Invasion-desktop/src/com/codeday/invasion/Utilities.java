package com.codeday.invasion;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Utilities
{

	private static final String INPUT_DIR = "../Invasion-android/assets/images";
	private static final String OUTPUT_DIR = "../Invasion-android/assets/images";
	private static final String PACK_FILE = "pages-info";

	public static void packTextures()
	{
		// create the packing's settings
		Settings settings = new Settings();

		// adjust the padding settings
		settings.paddingX = 2;
		settings.paddingY = 2;
		settings.edgePadding = false;

		// don't repack a group when no changes were made to it
		settings.combineSubdirectories = true;

		// pack the images
		TexturePacker2.process(settings, INPUT_DIR, OUTPUT_DIR, PACK_FILE);

	}
	
	public static void main(String[] args)
	{
		packTextures();
	}
}
