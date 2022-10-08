package ooo.paulsen.mc.xchant;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EnchantEvent implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        ItemStack item = event.getItemDrop().getItemStack();

        switch (item.getType()) {
            case DIAMOND:
            case PLAYER_HEAD:
            case ENCHANTED_BOOK:
                Xchant.instance.getServer().broadcastMessage("Dropped an Enchated Book!");

                List<Entity> nearbyItems = event.getItemDrop().getNearbyEntities(3, 2, 3);
                nearbyItems.add(event.getItemDrop());

                List<Entity> eList = Xchant.nearbyContain(nearbyItems);



                Xchant.instance.getServer().broadcastMessage("Found " + eList.size() + " Items");
                break;
        }
    }

}
