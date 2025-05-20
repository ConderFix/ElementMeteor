package ru.elementmeteor.meteor.listeners;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.elementmeteor.ElementMeteor;
import ru.elementmeteor.abilities.MeteorAbility;
import ru.elementmeteor.data.User;
import ru.elementmeteor.meteor.MeteorManager;
import ru.elementmeteor.storage.Storage;

public class MeteorLaunchListener implements Listener {

    private final ElementMeteor plugin;

    private final Storage storage = ElementMeteor.storage;

    public MeteorLaunchListener(ElementMeteor plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void on(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        final Player player = event.getPlayer();
        final BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        final String name = player.getName();

        if (!bPlayer.getBoundAbilityName().equals("Meteor")) return;

        storage.getUser(name).thenCompose(user -> {
            if (user != null) {
                storage.addUses(name);
                return storage.getUses(name);
            } else {
                storage.createUser(name);
                storage.addUses(name);
                return storage.getUses(name);
            }
        }).thenAccept(uses -> Bukkit.getScheduler().runTask(plugin, () ->
                ElementMeteor.meteorManager.launch(player.getEyeLocation(), uses)));

    }

}
