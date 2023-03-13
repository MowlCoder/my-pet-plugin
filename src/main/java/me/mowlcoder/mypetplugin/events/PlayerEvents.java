package me.mowlcoder.mypetplugin.events;

import me.mowlcoder.mypetplugin.pet.Pet;
import me.mowlcoder.mypetplugin.pet.PetDataKey;
import me.mowlcoder.mypetplugin.pet.PetManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String petName = event.getPlayer().getPersistentDataContainer().get(
                NamespacedKey.fromString(PetDataKey.NAME.getKey()),
                PersistentDataType.STRING
        );

        if (petName == null) {
            return;
        }

        String petBlock = event.getPlayer().getPersistentDataContainer().get(
                NamespacedKey.fromString(PetDataKey.BLOCK.getKey()),
                PersistentDataType.STRING
        );

        Pet pet = new Pet(event.getPlayer(), petName, Material.valueOf(petBlock));

        String petColor = event.getPlayer().getPersistentDataContainer().get(
                NamespacedKey.fromString(PetDataKey.COLOR.getKey()),
                PersistentDataType.STRING
        );

        if (petColor != null) {
            pet.setColor(ChatColor.valueOf(petColor));
        }

        pet.startFollowingPlayer();
        PetManager.getInstance().addActivePet(event.getPlayer(), pet);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        PetManager.getInstance().removeActivePet(event.getPlayer());
    }
}
