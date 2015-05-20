package se.chalmers.snake;

import java.net.ProtocolException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import se.chalmers.snake.highscoreDatabase.DataBaseUser;
import se.chalmers.snake.highscoreDatabase.ScoreHandler;
import se.chalmers.snake.interfaces.HighscoreDatabaseIC;
import se.chalmers.snake.mastercontroller.ControlResources;
import se.chalmers.snake.settings.SettingsSession;

public class StartActivity extends Activity {

	/**
	 * Takes care of the Menu
	 */
	private RelativeLayout background;
	private LinearLayout menu;
	//Buttons
	private Button newGameButton;
	private Button helpButton;
	private Button settingsButton;
	private Button highscoreButton;
	private Button back;
	private Button selectLevel;
	private Button localScores;
	private Button globalScores;
	private CheckBox soundsCheckBox;
	private CheckBox musicCheckBox;
	//Texts
	private TextView helpText;
	private TextView highscoreText;
	//Info

	private enum MenuState {
		//menuState possibilities
		onStartMenu,
		onHighscoreMenu,
		onHelpMenu,
		onSettingsMenu
	};
	
	public enum ScoreType {
		//menuState possibilities
		Local,
		Global
	};
	
	private MenuState menuState;
	private MenuState previousMenuState;
	private ScoreType scoreType = ScoreType.Local;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snake_layout);

		//Instantiate layouts
		background = (RelativeLayout) findViewById(R.id.backgroundImage);
		menu = (LinearLayout) findViewById(R.id.menu_buttons);

		//Instantiate buttons
		newGameButton = (Button) findViewById(R.id.snake_new_game_button);
		soundsCheckBox = (CheckBox) findViewById(R.id.sounds_check_box);
		musicCheckBox = (CheckBox) findViewById(R.id.music_check_box);
		helpButton = (Button) findViewById(R.id.snake_help_button);
		settingsButton = (Button) findViewById(R.id.snake_settings_button);
		highscoreButton = (Button) findViewById(R.id.snake_highscore_button);
		localScores = (Button) findViewById(R.id.local_scores_button);
		globalScores = (Button) findViewById(R.id.global_scores_button);
		
		localScores.setSelected(true);

		//Instantiate texts
		helpText = (TextView) findViewById(R.id.helpText);
		highscoreText = (TextView) findViewById(R.id.highscoreText);
		back = (Button) findViewById(R.id.back_button);

		this.selectLevel = (Button) this.findViewById(R.id.select_level_button);
		this.selectLevel.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				ControlResources.make(StartActivity.this, R.id.spelplan);
				Intent gameIntent = new Intent(StartActivity.this, SelectLevelActivity.class);
				StartActivity.this.startActivity(gameIntent);
				overridePendingTransition(0,0);

			}
		});
		
		SettingsSession settingsSession = SettingsSession.getInstance();
		soundsCheckBox.setChecked(settingsSession.soundsEnabled);
		musicCheckBox.setChecked(settingsSession.musicEnabled);

		switchToStartMenu();

		show();
		newGameButton.setOnClickListener(newGameListener);
		helpButton.setOnClickListener(helpListener);
		highscoreButton.setOnClickListener(highscoreListener);
		back.setOnClickListener(backListener);
		settingsButton.setOnClickListener(settingsListener);
		soundsCheckBox.setOnClickListener(soundsCheckBoxListener);
		musicCheckBox.setOnClickListener(musicCheckBoxListener);
		localScores.setOnClickListener(localScoresListener);
		globalScores.setOnClickListener(globalScoresListener);
	}

	public void show() {
		menu.setVisibility(View.VISIBLE);

	}

	public void hide() {
		menu.setVisibility(View.GONE);
		background.setBackgroundColor(0x00000000);
	}

	public void switchToStartMenu() {
		previousMenuState = menuState;
		menuState = MenuState.onStartMenu;
		//These components should be present
		background.setBackgroundResource(R.drawable.snake_bg);
		menu.setVisibility(View.VISIBLE);
		newGameButton.setVisibility(View.VISIBLE);
		helpButton.setVisibility(View.VISIBLE);
		settingsButton.setVisibility(View.VISIBLE);
		highscoreButton.setVisibility(View.VISIBLE);
		this.selectLevel.setVisibility(View.VISIBLE);
		//These shouldn't
		soundsCheckBox.setVisibility(View.GONE);
		musicCheckBox.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		helpText.setVisibility(View.GONE);
		highscoreText.setVisibility(View.GONE);
		localScores.setVisibility(View.GONE);
		globalScores.setVisibility(View.GONE);
	}

	public void switchToHighscoreMenu() {
		previousMenuState = menuState;
		menuState = MenuState.onHighscoreMenu;
		//These components should be present
		menu.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		highscoreText.setVisibility(View.VISIBLE);
		localScores.setVisibility(View.VISIBLE);
		globalScores.setVisibility(View.VISIBLE);
		//These shouldn't
		helpButton.setVisibility(View.GONE);
		settingsButton.setVisibility(View.GONE);
		highscoreButton.setVisibility(View.GONE);
		newGameButton.setVisibility(View.GONE);
		helpText.setVisibility(View.GONE);
		soundsCheckBox.setVisibility(View.GONE);
		musicCheckBox.setVisibility(View.GONE);
		this.selectLevel.setVisibility(View.GONE);
	}

	public void switchToHelpMenu() {
		previousMenuState = menuState;
		menuState = MenuState.onHelpMenu;
		//These components should be present
		menu.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		helpText.setVisibility(View.VISIBLE);
		//These shouldn't
		helpButton.setVisibility(View.GONE);
		settingsButton.setVisibility(View.GONE);
		highscoreButton.setVisibility(View.GONE);
		newGameButton.setVisibility(View.GONE);
		highscoreText.setVisibility(View.GONE);
		localScores.setVisibility(View.GONE);
		globalScores.setVisibility(View.GONE);
		soundsCheckBox.setVisibility(View.GONE);
		musicCheckBox.setVisibility(View.GONE);
		this.selectLevel.setVisibility(View.GONE);
	}
	
	public void switchToSettingsMenu() {
		previousMenuState = menuState;
		menuState = MenuState.onSettingsMenu;
		//These components should be present
		menu.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		soundsCheckBox.setVisibility(View.VISIBLE);
		musicCheckBox.setVisibility(View.VISIBLE);
		//These shouldn't
		helpText.setVisibility(View.GONE);
		helpButton.setVisibility(View.GONE);
		settingsButton.setVisibility(View.GONE);
		highscoreButton.setVisibility(View.GONE);
		newGameButton.setVisibility(View.GONE);
		highscoreText.setVisibility(View.GONE);
		localScores.setVisibility(View.GONE);
		globalScores.setVisibility(View.GONE);
		this.selectLevel.setVisibility(View.GONE);
	}
	
	private OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ControlResources.make(StartActivity.this, R.id.spelplan);
			switch (previousMenuState) {
				case onStartMenu:
					switchToStartMenu();
					break;
				default:
					break;
			}

		}
	};
	private OnClickListener settingsListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ControlResources.make(StartActivity.this, R.id.spelplan);
			switchToSettingsMenu();
			show();
		}
	};
	private View.OnClickListener newGameListener = new View.OnClickListener() {

		public void onClick(View v) {
			// This call most be down after first rend of screen but before some recuses nead this.
			ControlResources.make(StartActivity.this, R.id.spelplan);
			Intent gameIntent = new Intent(StartActivity.this, GameActivity.class);
			StartActivity.this.startActivity(gameIntent);
			overridePendingTransition(0,0);

		}
	};
	private View.OnClickListener helpListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ControlResources.make(StartActivity.this, R.id.spelplan);
			switchToHelpMenu();
			show();
		}
	};
	private View.OnClickListener highscoreListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ControlResources.make(StartActivity.this, R.id.spelplan);
			switchToHighscoreMenu();
			show();
			SetScores(scoreType);
		}
	};
	
	public void SetScores (ScoreType scoreType){
		if (scoreType.equals(ScoreType.Global)){
			ArrayList<DataBaseUser> list;
			try {
				list = ScoreHandler.GetGlobalDataBase();
				StringBuilder str = new StringBuilder("Highscore:");
				short n = 1;
				for(DataBaseUser user : list){
					str.append("\n").append(n).append(". " + user.getName()).append(" - ").append(user.getScore());
					n++;
				}
				highscoreText.setText(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			HighscoreDatabaseIC highData = ControlResources.get().getHighscoreDatabase();
			highscoreText.setText(highData.toString());
		}
	}
	
	private View.OnClickListener musicCheckBoxListener = new OnClickListener() {

		public void onClick(View v) {
			SettingsSession settingsSession = SettingsSession.getInstance();
			CheckBox musicCheckBox = (CheckBox)findViewById(R.id.music_check_box);
			settingsSession.musicEnabled = musicCheckBox.isChecked();
		}
	};
	
	private View.OnClickListener soundsCheckBoxListener = new OnClickListener() {

		public void onClick(View v) {
			SettingsSession settingsSession = SettingsSession.getInstance();
			CheckBox soundsCheckBox = (CheckBox)findViewById(R.id.sounds_check_box);
			settingsSession.soundsEnabled = soundsCheckBox.isChecked();
		}
	};
	
	private View.OnClickListener localScoresListener = new OnClickListener() {

		public void onClick(View v) {
			scoreType = ScoreType.Local;
			localScores.setSelected(true);
			globalScores.setSelected(false);
			SetScores(scoreType);
		}
	};
	
	private View.OnClickListener globalScoresListener = new OnClickListener() {

		public void onClick(View v) {
			scoreType = ScoreType.Global;
			localScores.setSelected(false);
			globalScores.setSelected(true);
			SetScores(scoreType);
		}
	};
	
}
