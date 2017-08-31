package atm.bloodworkxgaming.blooddebug.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.ArrayList;
import java.util.List;

public class TileCollector {
    public Class<? extends TileEntity> tileClass;
    public List<TileEntity> tiles = new ArrayList<>();
    private int count;
    private boolean isTicking;

    public TileCollector(TileEntity tileEntity){
        tileClass = tileEntity.getClass();
        tiles.add(tileEntity);
        count = 1;
        isTicking = tileEntity instanceof ITickable;
    }

    public boolean isTicking() {
        return isTicking;
    }

    public int getCount() {
        return count;
    }

    public void addTE(TileEntity e){
        tiles.add(e);
        count = tiles.size();
    }

    public String getClassName(){
        return tileClass.getName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(count).append(" * ").append(getClassName());
        if (isTicking){
            sb.append(" [ticking]");
        }
        return sb.toString();
    }
}
