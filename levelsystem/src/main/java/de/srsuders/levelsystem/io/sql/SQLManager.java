package de.srsuders.levelsystem.io.sql;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class SQLManager extends SQLConnector {

	@Override
	public void disconnect() {
		updateOnStop();
		super.disconnect();
	}

	public void updateOnStart() {

	}

	private void updateOnStop() {

	}
}