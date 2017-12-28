package atm.bloodworkxgaming.blooddebug;

import atm.bloodworkxgaming.blooddebug.commands.BDChatCommand;
import atm.bloodworkxgaming.blooddebug.commands.gist.GithubManager;
import atm.bloodworkxgaming.blooddebug.logger.ConsoleLogger;
import atm.bloodworkxgaming.blooddebug.logger.FileLogger;
import atm.bloodworkxgaming.blooddebug.logger.ILogger;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod(modid = BloodDebug.MODID, version = BloodDebug.VERSION)
public class BloodDebug
{
    public static final String MODID = "blooddebug";
    public static final String VERSION = "@MAJOR@.@MINOR@.@REVIS@.@BUILD@";

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    public static List<ILogger> loggers = new ArrayList<>();

    public static final File LOG_FILE = new File("logs/blooddebug.log");

    static {
        loggers.add(new FileLogger("blooddebug.log"));
        loggers.add(new ConsoleLogger());
    }

    /**
     * Error handling
     */
    public static void info(String s){
        for (ILogger logger : loggers) {
            logger.logInfo(s);
        }
    }

    public static void warn(String s){
        for (ILogger logger : loggers) {
            logger.logWarning(s);
        }
    }

    public static void error(String s){
        for (ILogger logger : loggers) {
            logger.logError(s);
        }
    }


    public static void error(String s, Exception e){
        for (ILogger logger : loggers) {
            logger.logError(s, e);
        }
    }

    public static void logCommand(String message){
        for (ILogger logger : loggers) {
            logger.logCommand(message);
        }
    }

    public static void logCommandChat(ICommandSender sender, ITextComponent message){
        if (ModConfig.chatOutput) sender.sendMessage(message);
        for (ILogger logger : loggers) {
            logger.logCommand(message.getUnformattedText());
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        GithubManager.login();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent ev) {
        // registering the command
        BDChatCommand.register(ev);
    }


    @NetworkCheckHandler
    public boolean matchModVersion(Map<String, String> remoteVersions, Side side){
        return true;
    }
}
