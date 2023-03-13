package me.mowlcoder.mypetplugin.commands;

import me.mowlcoder.mypetplugin.MyPetPlugin;
import me.mowlcoder.mypetplugin.pet.Pet;
import me.mowlcoder.mypetplugin.pet.PetDataKey;
import me.mowlcoder.mypetplugin.pet.PetManager;
import me.mowlcoder.mypetplugin.utils.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class PetCMD implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtil.createColorMessage(MyPetPlugin.getInstance().getConfig().getString("messages.commandUseOnlyPlayer")));
            return true;
        }

        if (args.length < 1) {
            return false;
        }

        if (args[0].equals("spawn")) {
            return spawnPet(player, args[1]);
        } else if (args[0].equals("remove")) {
            return removePet(player);
        } else if (args[0].equals("set-name")) {
            if (args.length != 2) {
                ChatUtil.sendMessage(
                        player,
                        MyPetPlugin.getInstance().getConfig().getString("messages.notEnterPetName")
                );
                return true;
            }

            return setPetName(player, args[1]);
        } else if (args[0].equals("set-block")) {
            if (args.length != 2) {
                ChatUtil.sendMessage(
                        player,
                        MyPetPlugin.getInstance().getConfig().getString("messages.notValidMaterial")
                );
                return true;
            }

            return setPetBlock(player, args[1]);
        } else if (args[0].equals("set-color")) {
            if (args.length != 2) {
                ChatUtil.sendMessage(
                        player,
                        MyPetPlugin.getInstance().getConfig().getString("messages.notValidMaterial")
                );
                return true;
            }

            return setPetColor(player, args[1]);
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList(
                    "spawn",
                    "remove",
                    "set-name",
                    "set-block",
                    "set-color"
            );
        }

        return null;
    }

    private boolean spawnPet(Player player, String blockMaterial) {
        Pet pet = PetManager.getInstance().getPlayerPet(player);

        if (pet != null) {
            ChatUtil.sendMessage(
                    player,
                    MyPetPlugin.getInstance().getConfig().getString("messages.alreadyHavePet")
            );
            return true;
        }

        try {
            pet = new Pet(
                    player,
                    MyPetPlugin.getInstance().getConfig()
                            .getString("pet.defaultTitle")
                            .replace("%player_name%", player.getName()),
                    Material.valueOf(blockMaterial)
            );
            pet.startFollowingPlayer();
            PetManager.getInstance().addActivePet(player, pet);
        } catch (IllegalArgumentException e) {
            ChatUtil.sendMessage(
                    player,
                    MyPetPlugin.getInstance().getConfig().getString("messages.notValidMaterial")
            );
        }

        return true;
    }

    private boolean removePet(Player player) {
        PetManager.getInstance().removeActivePet(player);

        player.getPersistentDataContainer().remove(
                NamespacedKey.fromString(PetDataKey.NAME.getKey())
        );
        player.getPersistentDataContainer().remove(
                NamespacedKey.fromString(PetDataKey.BLOCK.getKey())
        );
        player.getPersistentDataContainer().remove(
                NamespacedKey.fromString(PetDataKey.COLOR.getKey())
        );
        return true;
    }

    private boolean setPetName(Player player, String petName) {
        Pet pet = PetManager.getInstance().getPlayerPet(player);

        if (pet == null) {
            ChatUtil.sendMessage(
                    player,
                    MyPetPlugin.getInstance().getConfig().getString("messages.notHavePet")
            );
            return true;
        }

        pet.setName(petName);

        ChatUtil.sendMessage(
                player,
                MyPetPlugin.getInstance().getConfig().getString("messages.setNameSuccess")
        );

        return true;
    }

    private boolean setPetBlock(Player player, String petMaterial) {
        Pet pet = PetManager.getInstance().getPlayerPet(player);

        if (pet == null) {
            ChatUtil.sendMessage(
                    player,
                    MyPetPlugin.getInstance().getConfig().getString("messages.notHavePet")
            );
            return true;
        }

        try {
            pet.setBlock(Material.valueOf(petMaterial));

            ChatUtil.sendMessage(
                    player,
                    MyPetPlugin.getInstance().getConfig().getString("messages.setBlockSuccess")
            );
        } catch (IllegalArgumentException e) {
            ChatUtil.sendMessage(
                    player,
                    MyPetPlugin.getInstance().getConfig().getString("messages.notValidMaterial")
            );
        }

        return true;
    }

    private boolean setPetColor(Player player, String petColor) {
        Pet pet = PetManager.getInstance().getPlayerPet(player);

        if (pet == null) {
            ChatUtil.sendMessage(
                    player,
                    MyPetPlugin.getInstance().getConfig().getString("messages.notHavePet")
            );
            return true;
        }

        try {
            pet.setColor(ChatColor.valueOf(petColor));

            ChatUtil.sendMessage(
                    player,
                    MyPetPlugin.getInstance().getConfig().getString("messages.setColorSuccess")
            );
        } catch (IllegalArgumentException e) {
            ChatUtil.sendMessage(
                    player,
                    MyPetPlugin.getInstance().getConfig().getString("messages.notValidColor")
            );
        }

        return true;
    }
}
