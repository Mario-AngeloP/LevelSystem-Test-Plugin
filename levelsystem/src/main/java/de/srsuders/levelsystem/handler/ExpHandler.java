package de.srsuders.levelsystem.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.srsuders.levelsystem.io.FileManager;
import de.srsuders.levelsystem.storage.Const;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class ExpHandler {

	private final Map<Integer, Long> expMap = new HashMap<>();

	public ExpHandler() {
		for (Entry<String, String> entry : FileManager.loadConfig(Const.EXP_FILE).entrySet()) {
			expMap.put(Integer.valueOf(entry.getKey()), Long.valueOf(entry.getValue()));
		}
	}

	/**
	 * Gibt das Level wieder, welches man mit der Exp hat Bsp: Man ist lvl 10 und
	 * man benötigt insgesamt 13500 exp, jedoch
	 * 
	 * @param exp
	 * @return
	 */
	public Integer detectLevelByExp(long exp) {
		int lvl = 0;
		for (Long expValue : expMap.values()) {
			lvl++;
			if (expValue > exp)
				return lvl;
		}
		return 0;
	}

	public Integer getFirstLevel() {
		return (Integer) expMap.keySet().toArray()[0];
	}

	public Integer getLastLevel() {
		return (Integer) expMap.keySet().toArray()[expMap.keySet().toArray().length - 1] - 1;
	}

	/**
	 * Gibt die benötigte Exp wieder für den nächsten Level Up wieder
	 * 
	 * @param lvl
	 * @return
	 */
	public Long getExpOfLevel(Integer lvl) {
		return expMap.get(lvl);
	}

	/**
	 * Überprüft ob es ein Level Up sein würde
	 * 
	 * @param newExpValue
	 * @param level
	 * @return
	 */
	public boolean doesLevelUp(float newExpValue, Integer level) {
		return getExpOfLevel(level) <= newExpValue;
	}
}
