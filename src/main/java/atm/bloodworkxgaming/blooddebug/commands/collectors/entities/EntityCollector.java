package atm.bloodworkxgaming.blooddebug.commands.collectors.entities;


import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityCollector {
    public Class<? extends Entity> entityClass;
    public List<Entity> entities = new ArrayList<>();
    private int count;

    public EntityCollector(Entity entity) {
        entityClass = entity.getClass();
        entities.add(entity);
        count = 1;
    }

    public int getCount() {
        return count;
    }

    public void addTE(Entity e) {
        entities.add(e);
        count = entities.size();
    }

    public String getClassName() {
        return entityClass.getName();
    }

    @Override
    public String toString() {
        return String.valueOf(count) + " * " + getClassName();
    }
}
