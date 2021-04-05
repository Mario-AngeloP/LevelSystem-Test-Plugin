package de.srsuders.levelsystem.game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.srsuders.levelsystem.event.LevelPlayerChangeExpEvent;
import de.srsuders.levelsystem.io.sql.SQLManager;
import de.srsuders.levelsystem.storage.Const;
import de.srsuders.levelsystem.storage.Data;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class LevelPlayer {

	private final UUID uuid;
	private final SQLManager sql;
	private Integer lvl;
	private Long exp;

	public LevelPlayer(final UUID uuid) {
		this.uuid = uuid;
		this.sql = Data.getInstance().getSQL();
		final ResultSet rs = sql.query("SELECT * FROM " + Const.PLAYERTABLE + " WHERE uuid='" + uuid + "'");
		try {
			if (!rs.next()) {
				this.lvl = Data.getInstance().getExpHandler().getFirstLevel();
				this.exp = 0L;
				sql.updateAsync(
						"INSERT INTO " + Const.PLAYERTABLE + " (uuid, exp) VALUES ('" + uuid + "', '" + exp + "')");
			} else {
				this.exp = Long.valueOf(rs.getString("exp"));
				this.lvl = Data.getInstance().getExpHandler().detectLevelByExp(this.exp);
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
	}

	public void updateExpTask() {
		final Player player = Bukkit.getPlayer(uuid);
		if (player != null) {
			player.setLevel(lvl);
			if(this.lvl == Data.getInstance().getExpHandler().getLastLevel()) {
				player.setExp(0.99F);
				return;
			}
			final double result = ((this.exp * 100) / Data.getInstance().getExpHandler().getExpOfLevel(lvl)) / 100;
			if (result < 100)
				player.setExp((float) result);
			
		}
	}

	/**
	 * Fügt Exp hinzu und wenn ein neues Level erreicht werden würde, wird das
	 * LevelUpEvent aufgerufen.
	 * 
	 * @param exp
	 */
	public void addExp(long exp) {
		this.exp += exp;
		if (Data.getInstance().getExpHandler().doesLevelUp(this.exp, lvl))
			this.lvl++;
		Data.getInstance().getLevelSystem().getServer().getPluginManager()
				.callEvent(new LevelPlayerChangeExpEvent(this, this.exp - exp, this.exp));
		updateExpTask();
	}

	/**
	 * Entfernt gewisse Anzahl an Exp des Spielers, jedoch geht nicht unter des
	 * aktuellen Levels
	 * 
	 * @param exp
	 */
	public void removeExp(long exp) {
		final long oldExp = this.exp;
		if(lvl == 1 & (this.exp - exp) < 0) {
			this.exp = 0L;
			Data.getInstance().getLevelSystem().getServer().getPluginManager()
			.callEvent(new LevelPlayerChangeExpEvent(this, oldExp, this.exp));
			return;
		}
		final long lowerExp = Data.getInstance().getExpHandler().getExpOfLevel(lvl - 1);
		if ((this.exp - exp) < lowerExp) {
			this.exp = lowerExp;
		} else {
			this.exp -= exp;
		}
		Data.getInstance().getLevelSystem().getServer().getPluginManager()
				.callEvent(new LevelPlayerChangeExpEvent(this, oldExp, this.exp));
	}

	public void savePlayer() {
		sql.updateAsync("UPDATE " + Const.PLAYERTABLE + " SET exp='" + exp + "' WHERE uuid='" + this.uuid + "'");
	}

	public Integer getLevel() {
		return this.lvl;
	}

	public Long getExp() {
		return this.exp;
	}

	public UUID getUUID() {
		return this.uuid;
	}
}
