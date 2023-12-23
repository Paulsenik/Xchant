package de.paulsenik.mc.xchant;

import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Xchant extends JavaPlugin {

  public static final Material GROUND = Material.ENCHANTING_TABLE;
  public static final int DIAMONDS = 3, HEADS = 1;
  public static final int MAX_LEVEL = 10;

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
    getCommand("howto-xchant").setExecutor(new HowTo());
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

    if (item == null || enchant == null) {
      return;
    }

    if (item.getType() == Material.ENCHANTED_BOOK) {

      EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
      int lvl = meta.getStoredEnchantLevel(enchant);

      meta.addStoredEnchant(enchant, lvl + 1, true);
      item.setItemMeta(meta);

    } else {

      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
        int lvl = meta.getEnchantLevel(enchant);

        meta.addEnchant(enchant, lvl + 1, true);
        item.setItemMeta(meta);
      }
    }
  }

}
