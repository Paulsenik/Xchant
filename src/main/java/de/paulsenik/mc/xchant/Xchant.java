package de.paulsenik.mc.xchant;

import de.paulsenik.mc.xchant.commands.XchantCommand;
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
  public static final int RITUAL_WAIT_INTERVALL = 80; // in TICKS

  public static Xchant instance;

  public static Logger l;

  @Override
  public void onEnable() {
    instance = this;
    l = getLogger();
    load();

    getServer().getPluginManager().registerEvents(new EnchantEvent(), this);
    XchantCommand xchant = new XchantCommand();
    getCommand("xchant").setExecutor(xchant);
    getCommand("xchant").setTabCompleter(xchant);
    getCommand("howto-xchant").setExecutor(new HowTo());
  }

  @Override
  public void onDisable() {
    save();
  }

  /*
      sets enchantment level of item 1 higher and for books the new stored-lvl.
   */
  public static void cleverUpEnchant(ItemStack item, Enchantment enchant) {
    if (item == null || enchant == null) {
      return;
    }

    if (item.getType() == Material.ENCHANTED_BOOK) {
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
      if (meta != null) {
        meta.addStoredEnchant(enchant, meta.getStoredEnchantLevel(enchant) + 1, true);
        item.setItemMeta(meta);
      }
    } else {
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
        meta.addEnchant(enchant, meta.getEnchantLevel(enchant) + 1, true);
        item.setItemMeta(meta);
      }
    }
  }

  public void load() {
    // TODO
  }

  public void save() {
    // TODO
  }

}
