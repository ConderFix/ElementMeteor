package ru.elementmeteor;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static void load(FileConfiguration config) {
        parseMariaDB(config);
    }

    public static class MARIADB {
        public static String host, user, password, database;
        public static int port;
    }

    private static void parseMariaDB(Configuration config) {
        final ConfigurationSection section = config.getConfigurationSection("mariadb");

        MARIADB.host = section.getString("host");
        MARIADB.user = section.getString("user");
        MARIADB.password = section.getString("password");
        MARIADB.database = section.getString("database");
        MARIADB.port = section.getInt("port");
    }
}
