package me.th3dandy.betterTridents;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.AnvilView;

import java.util.Map;
import java.util.Random;

public class TridentListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        if (damager instanceof Player) {
            Player player = (Player) damager;
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() == Material.TRIDENT) {
                double baseDamage = 10.0;
                double finalDamage = baseDamage;

                // Check for Sharpness enchantment
                if (item.containsEnchantment(Enchantment.SHARPNESS)) {
                    int sharpnessLevel = item.getEnchantmentLevel(Enchantment.SHARPNESS);
                    finalDamage += 0.5 * sharpnessLevel + 0.5;
                }

                // Check for critical hit
                if (player.getFallDistance() > 0.0F && !player.isOnGround() && !player.isInsideVehicle() && player.getVehicle() == null) {
                    finalDamage *= 1.5;
                }

                event.setDamage(finalDamage);

                // Handle Looting enchantment
                if (item.containsEnchantment(Enchantment.LOOTING)) {
                    int lootingLevel = item.getEnchantmentLevel(Enchantment.LOOTING);
                    // Increase the maximum number of items for common drops
                    // Increase the chance of rare drops
                    // Increase the chance of equipment drops
                    // This logic should be implemented in the entity's drop handling code
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.TRIDENT) {
            // Set attack cooldown for trident usage
            AttributeInstance attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            attackSpeed.setBaseValue(4.2); // 4.2 attack speed
        }
    }

    @EventHandler
    public void onAnvilUse(PrepareAnvilEvent event) {
        ItemStack firstItem = event.getInventory().getItem(0);
        ItemStack secondItem = event.getInventory().getItem(1);
        if (firstItem != null && secondItem != null) {
            if (firstItem.getType() == Material.TRIDENT) {
                ItemStack result = firstItem.clone();
                boolean modified = false;
                if (secondItem.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) secondItem.getItemMeta();
                    if (bookMeta == null) {
                        return;
                    }

                    for (Map.Entry<Enchantment, Integer> entry : bookMeta.getStoredEnchants().entrySet()) {
                        if (isAllowedEnchantment(entry.getKey())) {
                            result.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                            modified = true;
                        }
                    }
                } else {
                    if (secondItem.getType() != Material.TRIDENT) {
                        return;
                    }

                    for (Map.Entry<Enchantment, Integer> entry : secondItem.getEnchantments().entrySet()) {
                        if (isAllowedEnchantment(entry.getKey())) {
                            int level = Math.max(result.getEnchantmentLevel(entry.getKey()), entry.getValue());
                            result.addUnsafeEnchantment(entry.getKey(), level);
                            modified = true;
                        }
                    }
                }

                if (modified) {
                    event.setResult(result);
                    AnvilView anvilView = event.getView();
                    if (anvilView instanceof AnvilView) {
                        anvilView.setMaximumRepairCost(5);
                        anvilView.setRepairCost(1);
                    }

                    anvilView = event.getView();
                    if (anvilView instanceof AnvilView) {
                        String newName = anvilView.getRenameText();
                        if (newName != null && !newName.isEmpty()) {
                            ItemMeta meta = result.getItemMeta();
                            meta.setDisplayName(newName);
                            result.setItemMeta(meta);
                            event.setResult(result);
                        }
                    }
                }
            }
        }
    }

    private boolean isAllowedEnchantment(Enchantment enchantment) {
        return enchantment == Enchantment.SHARPNESS || enchantment == Enchantment.LOOTING;
    }
}