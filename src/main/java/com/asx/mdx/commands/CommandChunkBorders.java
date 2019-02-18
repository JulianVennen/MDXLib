package com.asx.mdx.commands;

import com.asx.mdx.MDX;
import com.asx.mdx.core.network.server.PacketCommandChunkBorders;
import com.asx.mdx.lib.client.DebugToolsRenderer.BlockScanner;
import com.asx.mdx.lib.util.command.IClientCommand;
import com.asx.mdx.lib.world.entity.player.Players;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommandChunkBorders extends CommandBase implements IClientCommand
{
    @Override
    public String getName()
    {
        return "chunkborders";
    }

    @Override
    public String getUsage(ICommandSender commandSender)
    {
        return "/chunkborders <enabled/disabled> - Enables or disables chunk borders. Improved version of the vanilla chunk borders.";
    }
    
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return sender.canUseCommand(2, "");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayer player = Players.getPlayerForCommandSender(sender);

        if (player != null && player instanceof EntityPlayerMP)
        {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            MDX.network().sendTo(new PacketCommandChunkBorders(args), playerMP);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void executeClient(EntityPlayer player, String[] args)
    {
        if (args.length >= 1)
        {
            if (args[0].equalsIgnoreCase("enabled") || args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("on"))
            {
                BlockScanner.setChunkBordersEnabled(true);
                player.sendMessage(new TextComponentString("Enabled chunk borders."));
                return;
            }
            else if (args[0].equalsIgnoreCase("disabled") || args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("off"))
            {
                BlockScanner.setChunkBordersEnabled(false);
                player.sendMessage(new TextComponentString("Disabled chunk borders."));
                return;
            }
        }
        else
        {
            player.sendMessage(new TextComponentString("Incorrect amount of arguments! See the command context!"));
        }
    }
}
