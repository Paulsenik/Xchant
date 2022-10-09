package ooo.paulsen.mc.xchant;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantEvent implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        Bukkit.getScheduler().runTaskLater(Xchant.instance, new Runnable() {
            @Override
            public void run() {

                Item item = event.getItemDrop();
                ItemStack itemStack = event.getItemDrop().getItemStack();

                switch (itemStack.getType()) {
                    case DIAMOND:
                    case PLAYER_HEAD:
                    case ENCHANTED_BOOK:

                        // checking for Ground
                        if (Math.abs(item.getVelocity().getY()) <= 0.15d) {

                            // get block under item
                            Material m = item.getLocation().add(0, -0.1, 0).getBlock().getBlockData().getMaterial();

                            // item is on required block
                            if (m == Xchant.ground) {


                                itemStack.setAmount(0);
                                Xchant.instance.getServer().broadcastMessage(" Item on required ground!");

                                // TODO create list with al item-entitys

                                // TODO check amount of items

                                // TODO modify enchantment

                                // TODO remove items

                                // TODO spawn particles

                            }
                        }

                        break;
                }

            }
        }, 80);

    }

}
