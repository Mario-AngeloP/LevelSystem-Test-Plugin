package de.srsuders.levelsystem.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.srsuders.levelsystem.LevelSystem;
import de.srsuders.levelsystem.handler.ExpHandler;
import de.srsuders.levelsystem.io.sql.SQLManager;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 * Singleton Muster nur eine Instanz
 */
public class Data {

	private static Data instance;
	private final LevelSystem ls;
	private final SQLManager sql;
	private final ExpHandler expHandler;
	private final Map<UUID, String> cache;
	
	private Data() {
		this.cache = new HashMap<>();
		this.ls = new LevelSystem();
		this.sql = new SQLManager();
		this.expHandler = new ExpHandler();
	}

	public ExpHandler getExpHandler() {
		return this.expHandler;
	}

	public LevelSystem getLevelSystem() {
		return this.ls;
	}

	public SQLManager getSQL() {
		return this.sql;
	}

	public static Data getInstance() {
		if (instance == null) {
			instance = new Data();
		}
		return instance;
	}

	public Map<UUID, String> getCache() {
		return this.cache;
	}
}
