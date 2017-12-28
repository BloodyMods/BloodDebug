package atm.bloodworkxgaming.blooddebug.commands;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import static atm.bloodworkxgaming.blooddebug.commands.ClipboardHelper.copyCommandBase;


/**
 * @author BloodWorkXGaming
 */
public class SpecialMessagesChat {

    public static final ITextComponent EMPTY_TEXTMESSAGE = new TextComponentString("");

    public static ITextComponent getClickableCommandMessage(String message, String command, boolean runDirectly) {

        Style style = new Style();
        ClickEvent click = new ClickEvent(runDirectly ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND, command);
        style.setClickEvent(click);

        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to execute [\u00A76" + command + "\u00A7r]"));
        style.setHoverEvent(hoverEvent);

        return new TextComponentString(message).setStyle(style);
    }

    public static ITextComponent getClickableBrowserLinkMessage(String message, String url) {

        Style style = new Style();
        ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
        style.setClickEvent(click);
        style.setColor(TextFormatting.AQUA);
        style.setUnderlined(true);

        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to Open [\u00A76" + url + "\u00A7r]"));
        style.setHoverEvent(hoverEvent);

        return new TextComponentString(message).setStyle(style);
    }

    public static ITextComponent getFileOpenMessage(String message, String filepath) {

        Style style = new Style();
        ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_FILE, filepath);
        style.setClickEvent(click);

        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to open [\u00A76" + filepath + "\u00A7r]"));
        style.setHoverEvent(hoverEvent);

        return new TextComponentString(message).setStyle(style);
    }

    public static ITextComponent getNormalMessage(String message) {
        return new TextComponentString(message);
    }

    public static ITextComponent getCopyMessage(String message, String copyMessage) {
        Style style = new Style();
        ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, copyCommandBase + copyMessage);
        style.setClickEvent(click);

        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to copy [\u00A76" + copyMessage + "\u00A7r]"));
        style.setHoverEvent(hoverEvent);

        return new TextComponentString(message).setStyle(style);
    }
}
