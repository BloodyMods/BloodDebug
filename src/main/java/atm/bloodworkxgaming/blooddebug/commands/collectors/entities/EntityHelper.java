package atm.bloodworkxgaming.blooddebug.commands.collectors.entities;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import static atm.bloodworkxgaming.blooddebug.commands.Commands.DF;
import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getClickableCommandMessage;

public class EntityHelper {
    public static ITextComponent getEntityMessage(Entity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatFormatting.YELLOW).append("- ").append(entity.getName());
        BlockPos pos = entity.getPosition();
        sb.append(ChatFormatting.DARK_AQUA).append(" [").append(DF.format(entity.posX)).append(", ").append(DF.format(entity.posY)).append(", ").append(DF.format(entity.posZ)).append("]");
        sb.append(ChatFormatting.GREEN).append("{").append(entity.dimension).append("}");

        return getClickableCommandMessage(sb.toString(), "/bd tpx " + entity.getEntityWorld().provider.getDimension() + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ(), true);
    }
}
