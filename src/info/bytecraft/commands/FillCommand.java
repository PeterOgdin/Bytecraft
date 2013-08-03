package info.bytecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.blockfill.Fill;

public class FillCommand extends AbstractCommand
{

    public FillCommand(Bytecraft instance)
    {
        super(instance, "fill");
    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if (!player.isAdmin() && !player.isBuilder())
            return true;
        if (args.length != 1)
            return true;
        Material mat;
        try {
            mat = Material.getMaterial(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            mat = Material.getMaterial(args[0].toUpperCase());
        }
        if (mat != null) {
            if (player.getFillBlock1() != null
                    && player.getFillBlock2() != null) {
                Fill fill =
                        new Fill(player.getWorld(), player.getFillBlock1(),
                                player.getFillBlock2(), mat);
                int i = fill.fill();
                player.sendMessage(ChatColor.DARK_AQUA + "Filled " + ChatColor.GOLD
                        + i + ChatColor.DARK_AQUA + " blocks to "
                        + mat.name().toLowerCase().replace("_", " "));
            }
        }
        return true;
    }

}
