package de.srsuders.levelsystem.storage;

import java.io.File;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class Const {

	// Files
	public static final File SQL_CONFIG = new File(
			"plugins" + File.separator + "LevelSystem" + File.separator + "config" + File.separator + "sql.properties");
	public static final File EXP_FILE = new File("plugins" + File.separator + "LevelSystem" + File.separator + "config"
			+ File.separator + "expList.properties");

	// Tables
	public static final String PLAYERTABLE = "srsuderstestdb.playertable";

}
