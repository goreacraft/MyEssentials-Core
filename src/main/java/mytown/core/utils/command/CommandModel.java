package mytown.core.utils.command;

import mytown.core.Utils;
import mytown.core.utils.Assert;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class CommandModel extends CmdBase {
    private final Command cmd;
    private final Method method;

    private List commandAliasCache = null;

    public CommandModel(Command cmd, Method method) {
        this.cmd = cmd;
        this.method = method;
    }

    @Override
    public List getCommandAliases() {
        if (commandAliasCache == null) {
            commandAliasCache = Arrays.asList(cmd.alias());
        }
        return commandAliasCache;
    }

    @Override
    public String getPermissionNode() {
        return cmd.permission();
    }

    @Override
    public boolean canConsoleUseCommand() {
        return cmd.console();
    }

    @Override
    public boolean canRConUseCommand() {
        return cmd.rcon();
    }

    @Override
    public boolean canCommandBlockUseCommand() {
        return cmd.commandblocks();
    }

    @Override
    public String getCommandName() {
        return cmd.name();
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + cmd.name() + " " + cmd.syntax();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        // Return if player is not allowed to use this command
        if(sender instanceof EntityPlayer && cmd.opsOnlyAccess() && !Utils.isOp((EntityPlayer)sender))
            throw new CommandException("commands.generic.permission");

        CommandManager.commandCall(getPermissionNode(), sender, Arrays.asList(args));

        // Not doing it directly
        //method.invoke(null, sender, Arrays.asList(args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandManager.getTabCompletionList(sender, Arrays.asList(args), cmd.permission());
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        Assert.Perm(sender, getPermissionNode(), canConsoleUseCommand(), canRConUseCommand(), canCommandBlockUseCommand());

        if(sender instanceof EntityPlayer && cmd.opsOnlyAccess() && !MinecraftServer.getServer().getConfigurationManager().func_152607_e(((EntityPlayer) sender).getGameProfile()))
            return false;

        return true;
        //return canUseWithoutPermission() || super.canCommandSenderUseCommand(sender);
    }
}
