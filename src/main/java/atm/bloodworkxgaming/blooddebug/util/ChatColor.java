package atm.bloodworkxgaming.blooddebug.util;


import java.util.*;
import java.util.regex.Pattern;

public enum ChatColor {
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    OBFUSCATED('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),
    RESET('r');

    public static final char PREFIX_CODE = '\u00A7';
    private static final Map<Character, ChatColor> FORMATTING_BY_CHAR = new HashMap<>();
    private static final Map<String, ChatColor> FORMATTING_BY_NAME = new HashMap<>();
    private static final Pattern STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");

    static {
        for (ChatColor color : values()) {
            FORMATTING_BY_CHAR.put(color.getChar(), color);
            FORMATTING_BY_NAME.put(color.getName(), color);
        }
    }

    private final char code;
    private final boolean isFormat;
    private final String toString;

    ChatColor(char code) {
        this(code, false);
    }

    ChatColor(char code, boolean isFormat) {
        this.code = code;
        this.isFormat = isFormat;
        this.toString = "\u00A7" + code;
    }

    public static String stripFormatting(String input) {
        return input == null ? null : STRIP_FORMATTING_PATTERN.matcher(input).replaceAll("");
    }

    public static ChatColor getByChar(char code) {
        return FORMATTING_BY_CHAR.get(code);
    }

    public static ChatColor getByName(String name) {
        return name == null ? null : FORMATTING_BY_NAME.get(name.toLowerCase(Locale.ROOT));
    }

    public static Collection<String> getNames(boolean getColors, boolean getFormats) {
        List<String> result = new ArrayList<>();

        for (ChatColor format : values()) {
            if ((!format.isColor() || getColors) && (!format.isFormat() || getFormats)) {
                result.add(format.getName());
            }
        }

        return result;
    }

    public char getChar() {
        return this.code;
    }

    public boolean isFormat() {
        return this.isFormat;
    }

    public boolean isColor() {
        return !this.isFormat && this != RESET;
    }

    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String toString() {
        return this.toString;
    }

}
