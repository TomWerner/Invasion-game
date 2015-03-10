package com.codeday.invasion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.codeday.invasion.LRUCache.CacheEntryRemovedListener;
import com.codeday.invasion.SoundManager.SoundEffect;


/**
 * A service that manages the sound effects.
 */
public class SoundManager implements
		CacheEntryRemovedListener<SoundEffect, Sound>, Disposable
{
	/**
	 * The available sound files.
	 */
	public enum SoundEffect
	{
		LASER("music/laser1.mp3"),
		GAME_OVER("music/GameOver.ogg"),
		YOU_DIED("music/youDied.ogg"),
		MENU_SOUND("music/MenuSound.ogg");

		private final String fileName;

		private SoundEffect(String fileName)
		{
			this.fileName = fileName;
		}

		public String getFileName()
		{
			return fileName;
		}
	}

	/**
	 * The volume to be set on the sound.
	 */
	private float volume = 1f;

	/**
	 * Whether the sound is enabled.
	 */
	private boolean enabled = true;

	/**
	 * The sound cache.
	 */
	private final LRUCache<SoundEffect, Sound> soundCache;

	/**
	 * Creates the sound manager.
	 */
	public SoundManager()
	{
		soundCache = new LRUCache<SoundManager.SoundEffect, Sound>(10);
		soundCache.setEntryRemovedListener(this);
	}

	/**
	 * Plays the specified sound.
	 */
	public void play(SoundEffect sound)
	{
		// check if the sound is enabled
		if (!enabled)
			return;

		// try and get the sound from the cache
		Sound soundToPlay = soundCache.get(sound);
		if (soundToPlay == null)
		{
			FileHandle soundFile = Gdx.files.internal(sound.getFileName());
			soundToPlay = Gdx.audio.newSound(soundFile);
			soundCache.add(sound, soundToPlay);
		}

		// play the sound
		soundToPlay.play(volume);
	}

	/**
	 * Sets the sound volume which must be inside the range [0,1].
	 */
	public void setVolume(float volume)
	{
		Gdx.app.log(Invasion.LOG, "Adjusting sound volume to: " + volume);

		// check and set the new volume
		if (volume < 0 || volume > 1f)
		{
			throw new IllegalArgumentException(
					"The volume must be inside the range: [0,1]");
		}
		this.volume = volume;
	}

	/**
	 * Enables or disabled the sound.
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	// EntryRemovedListener implementation

	@Override
	public void notifyEntryRemoved(SoundEffect key, Sound value)
	{
		Gdx.app.log(Invasion.LOG, "Disposing sound: " + key.name());
		value.dispose();
	}

	/**
	 * Disposes the sound manager.
	 */
	public void dispose()
	{
		Gdx.app.log(Invasion.LOG, "Disposing sound manager");
		for (Sound sound : soundCache.retrieveAll())
		{
			sound.stop();
			sound.dispose();
		}
	}
}
