package de.srsuders.levelsystem.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.srsuders.levelsystem.game.LevelPlayer;
import de.srsuders.levelsystem.storage.Messages;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class LevelPlayerLevelUpEvent extends Event implements Messages {
	private static final HandlerList handlers = new HandlerList();

	private final int currentLvl, oldLvl;
	private final LevelPlayer lp;

	public LevelPlayerLevelUpEvent(final LevelPlayer lp) {
		this.lp = lp;
		this.oldLvl = lp.getLevel() - 1;
		this.currentLvl = lp.getLevel();
		Bukkit.getPlayer(lp.getUUID()).sendMessage(prefix + "§aDu bist jetzt Level " + currentLvl + "!");
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public float getCurrentLvl() {
		return currentLvl;
	}

	public LevelPlayer getLevelPlayer() {
		return lp;
	}

	public float getOldLvl() {
		return oldLvl;
	}
}