package de.srsuders.levelsystem.main;

import org.bukkit.plugin.java.JavaPlugin;

import de.srsuders.levelsystem.command.LevelCommand;
import de.srsuders.levelsystem.listener.PlayerListener;
import de.srsuders.levelsystem.storage.Const;
import de.srsuders.levelsystem.storage.Data;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class LevelSystem extends JavaPlugin {

	public void onEnable() {
		Data.getInstance().setLevelSystemInstance(this);
		Data.getInstance().getSQL().updateAsync(
				"CREATE TABLE IF NOT EXISTS " + Const.PLAYERTABLE + " (uuid VARCHAR(64), exp VARCHAR(64))");
		this.getServer().getPluginCommand("level").setExecutor(new LevelCommand());
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}
}
