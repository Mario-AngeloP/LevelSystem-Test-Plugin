package de.srsuders.levelsystem.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import de.srsuders.levelsystem.game.LevelPlayer;
import de.srsuders.levelsystem.handler.LevelPlayerHandler;
import de.srsuders.levelsystem.storage.Data;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		if(!Data.getInstance().getCache().containsKey(p.getUniqueId())) 
			Data.getInstance().getCache().put(p.getUniqueId(), p.getName());
		final LevelPlayer lp = LevelPlayerHandler.getLevelPlayer(p);

		lp.updateExpTask();
	}

	// Entfernt 5% Exp des Spielers
	@EventHandler
	public void onDeath(final PlayerDeathEvent e) {
		final Player p = e.getEntity();
		final LevelPlayer lp = LevelPlayerHandler.getLevelPlayer(p);
		final long l = (long) ((Data.getInstance().getExpHandler().getExpOfLevel(lp.getLevel())
				- Data.getInstance().getExpHandler().getExpOfLevel(lp.getLevel() - 1)) * 0.05);
		lp.removeExp(l);
	}
}
