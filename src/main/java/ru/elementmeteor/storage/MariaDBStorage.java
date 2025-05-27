package ru.elementmeteor.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.elementmeteor.Config;
import ru.elementmeteor.data.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MariaDBStorage implements Storage {

    private final HikariDataSource dataSource;
    private final ExecutorService dbExecutor = Executors.newFixedThreadPool(2);

    public MariaDBStorage() {
        final HikariConfig config = new HikariConfig();

        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl(Config.MARIADB.url);
        config.setUsername(Config.MARIADB.user);
        config.setPassword(Config.MARIADB.password);
        config.setMaximumPoolSize(3);
        dataSource = new HikariDataSource(config);
    }

    @Override
    public CompletableFuture<User> getUser(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "SELECT uses FROM uses WHERE name = ?")) {
                statement.setString(1, name);

                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        int uses = result.getInt("uses");
                        return new User(name, uses);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, dbExecutor);
    }

    @Override
    public CompletableFuture<Integer> getUses(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "SELECT uses FROM uses WHERE name = ?")) {
                statement.setString(1, name);

                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return result.getInt("uses");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }, dbExecutor);
    }

    @Override
    public void addUses(String name) {
        dbExecutor.execute(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE uses SET uses = uses + 1 WHERE name = ?")) {
                statement.setString(1, name);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void createUser(String name) {
        dbExecutor.execute(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO uses (name, uses) VALUES (?, 0)")) {
                statement.setString(1, name);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void createTable() {
        dbExecutor.execute(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "CREATE TABLE IF NOT EXISTS uses (" +
                                 "name VARCHAR(36) PRIMARY KEY," +
                                 "uses INT NOT NULL DEFAULT 0)")) {
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void unload() {
        dataSource.close();
        dbExecutor.shutdown();
    }
}
