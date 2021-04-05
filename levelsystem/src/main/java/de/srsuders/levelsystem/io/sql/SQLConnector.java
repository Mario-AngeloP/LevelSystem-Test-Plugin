package de.srsuders.levelsystem.io.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

import de.srsuders.levelsystem.io.FileManager;
import de.srsuders.levelsystem.storage.Const;
import de.srsuders.levelsystem.storage.Data;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class SQLConnector {
	private final Logger logger = Logger.getLogger(getClass().getName());
	private Connection connection;
	private final SQLData sqlData;
	private final ExecutorService executor;

	SQLConnector() throws NumberFormatException {
		this.executor = Executors.newCachedThreadPool();
		final Map<String, String> sqlConfig = FileManager.loadConfig(Const.SQL_CONFIG);
		final short port = Short.parseShort(Objects.requireNonNull(sqlConfig).get("port"));
		final String hostname = sqlConfig.get("host");
		final String username = sqlConfig.get("username");
		final String password = sqlConfig.get("password");
		final String database = sqlConfig.get("database");
		this.sqlData = new SQLData(hostname, port, username, password, database);
	}

	public void updateAsync(PreparedStatement statement) {
		executor.execute(() -> update(statement));
	}

	public void updateAsync(String statement) {
		executor.execute(() -> update(statement));
	}

	public void queryAsync(PreparedStatement statement, Consumer<ResultSet> result) {
		executor.execute(() -> {
			final ResultSet rs = query(statement);
			Bukkit.getScheduler().runTaskAsynchronously(Data.getInstance().getLevelSystem(), () -> result.accept(rs));
		});
	}

	public void queryAsync(String statement, Consumer<ResultSet> result) {
		executor.execute(() -> {
			final ResultSet rs = query(statement);
			Bukkit.getScheduler().runTaskAsynchronously(Data.getInstance().getLevelSystem(), () -> result.accept(rs));
		});
	}

	public void update(String statement) {
		evaluateConnection();
		try (final PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
			update(preparedStatement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void update(PreparedStatement statement) {
		try {
			evaluateConnection();
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ResultSet query(String statement) {
		try {
			evaluateConnection();
			return query(connection.prepareStatement(statement));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private ResultSet query(PreparedStatement statement) {
		try {
			evaluateConnection();
			return statement.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(sqlData.getUrl(), sqlData.getUsername(),
					sqlData.getPassword());
		} catch (ClassNotFoundException | SQLException ex) {
			getLogger().log(Level.SEVERE, "Die MySQL Verbindung konnte nicht aufgebaut werden.", ex);
		}
	}

	private void evaluateConnection() {
		try {
			if (((Statement) sqlData).getConnection() == null || !((Statement) sqlData).getConnection().isValid(10)
					|| ((Statement) sqlData).getConnection().isClosed())
				connect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			if (((Statement) sqlData).getConnection() != null) {
				((Statement) sqlData).getConnection().close();
			}
		} catch (final SQLException ex) {
			getLogger().log(Level.SEVERE, "Die MySQL Verbindung konnte nicht aufgebaut werden.", ex);
		}
	}

	Connection getConnection() {
		return connection;
	}

	private Logger getLogger() {
		return logger;
	}
}