package atm.bloodworkxgaming.blooddebug.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.Serializable;
import java.util.*;

public class TileManager {
    public static final TileCollectorComparator TILE_COLLECTOR_COMPARATOR = new TileCollectorComparator();
    public HashMap<String, TileCollector> tileEntityHashMap = new HashMap<>();

    private int totalCount = 0;
    private int totalCountTicking = 0;
    private List<TileCollector> sortedList = null;

    private World[] worlds;

    public TileManager(){
        worlds = DimensionManager.getWorlds();
    }

    /**
     * Collects the data from the world instance, has to be called to be useable. resets other data before continuing
     */
    public void collectTileList(Integer dim){
        totalCount = 0;
        totalCountTicking = 0;
        tileEntityHashMap.clear();
        sortedList = null;

        for (World world : worlds) {
            if (dim == null || world.provider.getDimension() == dim){
                List<TileEntity> tileList = world.loadedTileEntityList;

                for (TileEntity tileEntity : tileList) {
                    totalCount++;
                    if (tileEntity instanceof ITickable){
                        totalCountTicking++;
                    }

                    String className = tileEntity.getClass().getName();

                    if (tileEntityHashMap.containsKey(className)){
                        tileEntityHashMap.get(className).addTE(tileEntity);
                    }else {
                        tileEntityHashMap.put(className, new TileCollector(tileEntity));
                    }
                }
            }
        }
    }

    /**
     * Gets a sorted list, if it already has one cached it doesn't sort a new one
     */
    public List<TileCollector> getSortedList(){
        if (sortedList == null){
            List<TileCollector> values = new ArrayList<>();

            for (Map.Entry<String, TileCollector> stringListEntry : tileEntityHashMap.entrySet()) {
                values.add(stringListEntry.getValue());
            }

            values.sort(TILE_COLLECTOR_COMPARATOR);
            sortedList = values;
        }

        return sortedList;

    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalCountTicking() {
        return totalCountTicking;
    }


    private static class TileCollectorComparator implements Comparator<TileCollector>, Serializable {
        @Override
        public int compare(TileCollector o1, TileCollector o2) {
            return Integer.compare(o1.getCount(), o2.getCount());
        }
    }
}
