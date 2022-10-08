package ooo.paulsen.mc.xchant;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class Xchant extends JavaPlugin {

    public static Xchant instance;

    public static Logger l;

    @Override
    public void onEnable() {
        instance = this;
        l = getLogger();
        load();

        getServer().getPluginManager().registerEvents(new EnchantEvent(), this);
        getCommand("xchant").setExecutor(new Commands());
        getCommand("xchant").setTabCompleter(new Commands());
    }

    @Override
    public void onDisable() {
        save();
    }

    public void load() {

    }

    public void save() {

    }

    /*
        sets Echantmentlvl of item 1 higher and for books the new stored-lvl.
     */
    public static void cleverUpEnchant(ItemStack item, Enchantment enchant) {
        if (item.getType() == Material.ENCHANTED_BOOK) {

            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            int lvl = meta.getStoredEnchantLevel(Enchantment.DAMAGE_ALL);

            meta.addStoredEnchant(Enchantment.DAMAGE_ALL, lvl + 1, true);
            item.setItemMeta(meta);

        } else {

            ItemMeta meta = item.getItemMeta();
            int lvl = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);

            meta.addEnchant(Enchantment.DAMAGE_ALL, lvl + 1, true);
            item.setItemMeta(meta);

        }
    }

    /**
     * @return Entitys to delete after ritual; Null - if the ritual requirement is not satisfied
     */
    public static List<Entity> nearbyContain(List<Entity> list) {

        ArrayList<Entity> nearbyValueables = new ArrayList<>();

        int diamondAmount = 2;
        int headAmount = 2;
        int bookAmount = 1;

        for (Entity e : list) {
            if (e instanceof Item) {
                ItemStack stack = ((Item) e).getItemStack();

                switch (stack.getType()) {
                    case DIAMOND:
                        if (diamondAmount > 0) {
                            nearbyValueables.add(e);
                            Xchant.instance.getServer().broadcastMessage("Found a Diamond");
                            diamondAmount -= stack.getAmount();
                        }
                        break;
                    case PLAYER_HEAD:
                        if (headAmount > 0) {
                            nearbyValueables.add(e);
                            Xchant.instance.getServer().broadcastMessage("Found a Head");
                            headAmount -= stack.getAmount();
                        }
                        break;

                    case ENCHANTED_BOOK:
                        if (bookAmount > 0) {
                            nearbyValueables.add(e);
                            Xchant.instance.getServer().broadcastMessage("Found a Book");
                            bookAmount -= stack.getAmount();
                        }
                        break;
                    default:
                        Xchant.instance.getServer().broadcastMessage("Found a " + stack.getType().name());
                        break;
                }

            }
        }
        Xchant.instance.getServer().broadcastMessage(diamondAmount + " " + headAmount + " " + bookAmount);

        if (diamondAmount <= 0 && headAmount <= 0 && bookAmount <= 0)
            return nearbyValueables;

        return null;
    }
}
