package de.srsuders.levelsystem.game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.srsuders.levelsystem.event.LevelPlayerChangeExpEvent;
import de.srsuders.levelsystem.event.LevelPlayerLevelUpEvent;
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

	@SuppressWarnings("deprecation")
	public void updateExpTask() {
		final Player player = Bukkit.getPlayer(uuid);
		if (player != null) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Data.getInstance().getLevelSystem(), new Runnable() {
				@Override
				public void run() {
					player.setLevel(lvl);
				}
			}, 1L);
			if (this.lvl == Data.getInstance().getExpHandler().getLastLevel())
				return;
			final float lastLvlExp = this.lvl == 1 ? 0 : Data.getInstance().getExpHandler().getExpOfLevel(lvl - 1);
			final float result = ((((float) this.exp) - lastLvlExp)
					/ ((float) Data.getInstance().getExpHandler().getExpOfLevel(lvl) - (float) lastLvlExp));
			if (result >= 1)
				player.setExp(0.99F);
			else
				player.setExp(result);
		}
	}

	/**
	 * Fügt Exp hinzu und wenn ein neues Level erreicht werden würde, wird das
	 * LevelUpEvent aufgerufen.
	 * 
	 * @param exp
	 */
	public void addExp(long exp) {
		if (this.lvl == Data.getInstance().getExpHandler().getLastLevel())
			return;
		this.exp += exp;
		int oldLvl = this.lvl;
		if (Data.getInstance().getExpHandler().doesLevelUp(this.exp, lvl)) {
			while (doesLevelUp()) {
				this.lvl++;
				Data.getInstance().getLevelSystem().getServer().getPluginManager()
						.callEvent(new LevelPlayerLevelUpEvent(this));
				if (this.lvl == Data.getInstance().getExpHandler().getLastLevel()) {
					this.exp = Data.getInstance().getExpHandler().getExpOfLevel(lvl);
					break;
				}
			}
		}
		Data.getInstance().getLevelSystem().getServer().getPluginManager()
				.callEvent(new LevelPlayerChangeExpEvent(this, this.exp - exp, this.exp, oldLvl != this.lvl));
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
		if (lvl == 1 & (this.exp - exp) < 0) {
			this.exp = 0L;
			Data.getInstance().getLevelSystem().getServer().getPluginManager()
					.callEvent(new LevelPlayerChangeExpEvent(this, oldExp, this.exp, false));
			return;
		}
		final long newExp = this.exp -= exp;
		final long lowerExp = Data.getInstance().getExpHandler().getExpOfLevel(lvl - 1);
		if (newExp < lowerExp) {
			this.exp = lowerExp;
		} else {
			this.exp -= exp;
		}
		Data.getInstance().getLevelSystem().getServer().getPluginManager()
				.callEvent(new LevelPlayerChangeExpEvent(this, oldExp, this.exp, false));
		updateExpTask();
	}

	private boolean doesLevelUp() {
		return Data.getInstance().getExpHandler().getExpOfLevel(this.lvl) <= this.exp;
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
