package de.paulsenik.mc.xchant;

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;


public class HowTo implements CommandExecutor {

  private static ItemStack howToBook;

  public HowTo() {
    generateBook();
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s,
      String[] args) {
    if (commandSender instanceof Player) {
      openBook((Player) commandSender);
      return true;
    }
    commandSender.sendMessage("Only Players can open the HowTo-Book!");
    return true;
  }

  public static void generateBook() {
    howToBook = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta bookMeta = (BookMeta) howToBook.getItemMeta();
    if (bookMeta == null) {
      return;
    }

    bookMeta.setAuthor("Paulsen__");
    bookMeta.setTitle("How2 Xchant");
    bookMeta.setLore(
        Arrays.asList("This is a HowTo-Book", "of the usefulness of the", "Xchant plugin!"));
    bookMeta.setPages(getPages());

    howToBook.setItemMeta(bookMeta);
  }

  private static ArrayList<String> getPages() {
    ArrayList<String> pages = new ArrayList<>();

    pages.add(
        "        " + ChatColor.LIGHT_PURPLE + ChatColor.UNDERLINE + "<Xchant>\n" + ChatColor.RESET +
            ChatColor.GRAY + "      by Paulsenik\n\n" + ChatColor.RESET +
            ChatColor.BLACK + "Enchant any item over the normal limit by throwing "
            + ChatColor.UNDERLINE + "items" + ChatColor.RESET + " on an "
            + ChatColor.UNDERLINE + "Enchantment Table.\n\n" + ChatColor.RESET +
            "The Maximum level for any item is " + ChatColor.GOLD + ChatColor.UNDERLINE
            + Xchant.MAX_LEVEL + ChatColor.RESET + "."
    );
    // Mainpage

    pages.add("" +
        ChatColor.BLUE + ChatColor.UNDERLINE + "Items for +1 lvl:\n\n" +
        ChatColor.GOLD + "- 1 Enchanted Book" + ChatColor.GRAY
        + "\n  with the desired\n  enchantment.\n" +
        ChatColor.GOLD + "- Your Tool/Armor\n" +
        ChatColor.GOLD + "- " + Xchant.HEAD_AMOUNT + " Player/Mob Head\n" +
        ChatColor.GOLD + "- " + Xchant.VALUABLE_AMOUNT + " Netherite Ingot\n"
    );

    return pages;
  }

  private static void openBook(final Player player) {
    player.openBook(howToBook);
  }
}
