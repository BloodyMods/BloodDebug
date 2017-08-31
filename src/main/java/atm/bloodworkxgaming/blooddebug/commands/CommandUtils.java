package atm.bloodworkxgaming.blooddebug.commands;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BloodWorkXGaming
 */
public class CommandUtils {
    
    public static RayTraceResult getPlayerLookat(EntityPlayer player, double range) {
        Vec3d eyes = player.getPositionEyes(1.0F);
        return player.getEntityWorld().rayTraceBlocks(eyes, eyes.add(player.getLookVec().scale(range)));
    }
}
