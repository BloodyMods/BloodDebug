package atm.bloodworkxgaming.blooddebug;


import net.minecraftforge.common.config.Config;

@Config(modid = BloodDebug.MODID)
public class ModConfig {
    @Config.Comment("Key used for Github")
    public static String githubOAuthKey = "";

    @Config.Comment("Whether or not to output messages to the chat. Disable if you want to only use the gist stuff.")
    public static boolean chatOutput = true;

    @Config.Comment("Should the gist be secret")
    public static boolean secretGist = true;
}
