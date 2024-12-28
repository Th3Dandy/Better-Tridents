package me.th3dandy.betterTridents;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TridentListener implements Listener {

    @EventHandler
    public void onTridentThrow(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Trident) {
            Trident trident = (Trident) event.getDamager();
            if (trident.getShooter() instanceof Player) {
                event.setDamage(10.0); // Set ranged damage to 10
            }
        }
    }

    @EventHandler
    public void onTridentMelee(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.TRIDENT) {
                event.setDamage(10.0); // Set melee damage to 10
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.TRIDENT) {
            item.addEnchantment(Enchantment.SHARPNESS, 1); // Make trident compatible with sharpness
        }
    }
}