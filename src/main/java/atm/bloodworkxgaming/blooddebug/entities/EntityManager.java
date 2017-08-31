package atm.bloodworkxgaming.blooddebug.entities;

import net.minecraft.entity.Entity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.Serializable;
import java.util.*;

public class EntityManager {
    public static final EntityCollectorComparator TILE_COLLECTOR_COMPARATOR = new EntityCollectorComparator();
    public HashMap<String, EntityCollector> entityHashMap = new HashMap<>();

    private int totalCount = 0;
    private int totalCountTicking = 0;
    private List<EntityCollector> sortedList = null;

    private World[] worlds;

    public EntityManager(){
        worlds = DimensionManager.getWorlds();
    }

    /**
     * Collects the data from the world instance, has to be called to be useable. resets other data before continuing
     */
    public void collectEntityList(Integer dim){
        totalCount = 0;
        totalCountTicking = 0;
        entityHashMap.clear();
        sortedList = null;

        for (World world : worlds) {
            if (dim == null || world.provider.getDimension() == dim){
                List<Entity> entityList = world.loadedEntityList;

                for (Entity entityEntity : entityList) {
                    totalCount++;
                    if (entityEntity instanceof ITickable){
                        totalCountTicking++;
                    }

                    String className = entityEntity.getClass().getName();

                    if (entityHashMap.containsKey(className)){
                        entityHashMap.get(className).addTE(entityEntity);
                    }else {
                        entityHashMap.put(className, new EntityCollector(entityEntity));
                    }
                }
            }
        }
    }

    /**
     * Gets a sorted list, if it already has one cached it doesn't sort a new one
     */
    public List<EntityCollector> getSortedList(){
        if (sortedList == null){
            List<EntityCollector> values = new ArrayList<>();

            for (Map.Entry<String, EntityCollector> stringListEntry : entityHashMap.entrySet()) {
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


    private static class EntityCollectorComparator implements Comparator<EntityCollector>, Serializable {
        @Override
        public int compare(EntityCollector o1, EntityCollector o2) {
            return Integer.compare(o1.getCount(), o2.getCount());
        }
    }
}
