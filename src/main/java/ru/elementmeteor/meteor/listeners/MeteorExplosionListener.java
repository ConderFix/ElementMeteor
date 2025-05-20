package ru.elementmeteor.meteor.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;
import ru.elementmeteor.ElementMeteor;

import javax.swing.text.Element;

public class MeteorExplosionListener implements Listener {

    @EventHandler
    private void on(ProjectileHitEvent event) {
        final Projectile projectile = event.getEntity();

        if (!(projectile instanceof Fireball)) return;
        if (!projectile.getPersistentDataContainer().has(ElementMeteor.KEY_METEOR, PersistentDataType.BYTE)) return;

        final int uses = projectile.getPersistentDataContainer().get(ElementMeteor.KEY_METEOR_USES, PersistentDataType.INTEGER);

        ElementMeteor.meteorManager.explosion(projectile.getLocation(), uses);
    }

}
