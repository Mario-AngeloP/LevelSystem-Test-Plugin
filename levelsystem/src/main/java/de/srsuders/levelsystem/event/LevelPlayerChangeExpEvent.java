package de.srsuders.levelsystem.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.srsuders.levelsystem.game.LevelPlayer;
import de.srsuders.levelsystem.storage.Data;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class LevelPlayerChangeExpEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final float oldExpValue;
	private final LevelPlayer levelPlayer;
	private boolean doLevelUp;
	private float newExpValue;

	public LevelPlayerChangeExpEvent(final LevelPlayer levelPlayer, final float oldExpValue, final float newExpValue) {
		this.oldExpValue = oldExpValue;
		this.newExpValue = newExpValue;
		this.levelPlayer = levelPlayer;
		this.doLevelUp = Data.getInstance().getExpHandler().doesLevelUp(newExpValue, levelPlayer.getLevel());
		if (doLevelUp)
			Data.getInstance().getLevelSystem().getServer().getPluginManager()
					.callEvent(new LevelPlayerLevelUpEvent(levelPlayer));
		levelPlayer.updateExpTask();
	}

	public boolean doesLevelUp() {
		return this.doLevelUp;
	}

	/**
	 * Das verhindert nur, dass er levelUp geht, Nicht das er EXP erhält
	 *
	 * @param value
	 */
	public void cancelLevelUp(boolean value) {
		if (value)
			this.doLevelUp = false;
	}

	/**
	 * Das verhindert das der Spieler EXP erhält
	 *
	 * @param value
	 */
	public void cancelExpGain(boolean value) {
		if (value) {
			this.newExpValue = this.oldExpValue;
			this.doLevelUp = false;
		}
	}

	/**
	 * Returnt den Hinzugefügten/Abgezogenen/Nicht veränderten Wert
	 */
	public float getDiffrenceExpLevel() {
		return this.newExpValue - oldExpValue;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public LevelPlayer getLevelPlayer() {
		return this.levelPlayer;
	}

	public float getNewExpValue() {
		return this.newExpValue;
	}

	public void setNewExpValue(float expValue) {
		this.newExpValue = expValue;
	}

	public float getOldExpValue() {
		return this.oldExpValue;
	}
}
