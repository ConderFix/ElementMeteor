package ru.elementmeteor;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static void load(FileConfiguration config) {
        parseMariaDB(config);
    }

    public static class MARIADB {
        public static String url, user, password;
        public static int port;
    }

    private static void parseMariaDB(Configuration config) {
        final ConfigurationSection section = config.getConfigurationSection("mariadb");

        MARIADB.url = section.getString("url");
        MARIADB.user = section.getString("user");
        MARIADB.password = section.getString("password");
    }
}
