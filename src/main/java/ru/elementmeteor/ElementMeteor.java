package ru.elementmeteor;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.util.AddonAbilityLoader;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.elementmeteor.commands.ElementMeteorCommand;
import ru.elementmeteor.menu.AbilityMenu;
import ru.elementmeteor.meteor.MeteorManager;
import ru.elementmeteor.meteor.listeners.MeteorExplosionListener;
import ru.elementmeteor.meteor.listeners.MeteorLaunchListener;
import ru.elementmeteor.storage.MariaDBStorage;
import ru.elementmeteor.storage.Storage;

public class ElementMeteor extends JavaPlugin {

    public static final NamespacedKey KEY_METEOR = new NamespacedKey("elementmeteor", "meteor");
    public static final NamespacedKey KEY_METEOR_USES = new NamespacedKey("elementmeteor", "uses");
    public static MeteorManager meteorManager;
    public static Storage storage;
    public static AbilityMenu abilityMenu;

    @Override
    public void onEnable() {
        meteorManager = new MeteorManager(this);
        abilityMenu = new AbilityMenu();

        super.saveDefaultConfig();
        Config.load(super.getConfig());

        storage = new MariaDBStorage();
        storage.createTable();

        LiteBukkitFactory.builder("elementmeteor")
                .commands(new ElementMeteorCommand())
                .build();

        FastInvManager.register(this);

        CoreAbility.registerPluginAbilities(this, "ru.elementmeteor.abilities");

        final PluginManager pluginManager = super.getServer().getPluginManager();
        pluginManager.registerEvents(new MeteorLaunchListener(this), this);
        pluginManager.registerEvents(new MeteorExplosionListener(), this);
    }

    @Override
    public void onDisable() {
        if (storage != null) {
            storage.unload();
        }
    }

}
