package atm.bloodworkxgaming.blooddebug.commands.commandImpl;

import atm.bloodworkxgaming.blooddebug.BloodDebug;
import atm.bloodworkxgaming.blooddebug.commands.BloodDebugCommand;
import atm.bloodworkxgaming.blooddebug.commands.collectors.entities.EntityCollector;
import atm.bloodworkxgaming.blooddebug.commands.collectors.entities.EntityManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.*;

import static atm.bloodworkxgaming.blooddebug.commands.Commands.DF;
import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getClickableCommandMessage;
import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getNormalMessage;

public class CmdFindItems extends BloodDebugCommand {
    public CmdFindItems() {
        super("finditems");
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityManager manager = new EntityManager();
        String itemName;

        //get item name
        if (args.length >= 1){
            itemName = args[0];
        } else {
            sender.sendMessage(getClickableCommandMessage("\u00A74You must provide a item name, get all items with /bd items",
                    "/bd items", true));
            return;
        }

        // optional dimension
        if (args.length >= 2) {
            Integer dimension = null;
            try {
                if (!Objects.equals(args[0], "*")){
                    dimension = Integer.valueOf(args[0]);
                }
            } catch (NumberFormatException e){
                sender.sendMessage(getNormalMessage("\u00A74Wrong dimension id format, ignoring"));
            } finally {
                manager.collectEntityList(dimension);
            }
        } else {
            manager.collectEntityList(null);
        }

        EntityCollector eCollector = manager.entityHashMap.get(EntityItem.class.getName());

        if (eCollector == null) {
            sender.sendMessage(getNormalMessage("\u00A74No items found in the world"));
        } else {
            List<EntityItem> foundItems = new ArrayList<>();

            // Collects the list of all items and maps them to item names
            for (Entity entity : eCollector.entities) {
                EntityItem entityItem = (EntityItem) entity;
                String name = Objects.toString(entityItem.getItem().getItem().getRegistryName()) + ":" + entityItem.getItem().getMetadata();

                if (name.equals(itemName)){
                    foundItems.add(entityItem);
                }
            }

            if (foundItems.size() <= 0){
                sender.sendMessage(getClickableCommandMessage("\u00A74No item found with name " + itemName + ", get all items with /bd items",
                        "/bd items", true));
            }else {
                BloodDebug.logCommandChat(sender, getNormalMessage("Showing \u00A73" + foundItems.size() + " positions \u00A7rof \u00A76" + itemName));
                for (EntityItem entityItem : foundItems) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("\u00A7e- ");
                    BlockPos pos = entityItem.getPosition();
                    sb.append("\u00A73[").append(DF.format(pos.getX())).append(", ").append(DF.format(pos.getY())).append(", ").append(DF.format(pos.getZ())).append("]");

                    sb.append("\u00A7a{").append(entityItem.dimension).append("}");
                    BloodDebug.logCommandChat(sender, getClickableCommandMessage(sb.toString(), "/tp @p " + pos.getX() + " " + pos.getY() + " " + pos.getZ(), true));
                }
            }
        }
    }

    @Override
    protected void init() {
        setDescription(
                getClickableCommandMessage("\u00A72/bd finditems [name] <dim>", "/bd finditems ", false),
                getNormalMessage(" \u00A73Finds the coords of the given item"),
                getNormalMessage(" \u00A73Name must be provided, optional dimension")
        );
    }
}
