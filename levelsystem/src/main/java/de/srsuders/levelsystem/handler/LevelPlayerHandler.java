package de.srsuders.levelsystem.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.srsuders.levelsystem.game.LevelPlayer;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class LevelPlayerHandler {

	private static final Map<UUID, LevelPlayer> lp = new HashMap<>();

	private LevelPlayerHandler() {}

	public static LevelPlayer getLevelPlayer(final UUID uuid) {
		LevelPlayer lvlp;
		if (!lp.containsKey(uuid)) {
			lvlp = new LevelPlayer(uuid);
			lp.put(uuid, lvlp);
		} else {
			lvlp = lp.get(uuid);
		}
		return lvlp;
	}

	public static LevelPlayer getLevelPlayer(final Player player) {
		return getLevelPlayer(player.getUniqueId());
	}
}
