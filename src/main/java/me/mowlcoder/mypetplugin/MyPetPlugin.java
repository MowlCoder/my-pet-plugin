package me.mowlcoder.mypetplugin;

import me.mowlcoder.mypetplugin.commands.PetCMD;
import me.mowlcoder.mypetplugin.events.PetEvents;
import me.mowlcoder.mypetplugin.events.PlayerEvents;
import me.mowlcoder.mypetplugin.pet.PetManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MyPetPlugin extends JavaPlugin {

    private static MyPetPlugin instance;

    public static MyPetPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getCommand("pet").setExecutor(new PetCMD());
        getCommand("pet").setTabCompleter(new PetCMD());

        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new PetEvents(), this);

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        PetManager.getInstance().removeAllActivePet();
    }
}
