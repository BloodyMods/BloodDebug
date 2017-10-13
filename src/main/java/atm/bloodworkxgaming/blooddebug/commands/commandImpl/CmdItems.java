package atm.bloodworkxgaming.blooddebug.commands.commandImpl;

import atm.bloodworkxgaming.blooddebug.BloodDebug;
import atm.bloodworkxgaming.blooddebug.commands.CraftTweakerCommand;
import atm.bloodworkxgaming.blooddebug.entities.EntityCollector;
import atm.bloodworkxgaming.blooddebug.entities.EntityManager;
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

public class CmdItems extends CraftTweakerCommand {
    public CmdItems() {
        super("items");
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityManager manager = new EntityManager();

        // optional dimension
        if (args.length >= 1) {
            Integer dimension = null;
            try {
                if (!Objects.equals(args[0], "*")){
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
            sender.sendMessage(getClickableCommandMessage(
                    "\u00A74No entity with name " + args[0]
                            + ", get names with /bd alle", "/bd alle", true));
        } else {
            String itemNameIn = "";
            if (args.length >= 2){
                itemNameIn = args[1];
            }

            HashMap<String, List<EntityItem>> foundItems = new HashMap<>();

            // Collects the list of all items and maps them to item names
            for (Entity entity : eCollector.entities) {

                EntityItem entityItem = (EntityItem) entity;
                String name = Objects.toString(entityItem.getItem().getItem().getRegistryName()) + ":" + entityItem.getItem().getMetadata();

                List<EntityItem> list = foundItems.getOrDefault(name, new ArrayList<>());
                list.add(entityItem);
                foundItems.put(name, list);
            }

            if (Objects.equals(itemNameIn, "")){
                // Prints list of all Items with given amount
                List<ItemEntityCollected> itemEntityCollecteds = new ArrayList<>();
                foundItems.forEach((s, entityItems) -> itemEntityCollecteds.add(new ItemEntityCollected(entityItems.size(), s)));

                itemEntityCollecteds.sort(ItemEntityCollected.ITEM_ENTITY_COLLECTED_COMPARATOR);

                BloodDebug.logCommandChat(sender, getNormalMessage("Showing \u00A73" + itemEntityCollecteds.size() + " positions \u00A7rof \u00A76ItemEntities"));
                for (ItemEntityCollected itemEntityCollected : itemEntityCollecteds) {
                    String sb = "\u00A7a" + itemEntityCollected.count + "x " + ChatFormatting.RESET + itemEntityCollected.name;
                    BloodDebug.logCommandChat(sender, getClickableCommandMessage(sb, "/bd items " + itemEntityCollected.name, true));
                }
            }else {
                // if a name is specified then it prints out all of those
                List<EntityItem> list = foundItems.getOrDefault(itemNameIn, Collections.emptyList());

                BloodDebug.logCommandChat(sender, getNormalMessage("Showing \u00A73" + list.size() + " positions \u00A7rof \u00A76" + itemNameIn));
                for (EntityItem entityItem : list) {
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
                getClickableCommandMessage("\u00A72/bd findte [name] <dim>", "/bd findte ", false),
                getNormalMessage(" \u00A73Finds the coords of the given TE"),
                getNormalMessage(" \u00A73Name must be provided, optional dimension")
        );
    }

    private static class ItemEntityCollected {
        public static final Comparator<ItemEntityCollected> ITEM_ENTITY_COLLECTED_COMPARATOR = (o1, o2) -> Integer.compare(o2.count, o1.count);

        public int count;
        public String name;

        public ItemEntityCollected(int count, String name) {
            this.count = count;
            this.name = name;
        }
    }

}
