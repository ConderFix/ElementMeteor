package ru.elementmeteor.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Sender;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.elementmeteor.ElementMeteor;
import ru.elementmeteor.menu.AbilityMenu;

@Command(name = "elementmeteor")
@Permission("elementmeteor.menu")
public class ElementMeteorCommand {

    @Execute
    void command(@Sender CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Команда для игроков.");
            return;
        }

        final Player player = (Player) sender;
        new AbilityMenu().open(player);
    }
}
