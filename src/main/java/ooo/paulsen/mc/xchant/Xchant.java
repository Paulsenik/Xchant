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

    public static final int diamonds = 3, heads = 2;
    public static final Material ground = Material.ENCHANTING_TABLE;

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

    // config
    public void load() {

    }

    // config
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
            if (meta != null) {
                int lvl = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);

                meta.addEnchant(Enchantment.DAMAGE_ALL, lvl + 1, true);
                item.setItemMeta(meta);
            }
        }
    }

}
