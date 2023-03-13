package me.mowlcoder.mypetplugin.pet;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PetManager {

    private final Map<UUID, Pet> activePets = new HashMap<>();

    public void addActivePet(Player player, Pet pet) {
        activePets.put(player.getUniqueId(), pet);
    }

    public Pet getPlayerPet(Player player) {
        return activePets.get(player.getUniqueId());
    }

    public void removeActivePet(Player player) {
        Pet pet = activePets.get(player.getUniqueId());

        if (pet != null) {
            pet.destroy();
            activePets.remove(player.getUniqueId());
        }
    }

    public void removeAllActivePet() {
        for (UUID uuid : activePets.keySet()) {
            Pet pet = activePets.get(uuid);
            pet.destroy();
            activePets.remove(uuid);
        }
    }

    private static final PetManager instance = new PetManager();

    public static PetManager getInstance() {
        return instance;
    }

    private PetManager() {}
}
