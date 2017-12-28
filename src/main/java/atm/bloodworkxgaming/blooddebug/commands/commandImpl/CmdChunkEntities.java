package atm.bloodworkxgaming.blooddebug.commands.commandImpl;

import atm.bloodworkxgaming.blooddebug.BloodDebug;
import atm.bloodworkxgaming.blooddebug.commands.BloodDebugCommand;
import atm.bloodworkxgaming.blooddebug.commands.CommandUtils;
import atm.bloodworkxgaming.blooddebug.commands.collectors.entities.EntityHelper;
import atm.bloodworkxgaming.blooddebug.util.ChatColor;
import atm.bloodworkxgaming.blooddebug.util.CountedListItem;
import atm.bloodworkxgaming.blooddebug.util.CountedListItemGeneric;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getClickableCommandMessage;
import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getNormalMessage;

public class CmdChunkEntities extends BloodDebugCommand {
    public CmdChunkEntities() {
        super("chunkentities");
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
        Integer dimension = CommandUtils.readOptionalNumber(args, 0);
        Integer cordX = CommandUtils.readOptionalNumber(args, 1);
        Integer cordZ = CommandUtils.readOptionalNumber(args, 2);

        if (dimension != null) {
            WorldServer world = server.getWorld(dimension);
            if (world == null) {
                sender.sendMessage(getNormalMessage(ChatColor.RED + "Couldn't load dimension " + dimension));
                return;
            } else {
                handleWorld(sender, world, cordX, cordZ);
                return;
            }
        }

        for (WorldServer world : server.worlds) {
            handleWorld(sender, world, cordX, cordZ);
        }
    }

    @Override
    protected void init() {
        setDescription(
                getClickableCommandMessage("\u00A72/bd chunkentities <dim>", "/bd chunkentities", false),
                getNormalMessage(" \u00A73Finds all items in the given dim")
        );
    }

    private void handleWorld(ICommandSender sender, WorldServer world, Integer cordX, Integer cordZ) {
        int dimID = world.provider.getDimension();
        BloodDebug.logCommandChat(sender, getClickableCommandMessage("Checking dimension " + dimID, "/bd tpx " + dimID + "0 100 0", true));

        if (cordX != null && cordZ != null) {
            Chunk chunk = world.getChunkProvider().getLoadedChunk(cordX, cordZ);
            if (chunk == null) {
                sender.sendMessage(getNormalMessage(ChatColor.RED + "Couldn't find chunk  [" + cordX + ", " + cordZ + "] or it isn't loaded."));
                return;
            }

            handleChunk(sender, chunk, true, new ArrayList<>());
            return;
        }

        List<CountedListItemGeneric<ITextComponent>> list = new ArrayList<>();
        for (Chunk chunk : world.getChunkProvider().getLoadedChunks()) {
            handleChunk(sender, chunk, false, list);
        }
        list.sort(CountedListItem.COUNTED_LIST_ITEM_COMPARATOR);

        for (CountedListItemGeneric<ITextComponent> iTextComponentCountedListItemGeneric : list) {
            BloodDebug.logCommandChat(sender, iTextComponentCountedListItemGeneric.item);
        }
    }

    private void handleChunk(ICommandSender sender, Chunk chunk, boolean advanced, List<CountedListItemGeneric<ITextComponent>> list) {
        List<Entity> combinedList = new ArrayList<>();
        for (ClassInheritanceMultiMap<Entity> entities : chunk.getEntityLists()) {
            combinedList.addAll(entities);
        }

        if (combinedList.size() <= 0 && advanced) {
            BloodDebug.logCommandChat(sender, getNormalMessage(ChatColor.RED + "No Entities were found in the chunk [" + chunk.x + ", " + chunk.z + "]"));
        }

        if (combinedList.size() > 0) {
            CountedListItemGeneric<ITextComponent> c = new CountedListItemGeneric<>(getClickableCommandMessage("Found " + ChatColor.GOLD + combinedList.size() + ChatColor.RESET + " Entities in the chunk " + ChatColor.GREEN + "[" + chunk.x + ", " + chunk.z + "]" + ChatColor.AQUA + "{" + chunk.getWorld().provider.getDimension() + "}",
                    "/bd chunkentities " + chunk.getWorld().provider.getDimension() + " " + chunk.x + " " + chunk.z, true), combinedList.size());
            list.add(c);

            if (advanced) {
                BloodDebug.logCommandChat(sender, c.item);

                for (Entity entity : combinedList) {
                    BloodDebug.logCommandChat(sender, EntityHelper.getEntityMessage(entity));
                }
            }
        }
    }
}
