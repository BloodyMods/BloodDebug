package atm.bloodworkxgaming.blooddebug.commands;

import atm.bloodworkxgaming.blooddebug.BloodDebug;
import atm.bloodworkxgaming.blooddebug.CustomTeleporter;
import atm.bloodworkxgaming.blooddebug.commands.commandImpl.CmdItems;
import atm.bloodworkxgaming.blooddebug.entities.EntityCollector;
import atm.bloodworkxgaming.blooddebug.entities.EntityManager;
import atm.bloodworkxgaming.blooddebug.gui.TestFX;
import atm.bloodworkxgaming.blooddebug.tiles.TileCollector;
import atm.bloodworkxgaming.blooddebug.tiles.TileManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import javafx.stage.Stage;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getClickableCommandMessage;
import static atm.bloodworkxgaming.blooddebug.commands.SpecialMessagesChat.getNormalMessage;

/**
 * @author BloodWorkXGaming
 */
public class Commands {
    public static final NumberFormat DF = DecimalFormat.getInstance();

    static {
        DF.setMinimumFractionDigits(1);
        DF.setMaximumFractionDigits(3);
    }


    static void registerCommands() {

        BDChatCommand.registerCommand(new CraftTweakerCommand("help") {
            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
                BDChatCommand.sendUsage(sender);
            }

            @Override
            protected void init() {
                setDescription(getClickableCommandMessage("\u00A72/ct help", "/ct help", true), getNormalMessage(" \u00A73Prints out the this help page"));
            }
        });

        // TEs
        BDChatCommand.registerCommand(new CraftTweakerCommand("allte") {
            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
                TileManager manager = new TileManager();

                Integer dimension = null;
                if (args.length >= 1) {
                    try {
                        dimension = Integer.valueOf(args[0]);
                    } finally {
                        manager.collectTileList(dimension);
                    }
                } else {
                    manager.collectTileList(null);
                }

                List<TileCollector> list = manager.getSortedList();
                for (TileCollector tileCollector : list) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("\u00A76").append(tileCollector.getCount()).append(" * \u00A7r").append(tileCollector.getClassName());
                    if (tileCollector.isTicking()) {
                        sb.append("\u00A7c [ticking]");
                    }

                    BloodDebug.logCommandChat(sender, getClickableCommandMessage(
                            sb.toString(),
                            "/bd findte " + tileCollector.getClassName() + (dimension != null ? " " + dimension : ""), true));
                }

                BloodDebug.logCommandChat(sender,
                        getNormalMessage(
                                "Found \u00A73" + manager.getTotalCount() + " Tiles \u00A7rof which \u00A7b"
                                        + manager.getTotalCountTicking() + " are ticking"
                                        + (dimension == null ? "\u00A7r in \u00A7dall Dims." : "\u00A7r in \u00A7dDim " + dimension + ".")));
            }

            @Override
            protected void init() {
                setDescription(
                        getClickableCommandMessage("\u00A72/bd allte <dim>", "/bd allte", true),
                        getNormalMessage(" \u00A73Logs all TileEntities"),
                        getNormalMessage(" \u00A73Dim ID can be specified optionally"));
            }
        });

        BDChatCommand.registerCommand(new CraftTweakerCommand("findte") {
            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
                TileManager manager = new TileManager();

                // optional dimension
                if (args.length >= 2) {
                    Integer dimension = null;
                    try {
                        dimension = Integer.valueOf(args[1]);
                    } finally {
                        manager.collectTileList(dimension);
                    }
                } else {
                    manager.collectTileList(null);
                }

                if (args.length > 0) {
                    TileCollector teCollector = manager.tileEntityHashMap.get(args[0]);

                    if (teCollector == null) {
                        sender.sendMessage(getClickableCommandMessage(
                                "\u00A74No tile with name " + args[0]
                                        + ", get names with /bd allte", "/bd allte", true));
                    } else {
                        BloodDebug.logCommandChat(sender, getNormalMessage("Showing \u00A73" + teCollector.getCount() + " positions \u00A7rof \u00A76" + teCollector.getClassName()));

                        for (TileEntity tile : teCollector.tiles) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("\u00A7e- ");
                            BlockPos pos = tile.getPos();
                            sb.append("\u00A73[").append(pos.getX()).append(", ").append(pos.getY()).append(", ").append(pos.getZ()).append("]");
                            sb.append("\u00A7a{").append(tile.getWorld().provider.getDimension()).append("}");

                            BloodDebug.logCommandChat(sender, getClickableCommandMessage(sb.toString(), "/tp @p " + pos.getX() + " " + pos.getY() + " " + pos.getZ(), true));

                        }
                    }


                } else {
                    sender.sendMessage(getNormalMessage("\u00A74You must provide the name of a TE class"));
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
        });

        // Es
        BDChatCommand.registerCommand(new CraftTweakerCommand("alle") {
            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
                EntityManager manager = new EntityManager();

                Integer dimension = null;
                if (args.length >= 1) {
                    try {
                        dimension = Integer.valueOf(args[0]);
                    } finally {
                        manager.collectEntityList(dimension);
                    }
                } else {
                    manager.collectEntityList(null);
                }

                List<EntityCollector> list = manager.getSortedList();
                for (EntityCollector tileCollector : list) {
                    BloodDebug.logCommandChat(sender, getClickableCommandMessage(
                            "\u00A76" + tileCollector.getCount() + " * \u00A7r" + tileCollector.getClassName(),
                            "/bd finde " + tileCollector.getClassName() + (dimension != null ? " " + dimension : ""), true));
                }

                BloodDebug.logCommandChat(sender,
                        getNormalMessage(
                                "Found \u00A73" + manager.getTotalCount() + " Entities \u00A7r"
                                        + (dimension == null ? "\u00A7r in \u00A7dall Dims." : "\u00A7r in \u00A7dDim " + dimension + ".")));
            }

            @Override
            protected void init() {
                setDescription(
                        getClickableCommandMessage("\u00A72/bd alle <dim>", "/bd alle", true),
                        getNormalMessage(" \u00A73Logs all Entities"),
                        getNormalMessage(" \u00A73Dim ID can be specified optionally"));
            }
        });

        BDChatCommand.registerCommand(new CraftTweakerCommand("finde") {
            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
                EntityManager manager = new EntityManager();

                // optional dimension
                if (args.length >= 2) {
                    Integer dimension = null;
                    try {
                        dimension = Integer.valueOf(args[1]);
                    } finally {
                        manager.collectEntityList(dimension);
                    }
                } else {
                    manager.collectEntityList(null);
                }

                if (args.length > 0) {
                    EntityCollector eCollector = manager.entityHashMap.get(args[0]);

                    if (eCollector == null) {
                        sender.sendMessage(getClickableCommandMessage(
                                "\u00A74No entity with name " + args[0]
                                        + ", get names with /bd alle", "/bd alle", true));
                    } else {
                        BloodDebug.logCommandChat(sender, getNormalMessage("Showing \u00A73" + eCollector.getCount() + " positions \u00A7rof \u00A76" + eCollector.getClassName()));

                        for (Entity entity : eCollector.entities) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("\u00A7e- ");
                            BlockPos pos = entity.getPosition();
                            sb.append("\u00A73[").append(DF.format(entity.posX)).append(", ").append(DF.format(entity.posY)).append(", ").append(DF.format(entity.posZ)).append("]");
                            sb.append("\u00A7a{").append(entity.dimension).append("}");

                            BloodDebug.logCommandChat(sender, getClickableCommandMessage(sb.toString(), "/tp @p " + pos.getX() + " " + pos.getY() + " " + pos.getZ(), true));

                        }
                    }


                } else {
                    sender.sendMessage(getNormalMessage("\u00A74You must provide the name of a Entity class"));
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
        });

        // bd items [dim: {'*' | int}] [@Optional itemname: String]
        BDChatCommand.registerCommand(new CmdItems());

        BDChatCommand.registerCommand(new CraftTweakerCommand("window") {
            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {

                new Thread("JavaFX_mc_thread") {
                    @Override
                    public void run() {
                        TestFX testFX = new TestFX();
                        Stage stage = new Stage();
                        testFX.start(stage);

                        // Application.launch(TestFX.class);
                    }
                }.start();

            }

            @Override
            protected void init() {
                setDescription(
                        getClickableCommandMessage("\u00A72/bd window", "/bd window", true),
                        getNormalMessage(" \u00A73Logs all TileEntities"));
            }
        });

        BDChatCommand.registerCommand(new CraftTweakerCommand("tpx") {
            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
                Entity entityToTeleport = null;
                Integer dimension = null;
                int x = 0;
                int y = 100;
                int z = 0;

                if (args.length == 0) {
                    sender.sendMessage(getNormalMessage("No arguments provided"));
                }

                if (args.length == 4 || args.length == 1) {
                    Entity senderEntity = sender.getCommandSenderEntity();
                    if (senderEntity == null) {
                        sender.sendMessage(getNormalMessage("Sender Entity is invalid"));
                        return;
                    } else {
                        entityToTeleport = senderEntity;

                        try {
                            dimension = Integer.valueOf(args[0]);

                            if (args.length == 4) {
                                x = Integer.valueOf(args[1]);
                                y = Integer.valueOf(args[2]);
                                z = Integer.valueOf(args[3]);
                            } else {
                                x = 0;
                                y = 100;
                                z = 0;
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage(getNormalMessage("Wrong number format"));
                            return;
                        }
                    }
                }


                if (args.length >= 5 || args.length == 2) {
                    EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[0]);

                    if (player == null) {
                        sender.sendMessage(getNormalMessage("No player with that name found"));
                        return;
                    } else {
                        entityToTeleport = player;

                        try {
                            dimension = Integer.valueOf(args[0]);

                            if (args.length >= 5) {
                                x = Integer.valueOf(args[2]);
                                y = Integer.valueOf(args[3]);
                                z = Integer.valueOf(args[4]);
                            } else {
                                x = 0;
                                y = 100;
                                z = 0;
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage(getNormalMessage("Wrong number format"));
                            return;
                        }
                    }
                }

                if (entityToTeleport != null && dimension != null) {
                    int oldID = entityToTeleport.dimension;

                    entityToTeleport.posX = x;
                    entityToTeleport.posY = y;
                    entityToTeleport.posZ = z;


                    if (entityToTeleport instanceof EntityPlayer) {
                        server.getPlayerList().transferPlayerToDimension((EntityPlayerMP) entityToTeleport, dimension, new CustomTeleporter(((EntityPlayerMP) entityToTeleport).getServerWorld()));
                        FMLCommonHandler.instance().firePlayerChangedDimensionEvent((EntityPlayer) entityToTeleport, entityToTeleport.dimension, dimension);
                    } else {
                        sender.sendMessage(getNormalMessage("Entity to teleport must be a Player"));
                    }

                } else {
                    sender.sendMessage(getNormalMessage("Something went wrong"));
                }
            }

            @Override
            protected void init() {
                setDescription(
                        getClickableCommandMessage("\u00A72/bd tpx <name> [dim] [x] [y] [z]", "/bd tpx ", false),
                        getNormalMessage(" \u00A73Teleports the player across dimensions"),
                        getNormalMessage(" \u00A73Teleports the player across dimensions")
                );
            }
        });

    }
}
