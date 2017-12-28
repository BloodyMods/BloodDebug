package atm.bloodworkxgaming.blooddebug.commands.commandImpl;

import atm.bloodworkxgaming.blooddebug.BloodDebug;
import atm.bloodworkxgaming.blooddebug.commands.BloodDebugCommand;
import atm.bloodworkxgaming.blooddebug.commands.collectors.entities.EntityCollector;
import atm.bloodworkxgaming.blooddebug.commands.collectors.entities.EntityManager;
import atm.bloodworkxgaming.blooddebug.util.ChatColor;
import atm.bloodworkxgaming.blooddebug.util.CountedListItem;
import atm.bloodworkxgaming.blooddebug.util.CountedListItemGeneric;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
            List<CountedListItemGeneric<String>> itemEntityCollecteds = new ArrayList<>();
            foundItems.forEach((s, entityItems) -> itemEntityCollecteds.add(new CountedListItemGeneric<>(s, entityItems.size())));
            itemEntityCollecteds.sort(CountedListItem.COUNTED_LIST_ITEM_COMPARATOR);

            BloodDebug.logCommandChat(sender, getNormalMessage("Showing \u00A73" + itemEntityCollecteds.size() + " amount \u00A7rof \u00A76ItemEntities"));
            for (CountedListItemGeneric<String> itemEntityCollected : itemEntityCollecteds) {
                String sb = "\u00A7a" + itemEntityCollected.count + "x " + ChatColor.RESET + itemEntityCollected.item;
                BloodDebug.logCommandChat(sender, getClickableCommandMessage(sb, "/bd finditems " + itemEntityCollected.item + " " + (dimension == null ? "" : dimension), true));
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
}
