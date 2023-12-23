package de.paulsenik.mc.xchant.commands;

import de.paulsenik.mc.xchant.Xchant;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class XchantCommand extends CommandEssentials implements
    CommandExecutor, TabCompleter {

  public static String head =
      ChatColor.GOLD + "[" + ChatColor.UNDERLINE + ChatColor.DARK_PURPLE + "Xchant"
          + ChatColor.RESET + ChatColor.GOLD + "]: ";

  @Override
  public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
    if (args.length == 1) {
      if (s instanceof Player && s.hasPermission("op")
          && ((Player) s).getGameMode() == GameMode.CREATIVE) {
        ItemStack item = ((Player) s).getInventory().getItemInMainHand();

        if (item.getType() != Material.ENCHANTED_BOOK) {

          Enchantment enchantment = null;

          for (Enchantment e : Enchantment.values()) {
            if (e.getKey().getKey().contentEquals(args[0])) {
              enchantment = e;
              break;
            }
          }

          if (enchantment != null && item.getItemMeta() != null) {
            int level = item.getItemMeta().getEnchantLevel(enchantment);

            if (item.getItemMeta().getEnchants().containsKey(enchantment)) {

              s.sendMessage(
                  "You are trying to add 1 level ontop of " + enchantment.getKey().getKey()
                      + " lvl " + level + " to " + item.getType().name());
              Xchant.cleverUpEnchant(item, enchantment);

              return true;
            }
          }

        }
      } else {
        CommandEssentials.send(s, "No valid message");
      }
    }
    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String label,
      String[] args) {
    ArrayList<String> list = new ArrayList<>();
    if (args.length == 1 && sender.hasPermission("op")) {
      for (Enchantment e : Enchantment.values()) {
        list.add(e.getKey().getKey());
      }
    }
    return CommandEssentials.filter(list, args[args.length - 1], false);
  }
}
