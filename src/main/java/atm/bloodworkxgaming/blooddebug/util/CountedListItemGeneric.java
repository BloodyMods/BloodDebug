package atm.bloodworkxgaming.blooddebug.util;

public class CountedListItemGeneric<T> extends CountedListItem {
    public T item;

    public CountedListItemGeneric(T item) {
        super(0);
        this.item = item;
    }

    public CountedListItemGeneric(T item, int count) {
        super(count);
        this.item = item;
    }
}
