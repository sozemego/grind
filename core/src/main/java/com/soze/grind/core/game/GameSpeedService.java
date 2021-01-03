package com.soze.grind.core.game;

import com.badlogic.gdx.math.MathUtils;
import org.springframework.stereotype.Service;

/**
 * Holds the data about game speed or pause/unpaused state.
 */
@Service
public class GameSpeedService {

	private int gameSpeed = 1;
	private boolean paused = false;

	public int getGameSpeed() {
		return gameSpeed;
	}

	public void incrementGameSpeed() {
		this.gameSpeed++;
	}

	public void decrementGameSpeed() {
		this.gameSpeed = MathUtils.clamp(--this.gameSpeed, 1, 500);
	}

	public boolean isPaused() {
		return paused;
	}

	public void togglePause() {
		this.paused = !this.paused;
	}

}
