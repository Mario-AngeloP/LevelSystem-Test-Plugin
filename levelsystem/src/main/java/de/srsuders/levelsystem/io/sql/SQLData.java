package de.srsuders.levelsystem.io.sql;

import java.sql.Connection;
import de.srsuders.levelsystem.storage.Data;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class SQLData {

	private final String password, url, username;

	public SQLData(String host, short port, String username, String password, String database) {
		if (!host.contains("/") || !host.contains(":")) {
			this.url = "jdbc:mysql://" + host + ":" + port + "/" + database
					+ "?useSSL=false&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8";
			this.username = username;
			this.password = password;
		} else {
			throw new IllegalArgumentException("Dont use chars like / or :");
		}
	}

	public Connection getConnection() {
		return Data.getInstance().getSQL().getConnection();
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}
}