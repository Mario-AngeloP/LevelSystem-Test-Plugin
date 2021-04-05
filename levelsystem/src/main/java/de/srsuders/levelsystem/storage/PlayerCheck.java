package de.srsuders.levelsystem.storage;

import java.util.UUID;

import de.srsuders.levelsystem.utils.UUIDUtils;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 05.04.2021
* Project: levelsystem
*/
public class PlayerCheck {

	private final UUID uuid;
	
	public PlayerCheck(final String name) {
		this.uuid = UUIDUtils.getUUID(name);
	}
	
	public PlayerCheck(final UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUUID() {
		return this.uuid;
	}
	
	public boolean existsPlayer() {
		return this.uuid != null;
	}
}
