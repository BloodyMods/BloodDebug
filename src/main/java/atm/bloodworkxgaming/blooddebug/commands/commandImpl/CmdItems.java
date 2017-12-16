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

import java.util.*;

import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getClickableCommandMessage;
import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getNormalMessage;

public class CmdItems extends BloodDebugCommand {
    public CmdItems() {
        super("items");
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityManager manager = new EntityManager();

        Integer dimension = null;
        // optional dimension
        if (args.length >= 1) {
            try {
                if (!Objects.equals(args[0], "*")) {
                    dimension = Integer.valueOf(args[0]);
                }
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
            HashMap<String, List<EntityItem>> foundItems = new HashMap<>();

            // Collects the list of all items and maps them to item names
            for (Entity entity : eCollector.entities) {
                EntityItem entityItem = (EntityItem) entity;
                String name = Objects.toString(entityItem.getItem().getItem().getRegistryName()) + ":" + entityItem.getItem().getMetadata();

                List<EntityItem> list = foundItems.getOrDefault(name, new ArrayList<>());
                list.add(entityItem);
                foundItems.put(name, list);
            }


            // Prints list of all Items with given amount
            List<ItemEntityCollected> itemEntityCollecteds = new ArrayList<>();
            foundItems.forEach((s, entityItems) -> itemEntityCollecteds.add(new ItemEntityCollected(entityItems.size(), s)));

            itemEntityCollecteds.sort(ItemEntityCollected.ITEM_ENTITY_COLLECTED_COMPARATOR);

            BloodDebug.logCommandChat(sender, getNormalMessage("Showing \u00A73" + itemEntityCollecteds.size() + " amount \u00A7rof \u00A76ItemEntities"));
            for (ItemEntityCollected itemEntityCollected : itemEntityCollecteds) {
                String sb = "\u00A7a" + itemEntityCollected.count + "x " + ChatFormatting.RESET + itemEntityCollected.name;
                BloodDebug.logCommandChat(sender, getClickableCommandMessage(sb, "/bd finditems " + itemEntityCollected.name + " " + (dimension == null ? "" : dimension), true));
            }
        }

    }

    @Override
    protected void init() {
        setDescription(
                getClickableCommandMessage("\u00A72/bd items <dim>", "/bd items", false),
                getNormalMessage(" \u00A73Finds all items in the given dim")
        );
    }

    private static class ItemEntityCollected {
        static final Comparator<ItemEntityCollected> ITEM_ENTITY_COLLECTED_COMPARATOR = (o1, o2) -> Integer.compare(o2.count, o1.count);

        int count;
        String name;

        ItemEntityCollected(int count, String name) {
            this.count = count;
            this.name = name;
        }
    }

}
