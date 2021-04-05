package de.srsuders.levelsystem.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public final class FileManager {
	private static final Logger logger;

	static {
		logger = Logger.getLogger(FileManager.class.getName());
	}

	public static Map<String, String> loadConfig(File file) {
		final Properties config = new Properties();
		try (final InputStream input = new FileInputStream(file.getPath())) {
			config.load(input);
			return collectData(config);
		} catch (FileNotFoundException ex) {
			logger.log(Level.SEVERE, "Es wurde noch keine Datei mit dem namen " + file.getName() + " gefunden.");
		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		}
		return null;
	}

	public static String get(File file, String key) {
		return Objects.requireNonNull(loadConfig(file)).get(key);
	}

	public static File getDataFolder() {
		return new File("plugins" + File.separator + "config");
	}

	public static void writeIntoConfig(File file, Map<String, String> toWrite) {
		try (final FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			final Properties properties = collectProperties(toWrite);
			properties.store(fileOutputStream, "Properties");

		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		}
	}

	private static Properties collectProperties(Map<String, String> toWrite) {
		final Properties properties = new Properties();
		for (String key : toWrite.keySet()) {
			properties.setProperty(key, toWrite.get(key));
		}

		return properties;
	}

	private static Map<String, String> collectData(Properties config) {
		final Map<String, String> propertyMap = new HashMap<>();
		for (String key : config.stringPropertyNames()) {
			propertyMap.put(key, config.getProperty(key));
		}

		return propertyMap;
	}
}