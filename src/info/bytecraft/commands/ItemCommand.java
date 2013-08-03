package info.bytecraft.commands;

import static org.bukkit.ChatColor.DARK_AQUA;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;

public class ItemCommand extends AbstractCommand
{

    private static Set<Material> disallowedItems = new HashSet<Material>();

    public ItemCommand(Bytecraft instance)
    {
        super(instance, "item");
        disallowedItems.add(Material.BEDROCK);
    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if (args.length == 0) {
            return false;
        }
        if (!player.isAdmin()) {
            return false;
        }

        String param = args[0].toUpperCase();

        int materialId;
        try {
            materialId = Integer.parseInt(param);
        } catch (NumberFormatException e) {
            try {
                Material material = Material.getMaterial(param);
                materialId = material.getId();
            } catch (NullPointerException ne) {
                player.sendMessage(DARK_AQUA
                        + "/item <id|name> <amount> <data>.");
                return true;
            }
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            amount = 1;
        } catch (NumberFormatException e) {
            amount = 1;
        }

        int data;
        try {
            data = Integer.parseInt(args[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            data = 0;
        } catch (NumberFormatException e) {
            data = 0;
        }

        ItemStack item = new ItemStack(materialId, amount, (byte) data);
        if (item.getType() == Material.MONSTER_EGG || item.getType() == Material.NAME_TAG) {
            return false;
        }

        PlayerInventory inv = player.getInventory();
        inv.addItem(item);

        Material material = Material.getMaterial(materialId);
        String materialName = material.toString();
        player.sendMessage("You received " + amount + " of " + DARK_AQUA
                + materialName.toLowerCase() + ".");
        return true;
    }

}
