package me.mowlcoder.mypetplugin.pet;

import me.mowlcoder.mypetplugin.MyPetPlugin;
import me.mowlcoder.mypetplugin.utils.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class Pet {
    private ArmorStand armorStand;
    private Player player;

    private ChatColor petColor;

    private String name;

    private BukkitRunnable followingThread;

    public Pet(Player player, String name, Material material) {
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(
                player.getLocation(),
                EntityType.ARMOR_STAND
        );

        petColor = ChatColor.valueOf(
                MyPetPlugin.getInstance().getConfig().getString("pet.defaultColor")
        );

        armorStand.setSmall(true);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setCustomName(
                ChatUtil.createColorMessage(
                        "&" + petColor.getChar() + name
                )
        );
        armorStand.setCustomNameVisible(true);
        armorStand.getEquipment().setHelmet(new ItemStack(material));

        armorStand.getPersistentDataContainer().set(
                NamespacedKey.fromString("my-pet-plugin-pet"),
                PersistentDataType.STRING,
                "yes"
        );

        this.armorStand = armorStand;
        this.player = player;
        this.name = name;

        player.getPersistentDataContainer().set(
                NamespacedKey.fromString(PetDataKey.NAME.getKey()),
                PersistentDataType.STRING,
                name
        );

        player.getPersistentDataContainer().set(
                NamespacedKey.fromString(PetDataKey.BLOCK.getKey()),
                PersistentDataType.STRING,
                material.name()
        );
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void setName(String name) {
        this.name = name;

        armorStand.setCustomName(
                ChatUtil.createColorMessage(
                        "&" + petColor.getChar() + name
                )
        );
        player.getPersistentDataContainer().set(
                NamespacedKey.fromString(PetDataKey.NAME.getKey()),
                PersistentDataType.STRING,
                name
        );
    }

    public void setColor(ChatColor color) {
        this.petColor = color;

        armorStand.setCustomName(
                ChatUtil.createColorMessage(
                        "&" + petColor.getChar() + name
                )
        );

        player.getPersistentDataContainer().set(
                NamespacedKey.fromString(PetDataKey.COLOR.getKey()),
                PersistentDataType.STRING,
                color.name()
        );
    }

    public void setBlock(Material material) {
        armorStand.getEquipment().setHelmet(new ItemStack(material));

        player.getPersistentDataContainer().set(
                NamespacedKey.fromString(PetDataKey.BLOCK.getKey()),
                PersistentDataType.STRING,
                material.name()
        );
    }

    public void moveToPlayer() {
        getArmorStand().teleport(
                new Location(
                        player.getWorld(),
                        player.getLocation().x() + 0.7,
                        player.getLocation().y(),
                        player.getLocation().z() + 0.7
                )
        );
    }

    public void startFollowingPlayer() {
        followingThread = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                }

                moveToPlayer();
            }
        };

        followingThread.runTaskTimer(MyPetPlugin.getInstance(), 0L, 1L);
    }

    public void destroy() {
        armorStand.remove();
        followingThread.cancel();
    }
}
