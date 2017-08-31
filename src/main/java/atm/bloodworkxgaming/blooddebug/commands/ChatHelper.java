package atm.bloodworkxgaming.blooddebug.commands;

public class ChatHelper {

    /**
     * Removes any formatting codes like §c for red
     */
    public static String removeFormattingCodes(String s){
        StringBuilder sb = new StringBuilder();
        String[] split = s.split("\u00A7.");
        for (String s1 : split) {
            sb.append(s1);
        }
        return sb.toString();
    }
}
