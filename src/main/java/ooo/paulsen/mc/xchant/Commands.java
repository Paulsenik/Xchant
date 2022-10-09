package ooo.paulsen.mc.xchant;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {

    private static String head = ChatColor.GOLD + "[" + ChatColor.UNDERLINE + ChatColor.DARK_PURPLE + "Xchant" + ChatColor.RESET + ChatColor.GOLD + "]: ";

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0) {
            send(s, ChatColor.AQUA + " > Enchant any item up to lvl 10 <",
                    " Enchant by throwing required items onto Enchantment-Table",
                    " Items Needed for 1 lvl-upgrade:",
                    ChatColor.GOLD + " - 2 Playerheads",
                    ChatColor.GOLD + " - 3 Diamonds",
                    ChatColor.GOLD + " - Enchanted Book with the required entchantment",
                    ChatColor.GOLD + " - Item which receives the enchantment");
            return true;
        } else if (args.length == 1) {
            if (s instanceof Player && s.hasPermission("op") && ((Player) s).getGameMode() == GameMode.CREATIVE) {
                ItemStack item = ((Player) s).getInventory().getItemInMainHand();

                if (item != null && item.getType() != Material.ENCHANTED_BOOK) {

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

                            s.sendMessage("You are trying to add 1 level ontop of " + enchantment.getKey().getKey() + " lvl " + level + " to " + item.getType().name());
                            Xchant.cleverUpEnchant(item, enchantment);

                            return true;
                        }
                    }

                }
            } else {
                send(s, "No valid message");
            }
        }
        return false;
    }

    private void send(CommandSender s, String... message) {
        if (s instanceof Player) {
            s.sendMessage(head + message[0]);
            for (int i = 1; i < message.length; i++)
                s.sendMessage(message[i]);
        } else {
            s.sendMessage(ChatColor.stripColor(head + message[0]));
            for (int i = 1; i < message.length; i++)
                s.sendMessage(ChatColor.stripColor(message[i]));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("op")) {
            for (Enchantment e : Enchantment.values())
                list.add(e.getKey().getKey());
        }
        return filter(list, args[args.length - 1]);

    }

    // returns a list of possible commands according to the already (partly) typed command
    private static List<String> filter(List<String> l, String arg) {
        ArrayList<String> nL = new ArrayList<>();
        for (String s : l) {
            if (s.toLowerCase().contains(arg.toLowerCase()))
                nL.add(s);
        }
        return nL;
    }
}
