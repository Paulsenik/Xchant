package ooo.paulsen.mc.xchant;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HowTo implements CommandExecutor {

    private static ItemStack howToBook;

    public HowTo() {
        generateBook();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            openBook((Player) commandSender);
            return true;
        }
        commandSender.sendMessage("Only Players can open the HowTo-Book!");
        return true;
    }

    public static void generateBook(){
        howToBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) howToBook.getItemMeta();

        bookMeta.setAuthor("Paulsen__");
        bookMeta.setTitle("How2 Xchant");
        bookMeta.setLore(Arrays.asList("This is a HowTo-Book", "of the usefulness of the", "Xchant plugin!"));
        bookMeta.setPages(getPages());

        howToBook.setItemMeta(bookMeta);
    }

    private static ArrayList<String> getPages(){
        ArrayList<String> pages = new ArrayList<>();

        pages.add("        " + ChatColor.LIGHT_PURPLE + ChatColor.UNDERLINE + "<Xchant>\n" + ChatColor.RESET +
                ChatColor.GRAY+"      by Paulsen__\n\n"+ChatColor.RESET+
                ChatColor.BLACK + "Enchant any item over the normal limit by throwing "+ChatColor.UNDERLINE+"7 items"+ChatColor.RESET+" onto an "+ChatColor.UNDERLINE+"Enchantment-Table\n\n"+ChatColor.RESET+
                "The Maximum level is "+ChatColor.GOLD+ChatColor.UNDERLINE+Xchant.MAX_LEVEL+"\n\n"+
                        ChatColor.BLUE+ChatColor.UNDERLINE+"The 7 Items needed for 1 lvl-upgrade:");
        // Mainpage

        pages.add(ChatColor.GOLD + "- 2 Playerheads\n"+
                ChatColor.GOLD + "- 3 Diamonds\n"+
                ChatColor.GOLD + "- 1 Enchanted Book"+ChatColor.GRAY+" with the required enchantment\n"+
                ChatColor.GOLD + "- 1 Item"+ChatColor.GRAY+" which receives the enchantment");

        return pages;
    }

    private static void openBook(final Player player) {
        player.openBook(howToBook);
    }
}
