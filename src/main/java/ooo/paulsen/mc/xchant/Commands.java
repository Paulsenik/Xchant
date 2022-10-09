package ooo.paulsen.mc.xchant;

import org.bukkit.ChatColor;
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
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0) {
            send(s, "\n/xchant [enchantment] : see whats required to level up item in hand");
            return true;
        } else if (args.length == 1) {
            if (s instanceof Player) {
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
            }
        }
        return false;
    }

    private void send(CommandSender s, String message) {
        if (s instanceof Player)
            s.sendMessage("[Xchant] :: " + message);
        else
            s.sendMessage(ChatColor.stripColor("[Xchant] :: " + message));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1) {
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
