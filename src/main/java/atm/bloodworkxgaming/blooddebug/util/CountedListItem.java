package atm.bloodworkxgaming.blooddebug.util;

import java.util.Comparator;

public class CountedListItem {
    public static final Comparator<CountedListItem> COUNTED_LIST_ITEM_COMPARATOR = Comparator.comparingInt(o -> o.count);

    public int count;

    public CountedListItem(int count) {
        this.count = count;
    }
}
