package ru.elementmeteor.menu;

import com.projectkorra.projectkorra.BendingPlayer;
import fr.mrmicky.fastinv.FastInv;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import ru.elementmeteor.ElementMeteor;

import java.util.ArrayList;
import java.util.List;

public class AbilityMenu extends FastInv {

    public AbilityMenu() {
        super(45, "Стихия метеорита");

        final ItemStack stack = new ItemStack(Material.TNT_MINECART);
        stack.editMeta(meta -> {
            meta.setDisplayName(ChatColor.AQUA + "Стихия 'Метеорит'");

            final List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.WHITE + "Нажмите, чтобы получить стихию");
            lore.add("");

            meta.setLore(lore);
        });

        super.setItem(22, stack, e -> {
            final Player player = (Player) e.getWhoClicked();
            final BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
            bPlayer.bindAbility("Meteor");
            player.sendMessage("Готово!");
            player.closeInventory();
        });
    }
}
