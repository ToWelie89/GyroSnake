package se.chalmers.snake.sound;

import android.content.Context;
import android.media.MediaPlayer;
import se.chalmers.snake.R;
import se.chalmers.snake.settings.SettingsSession;


public class SoundPlayer {
	
	Context context;
	MediaPlayer musicMediaPlayer;
	
	public SoundPlayer(Context context)
	{
		this.context = context;
	}
	
	private boolean soundsEnabled()
	{
		SettingsSession settingsSession = SettingsSession.getInstance();
		return settingsSession.soundsEnabled;
	}
	
	private boolean musicEnabled()
	{
		SettingsSession settingsSession = SettingsSession.getInstance();
		return settingsSession.musicEnabled;
	}
	
	public void playAppleSound()
	{
		if (soundsEnabled())
		{
			MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.apple);
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
			    public void onCompletion(MediaPlayer player) {
			       player.stop();
			       player.release();       
			    }
			});
			mediaPlayer.start();
		}
	}
	
	public void playBackgroundMusic()
	{
		if (musicEnabled())
		{
			musicMediaPlayer = MediaPlayer.create(context, R.raw.theme);
			musicMediaPlayer.setLooping(true);
			musicMediaPlayer.start();
		}
	}
	
	public void pauseBackgroundMusic()
	{
		if (musicEnabled())
		{
			musicMediaPlayer.pause();
		}
	}
	
	public void stopBackgroundMusic()
	{
		if (musicEnabled())
		{
			musicMediaPlayer.stop();
		}
	}
}
