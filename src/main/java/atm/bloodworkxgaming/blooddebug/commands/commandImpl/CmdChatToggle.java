package atm.bloodworkxgaming.blooddebug.commands.commandImpl;

import atm.bloodworkxgaming.blooddebug.ModConfig;
import atm.bloodworkxgaming.blooddebug.commands.BloodDebugCommand;
import atm.bloodworkxgaming.blooddebug.util.ChatColor;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getClickableCommandMessage;
import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getNormalMessage;

public class CmdChatToggle extends BloodDebugCommand{

    public CmdChatToggle() {
        super("chat");
    }

    /**
     * Has to be overwritten
     * Used to set the description and all other values
     * Better for viability, as the constructor is not that full then
     */
    @Override
    protected void init() {
        setDescription(
                getClickableCommandMessage("\u00A72/bd chat [enable|disable]", "/bd chat ", false),
                getNormalMessage(" \u00A73Enables or Disable the output to the chat to prevent spamm."),
                getNormalMessage(" \u00A73Doesn't update the config option on the disc, so has to be rerun every restart"));
    }

    /**
     * Has to be overwritten by the commands
     *
     * @param server
     * @param sender
     * @param args   : Has only the args after this original event
     */
    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {

        if (args.length > 0){
            if (args[0].equals("enable")) {
                ModConfig.chatOutput = true;
                sender.sendMessage(getNormalMessage(ChatColor.GREEN + "Enabled chat output."));
                return;

            } else if (args[0].equals("disable")) {
                ModConfig.chatOutput = false;
                sender.sendMessage(getNormalMessage(ChatColor.YELLOW + "Disabled chat output."));
                return;
            }
        }

        sender.sendMessage(getNormalMessage(ChatColor.RED + "Wrong arguments."));
    }

    /**
     * Has to be overwritten by the Commands when they need subcommands below the first one
     *
     * @param server
     * @param sender
     * @param args      : Args are only from past the initial command
     * @param targetPos
     * @return Returns an empty List by default
     */
    @Override
    public List<String> getSubSubCommand(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return ImmutableList.of("enable", "disable");
    }
}
