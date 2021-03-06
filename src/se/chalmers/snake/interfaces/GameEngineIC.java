package se.chalmers.snake.interfaces;

import se.chalmers.snake.interfaces.util.REPoint;
import java.util.List;
import se.chalmers.snake.interfaces.util.XYPoint;
import se.chalmers.snake.sound.SoundPlayer;
import se.chalmers.snake.util.EnumObservableInterface;
import se.chalmers.snake.util.EnumObserver;

/**
 * This is the main interface for the GameEngine for the Snake Game.
 */
public interface GameEngineIC extends EnumObservableInterface<GameEngineIC.GameEngineEvent, Void, Void> {

	/**
	 * This hold event the GameEngine can send to the Observer.
	 */
	public static enum GameEngineEvent {

		/**
		 * A new game has be load into the game.
		 */
		NEW_GAME,
		/**
		 * The game has restart.
		 */
		RESTART_GAME,
		/**
		 * The game has start or resume from a pause stat.
		 */
		START_GAME,
		/**
		 * The game has be pause and will not do create a UPDATE event unto the START_GAME has be send.
		 */
		PAUSE_GAME,
		/**
		 * A new update of the {@link GameEngineIC#getPlayerBody()} or/and {@link GameEngineIC#getItems()} exist.
		 */
		UPDATE,
		/**
		 * This will be send while the level is end, the player has get the goal of this level.
		 */
		LEVEL_END,
		/**
		 * This will be send while the player is lose the game.
		 */
		PLAYER_LOSE
	}

	public static enum GameEngineStatus {

		NO_LEVEL_LOAD,
		NEW_LEVEL,
		RUNNING,
		PAUSE,
		LEVEL_END
	}

	public void SetSoundPlayer(SoundPlayer sp);

	/**
	 * Call this method for start a pause game or
	 * start a new game that has start but wait on redy.
	 * @return True if this action are allow and a load level exist.
	 */
	public boolean startGame();

	/**
	 * Call this method for pause a game.
	 * This most will be call on exit of the app to close recsusces that gameEngine has open or start.
	 * @return True if this action are allow and a load level exist.
	 */
	public boolean pauseGame();

	/**
	 * Return the current status of the game if the game is run or not.
	 * @return Return true if the game is running.
	 */
	public boolean isRun();

	/**
	 * Call this method for restart a load game.
	 * This will be sam as call GameEngineIC.loadLevel(GameEngineIC.getLevelName());
	 * @return True if this action are allow and a load level exist.
	 */
	public boolean restartGame();

	/**
	 * Return the current status of the GameEngine
	 * @return 
	 */
	public GameEngineStatus getStatus();

	/**
	 * Load select level, this will reset the GameEngine to default state.
	 * @param level The level to be load,
	 * @return If the level exist and can be load.
	 */
	public boolean loadLevel(String name);

	/**
	 * Test if a level has be load into the GameEngine
	 */
	public boolean isLevelLoad();

	/**
	 * Return the current state of points the use haves.
	 * @return 
	 */
	public int getScore();

	/**
	 * Return the current level the use is on.
	 * @return Return the level or null if no level is load.
	 */
	public String getLevelName();

	/**
	 * Add a listened to receive events from the GameEngine about current game state.
	 * A modul can listened to one of the events in the {@link GameEngineIC.GameEngineEvent}
	 * {@inheritDoc}
	 * @param key
	 * @param observer
	 * @return 
	 */
	@Override
	public boolean addObserver(GameEngineIC.GameEngineEvent key, EnumObserver<GameEngineIC.GameEngineEvent, Void, Void> observer);

	/**
	 * Return the Game Filed Size
	 * @return 
	 */
	public XYPoint getGameFieldSize();

	/**
	 * This will return a list of all segments that define the players body.
	 * The list will be in the logical order of the player body, with head frist and tail last of the list.
	 * @return Return a list of GamePoints, or a empty list if no player body exist.
	 */
	public List<REPoint> getPlayerBody();

	/**
	 * This will return the first point in the player body call head.
	 * @return Return a REPoint or null in case the player body not exist.
	 */
	public REPoint getPlayerHead();

	/**
	 * This will return a list of all item the player will try to collect in the game.
	 * @return 
	 */
	public List<REPoint> getItems();

	/**
	 * Return the size of each item will have.
	 * @return 
	 */
	public int getItemRadius();

	/**
	 * This will return a list of all walls in the game or other static item,
	 * that never will be modify in a running game.
	 * And no point to be read more that in the beginning of each level.
	 * @return 
	 */
	public List<REPoint> getObstacles();

	/**
	 * Get the Level's metadata, contains, original elements data, image data and more.
	 * 
	 * @return  Return the a level, or null if no level are load.
	 */
	public LevelIC getLevelMetaData();

	public void setStartScore(int score);
}
