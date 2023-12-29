package de.paulsenik.mc.xchant;

import de.paulsenik.mc.xchant.commands.XchantCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantEvent implements Listener {

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    TextComponent t = new TextComponent("/howto-xchant");
    t.setClickEvent(
        new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/paulsenik/Xchant"));
    t.setColor(ChatColor.LIGHT_PURPLE);
    t.setUnderlined(true);
    t.setBold(true);

    event.getPlayer().spigot().sendMessage(
        new ComponentBuilder().append(XchantCommand.head + "Read how to use ").append(t).create());
  }

  @EventHandler
  public void onDrop(PlayerDropItemEvent event) {
    scheduleGroundCheck(event.getItemDrop());
  }

  private void scheduleGroundCheck(Item item) {
    Bukkit.getScheduler().runTaskLater(Xchant.instance, () -> {

      switch (item.getItemStack().getType()) {
        case DIAMOND:
        case PLAYER_HEAD:
        case CREEPER_HEAD:
        case DRAGON_HEAD:
        case ZOMBIE_HEAD:
        case SKELETON_SKULL:
        case WITHER_SKELETON_SKULL:
        case ENCHANTED_BOOK:

          // is on Ground
          if (Math.abs(item.getVelocity().getY()) >= 0.15d) {
            return;
          }

          // item is on required block
          Material m = item.getLocation().add(0, -0.1, 0).getBlock().getBlockData().getMaterial();
          if (m != Xchant.GROUND) {
            return;
          }

          // Get nearby items
          List<Item> items = new ArrayList<>();
          items.add(item);
          item.getNearbyEntities(1, 1, 1).forEach(e -> {
            if (e instanceof Item) {
              items.add((Item) e);
            }
          });

          if (enchantGroundItems(items)) {
            // try ritual again
            List<Entity> l = item.getNearbyEntities(1, 1, 1);
            for (Entity e : l) {
              if (e instanceof Item) {
                Item i = (Item) e;
                switch (i.getItemStack().getType()) {
                  case DIAMOND:
                  case PLAYER_HEAD:
                  case CREEPER_HEAD:
                  case DRAGON_HEAD:
                  case ZOMBIE_HEAD:
                  case SKELETON_SKULL:
                  case WITHER_SKELETON_SKULL:
                  case ENCHANTED_BOOK:
                    Xchant.l.info(i.getName());
                    scheduleGroundCheck(i);
                    return;
                }
              }
            }
          }
          break;
      }

    }, Xchant.RITUAL_WAIT_INTERVALL);
  }

  private boolean enchantGroundItems(List<Item> items) {
    // check items
    if (!checkAmounts(items)) {
      return false;
    }

    Item enchantItem = getEnchantItem(items);
    Enchantment enchantment = getEnchantment(items);
    if (enchantment == null || enchantItem == null) {
      return false;
    }

    // Check lvl
    ItemMeta meta = enchantItem.getItemStack().getItemMeta();
    if (meta == null || meta.getEnchantLevel(enchantment) >= Xchant.MAX_LEVEL) {
      return false;
    }

    removeRitualItems(items);
    Xchant.cleverUpEnchant(enchantItem.getItemStack(), enchantment);
    playEffects(items.get(0).getLocation());
    return true;
  }

  /**
   * checks if the items are valid and enough for a ritual
   */
  private static boolean checkAmounts(List<Item> items) {
    if (items.size() < 4) {
      return false;
    }

    int diamonds = Xchant.DIAMONDS;
    int heads = Xchant.HEADS;
    boolean hasBook = false, hasEnchantableItem = false;

    for (Item i : items) {
      ItemStack s = i.getItemStack();
      switch (s.getType()) {
        case DIAMOND:
          diamonds -= s.getAmount();
          break;
        case PLAYER_HEAD:
        case CREEPER_HEAD:
        case DRAGON_HEAD:
        case ZOMBIE_HEAD:
        case SKELETON_SKULL:
        case WITHER_SKELETON_SKULL:
          heads -= s.getAmount();
          break;
        case ENCHANTED_BOOK:
          hasBook = true;
          break;
        default:
          hasEnchantableItem = true;
      }
    }

    return diamonds <= 0 && heads <= 0 && hasBook && hasEnchantableItem;
  }

  /**
   * Only removes the items needed for the ritual
   */
  private void removeRitualItems(List<Item> items) {
    int diamonds = Xchant.DIAMONDS;
    int heads = Xchant.HEADS;
    boolean hasBook = false;

    for (Item i : items) {
      ItemStack itemStack = i.getItemStack();
      switch (itemStack.getType()) {
        case DIAMOND:
          if (diamonds > 0) {
            if (diamonds < itemStack.getAmount()) {
              itemStack.setAmount(itemStack.getAmount() - diamonds);
              diamonds = 0;
            } else {
              int amount = itemStack.getAmount();
              diamonds -= amount;
              itemStack.setAmount(itemStack.getAmount() - amount);
            }
          }
          break;
        case PLAYER_HEAD:
        case CREEPER_HEAD:
        case DRAGON_HEAD:
        case ZOMBIE_HEAD:
        case SKELETON_SKULL:
        case WITHER_SKELETON_SKULL:
          if (heads > 0) {
            if (heads < itemStack.getAmount()) {
              itemStack.setAmount(itemStack.getAmount() - heads);
              heads = 0;
            } else {
              int amount = itemStack.getAmount();
              heads -= amount;
              itemStack.setAmount(itemStack.getAmount() - amount);
            }
          }
          break;
        case ENCHANTED_BOOK:
          if (!hasBook) {
            hasBook = true;
            itemStack.setAmount(Math.max(itemStack.getAmount() - 1, 0));
          }
          break;
      }
    }
  }

  /**
   * @return any other item not needed for the ritual to be enchanted
   */
  @Nullable
  private static Item getEnchantItem(List<Item> items) {
    for (Item i : items) {
      switch (i.getItemStack().getType()) {
        case DIAMOND:
        case PLAYER_HEAD:
        case CREEPER_HEAD:
        case DRAGON_HEAD:
        case ZOMBIE_HEAD:
        case SKELETON_SKULL:
        case WITHER_SKELETON_SKULL:
        case ENCHANTED_BOOK:
          continue;
        default:
          return i;
      }
    }
    return null;
  }

  @Nullable
  private static Enchantment getEnchantment(List<Item> items) {
    for (Item i : items) {
      if (i.getItemStack().getType() == Material.ENCHANTED_BOOK) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) i.getItemStack().getItemMeta();

        if (meta == null) {
          return null;
        }

        try {
          Map<Enchantment, Integer> enchants = meta.getStoredEnchants();
          if (!enchants.isEmpty()) {
            return (Enchantment) enchants.keySet().toArray()[0];
          }
        } catch (RuntimeException e) {
          // Enchantments can be funky (catch is just in case something happens)
          Xchant.l.warning("Something went wrong when trying to get the Enchantment of a Book");
          Xchant.l.warning(e.toString());
          return null;
        }
      }
    }
    return null;
  }

  private static void playEffects(Location l) {
    if (l.getWorld() != null) {
      l.getWorld().spawnParticle(Particle.SPELL_WITCH, l, 600);
      l.getWorld().playSound(l, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1f, 1f);
    }
  }

}
