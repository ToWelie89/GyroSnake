package se.chalmers.snake.settings;

public class SettingsSession {
	
	private static SettingsSession settingsSession = new SettingsSession();
	
	public boolean musicEnabled;
	public boolean soundsEnabled;
	   
	   private SettingsSession(){ 
		   // Default settings
		   musicEnabled = true;
		   soundsEnabled = true;
	   }
	   	   
	   public static SettingsSession getInstance( ) {
	      return settingsSession;
	   }
}
