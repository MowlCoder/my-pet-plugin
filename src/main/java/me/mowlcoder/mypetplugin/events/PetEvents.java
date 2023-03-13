package me.mowlcoder.mypetplugin.events;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.persistence.PersistentDataType;

public class PetEvents implements Listener {

    @EventHandler
    public void onPetDeath(EntityDeathEvent event) {
        String myPetPluginPet = event.getEntity().getPersistentDataContainer().get(
                NamespacedKey.fromString("my-pet-plugin-pet"),
                PersistentDataType.STRING
        );

        if (myPetPluginPet != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPetBlockTake(PlayerArmorStandManipulateEvent event) {
        String myPetPluginPet = event.getRightClicked().getPersistentDataContainer().get(
                NamespacedKey.fromString("my-pet-plugin-pet"),
                PersistentDataType.STRING
        );

        if (myPetPluginPet != null) {
            event.setCancelled(true);
        }
    }
}
