package me.th3dandy.betterTridents;

import org.bukkit.plugin.java.JavaPlugin;

public class BetterTridents extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the TridentListener
        getServer().getPluginManager().registerEvents(new TridentListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}