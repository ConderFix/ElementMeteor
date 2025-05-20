package ru.elementmeteor.abilities;

import com.projectkorra.projectkorra.ability.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MeteorAbility extends FireAbility {

    private Location location;

    public MeteorAbility(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        location = player.getEyeLocation().clone();

        bPlayer.addCooldown(this);

        start();
    }

    @Override
    public void progress() {}

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return 10 * 5;
    }

    @Override
    public String getName() {
        return "Meteor";
    }

    @Override
    public Location getLocation() {
        return location;
    }

}
