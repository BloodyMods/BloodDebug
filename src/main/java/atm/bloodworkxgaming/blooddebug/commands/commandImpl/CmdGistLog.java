package atm.bloodworkxgaming.blooddebug.commands.commandImpl;

import atm.bloodworkxgaming.blooddebug.BloodDebug;
import atm.bloodworkxgaming.blooddebug.commands.BloodDebugCommand;
import atm.bloodworkxgaming.blooddebug.commands.gist.GithubManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.IOUtils;
import org.eclipse.egit.github.core.Gist;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.*;

public class CmdGistLog extends BloodDebugCommand {
    private Gist currentGist = null;

    public CmdGistLog() {
        super("gist");
    }

    @Override
    protected void init() {
        setDescription(
                getClickableCommandMessage("\u00A72/bd gist", "/bd gist", true),
                getNormalMessage(" \u00A73Gist the current content of the log"));
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
        StringBuilder content = new StringBuilder();

        BufferedReader in = null;
        try {

            in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(BloodDebug.LOG_FILE), "UTF8"));

            String str;
            while ((str = in.readLine()) != null) {
                content.append(str).append("\n");
            }

        } catch (IOException e) {
            BloodDebug.error("Error while reading file", e);
            sender.sendMessage(getNormalMessage("Error while reading the config file, check log for more info."));
            return;

        } finally {
            IOUtils.closeQuietly(in);
        }

        currentGist = GithubManager.updateGist(currentGist, "Gist from " + new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss").format(new Date()) + " issued by " + sender.getName(), "blooddebug.log", content.toString());

        if (currentGist != null) {
            String url = currentGist.getHtmlUrl();
            sender.sendMessage(getClickableBrowserLinkMessage("open gist here[" + url + "]", url));
        } else {
            sender.sendMessage(getNormalMessage("Gist could not be created. Check log for more info"));
        }
    }
}
