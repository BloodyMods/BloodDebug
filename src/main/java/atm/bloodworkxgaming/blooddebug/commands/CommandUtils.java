package atm.bloodworkxgaming.blooddebug.commands;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/**
 * @author BloodWorkXGaming
 */
public class CommandUtils {
    public static RayTraceResult getPlayerLookat(EntityPlayer player, double range) {
        Vec3d eyes = player.getPositionEyes(1.0F);
        return player.getEntityWorld().rayTraceBlocks(eyes, eyes.add(player.getLookVec().scale(range)));
    }

    public static Integer readOptionalNumber(String[] args, int index) {
        if (args.length > index) {
            try {
                return Integer.valueOf(args[index]);
            } catch (NumberFormatException ignored) {
            }
        }

        return null;
    }
}
