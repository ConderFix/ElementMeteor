package ru.elementmeteor.storage;

import ru.elementmeteor.data.User;
import java.util.concurrent.CompletableFuture;

public interface Storage {
    CompletableFuture<User> getUser(String name);
    CompletableFuture<Integer> getUses(String name);
    void addUses(String name);
    void createUser(String name);
    void createTable();
    void unload();
}
