package ru.elementmeteor.meteor;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.util.TempBlock;
import lombok.AllArgsConstructor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import ru.elementmeteor.ElementMeteor;

import java.util.Collection;

public class MeteorManager {

    private final ElementMeteor plugin;

    public MeteorManager(ElementMeteor plugin) {
        this.plugin = plugin;
    }

    public void launch(Location location, int uses) {
        final Fireball fireball = location.getWorld().spawn(location, Fireball.class);
        fireball.setYield(0);
        fireball.setVelocity(location.getDirection().toBlockVector());
        fireball.getPersistentDataContainer().set(ElementMeteor.KEY_METEOR, PersistentDataType.BYTE, (byte) 1);
        fireball.getPersistentDataContainer().set(ElementMeteor.KEY_METEOR_USES, PersistentDataType.INTEGER, uses);
    }

    public void explosion(Location location, int uses) {
        final World world = location.getWorld();

        if (uses == 0 || uses == 1) uses = 2; // Чтобы первый запуск что-то делал (без проверки первый запуск ничего не делал)
        if (uses <= 80) uses = 80; // Чтобы челики не делали миллионые прокачки и взрывали пол мира.

        final int radius = 10 + uses / 9;
        final double explosionPower = 1.3;

        for (int i = 0; i < 5; i++) {
            world.strikeLightning(location);
            world.spawnParticle(Particle.EXPLOSION_HUGE, location, 1);
            world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 2f, 1f);
        }

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    final Location target = location.clone().add(x, y, z);
                    final double distance = location.distance(target);
                    final double chance = (explosionPower * radius - distance) / radius;

                    if (chance > Math.random()) {
                        final Block block = target.getBlock();

                        if (block.getType() == Material.AIR) continue;
                        final TempBlock temp = new TempBlock(block, Material.AIR);
                        Bukkit.getScheduler().runTaskLater(plugin, temp::revertBlock, 3 * 20L);

                        final Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(
                                location, radius, radius, radius
                        );

                        nearbyEntities.forEach(entity -> entity.setVelocity(new Vector(0, 1.5, 0)));
                    }
                }
            }
        }
    }


}

