package info.bytecraft.commands;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;

import static org.bukkit.ChatColor.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GiveCommand extends AbstractCommand
{
    public GiveCommand(Bytecraft Bytecraft)
    {
        super(Bytecraft, "give");
    }

    @Override
    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if (args.length == 0) {
            return false;
        }
        if (!player.isAdmin()) {
            return false;
        }

        String param = args[1].toUpperCase();
        Player delegate = Bukkit.getPlayer(args[0]);
        if (delegate != null) {
            BytecraftPlayer target = plugin.getPlayer(delegate);

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
                amount = Integer.parseInt(args[2]);
            } catch (ArrayIndexOutOfBoundsException e) {
                amount = 1;
            } catch (NumberFormatException e) {
                amount = 1;
            }

            int data;
            try {
                data = Integer.parseInt(args[3]);
            } catch (ArrayIndexOutOfBoundsException e) {
                data = 0;
            } catch (NumberFormatException e) {
                data = 0;
            }

            ItemStack item = new ItemStack(materialId, amount, (byte) data);
            if (item.getType() == Material.MONSTER_EGG
                    || item.getType() == Material.NAME_TAG) {
                return false;
            }

            PlayerInventory inv = target.getInventory();
            inv.addItem(item);

            Material material = Material.getMaterial(materialId);
            String materialName = material.toString();

            player.sendMessage("You gave " + amount + " of " + DARK_AQUA
                    + materialName.toLowerCase() + " to " + target.getName()
                    + ".");
            target.sendMessage(YELLOW
                    + "You were gifted by the gods. Look in your "
                    + "inventory!");

        }
        return true;
    }
}
