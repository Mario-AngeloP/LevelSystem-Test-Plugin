package de.srsuders.levelsystem.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import de.srsuders.levelsystem.game.LevelPlayer;
import de.srsuders.levelsystem.handler.LevelPlayerHandler;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		final LevelPlayer lp = LevelPlayerHandler.getLevelPlayer(p);
		lp.updateExpTask();
	}
	
	@EventHandler
	public void onDeath(final PlayerDeathEvent e) {
		final Player player = e.getEntity();
	}
}
