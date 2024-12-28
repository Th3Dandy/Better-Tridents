package me.th3dandy.betterTridents;

import org.bukkit.plugin.java.JavaPlugin;

public final class BetterTridents extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new TridentListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}