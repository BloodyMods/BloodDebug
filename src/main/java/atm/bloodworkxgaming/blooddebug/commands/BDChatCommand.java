package atm.bloodworkxgaming.blooddebug.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author BloodWorkXGaming
 */
public class BDChatCommand extends CommandBase {

    private static final List<String> aliases = new ArrayList<>();
    private static final Map<String, BloodDebugCommand> craftTweakerCommands = new TreeMap<>();
    
    static {
        aliases.add("bd");
    }
    
    public static void register(FMLServerStartingEvent ev) {
        Commands.registerCommands();
        ev.registerServerCommand(new BDChatCommand());
    }
    
    public static void sendUsage(ICommandSender sender) {
        sender.sendMessage(SpecialMessagesChat.EMPTY_TEXTMESSAGE);
        
        for(Map.Entry<String, BloodDebugCommand> entry : craftTweakerCommands.entrySet()) {
            for(ITextComponent s : entry.getValue().getDescription()) {
                sender.sendMessage(s);
            }
            sender.sendMessage(SpecialMessagesChat.EMPTY_TEXTMESSAGE);
        }
    }
    
    public static void registerCommand(BloodDebugCommand command) {
        craftTweakerCommands.put(command.getSubCommandName(), command);
    }
    
    @Override
    public String getName() {
        return "blooddebug";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("/bd ");
        
        String[] commands = new String[craftTweakerCommands.keySet().size()];
        craftTweakerCommands.keySet().toArray(commands);
        
        for(int i = 0; i < commands.length; i++) {
            sb.append(commands[i]);
            if(i != commands.length - 1)
                sb.append(" | ");
            
        }
        
        return sb.toString();
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length <= 0) {
            sender.sendMessage(new TextComponentString(getUsage(sender)));
            return;
        }
        
        if(craftTweakerCommands.containsKey(args[0])) {
            if(sender.getCommandSenderEntity() instanceof EntityPlayer) {
                craftTweakerCommands.get(args[0]).executeCommand(server, sender, ArrayUtils.subarray(args, 1, args.length));
            } else {
                craftTweakerCommands.get(args[0]).executeCommand(server, sender, ArrayUtils.subarray(args, 1, args.length));
            }
        } else {
            sender.sendMessage(SpecialMessagesChat.getClickableCommandMessage("\u00A7cNo such command! \u00A76[Click to show help]", "/bt help", true));
        }
    }
    
    @Override
    public List<String> getAliases() {
        return aliases;
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        Set<String> keys = craftTweakerCommands.keySet();
        List<String> currentPossibleCommands = new ArrayList<>();
        
        if(args.length <= 0) {
            return new ArrayList<>(keys);
        }
        
        // First sub-command
        if(args.length == 1) {
            for(String cmd : keys) {
                if(cmd.startsWith(args[0])) {
                    currentPossibleCommands.add(cmd);
                }
            }
            return currentPossibleCommands;
        }
        
        // gives subcommands of the subcommand
        // each has to implement on it's own for special requirements
        BloodDebugCommand subCommand = craftTweakerCommands.get(args[0]);
        if(subCommand != null) {
            System.out.println(Arrays.toString(ArrayUtils.subarray(args, 1, args.length)));
            return subCommand.getSubSubCommand(server, sender, ArrayUtils.subarray(args, 1, args.length), targetPos);
            
        }
        
        // returns empty by default
        return currentPossibleCommands;
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
}
