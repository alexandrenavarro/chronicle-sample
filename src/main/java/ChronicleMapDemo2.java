import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.openhft.chronicle.hash.Data;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import net.openhft.chronicle.map.MapEntryOperations;
import net.openhft.chronicle.map.MapMethods;
import net.openhft.chronicle.map.replication.MapRemoteOperations;
import net.openhft.chronicle.map.replication.MapRemoteQueryContext;
import net.openhft.lang.model.DataValueClasses;

/**
 * <p>Main. </p>
 *
 * @author anavarro - Sep 6, 2015
 *
 */
public final class ChronicleMapDemo2 {

    /**
     * main.
     *
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

        final ChronicleMap<String, DefaultPrice> map = ChronicleMapBuilder.of(String.class, DefaultPrice.class).remoteOperations(new MapRemoteOperations() {
            @Override
            public void put(MapRemoteQueryContext aQ, Data aNewValue) {
                System.out.println("put");
                MapRemoteOperations.super.put(aQ, aNewValue);
                
            }
            @Override
            public void remove(MapRemoteQueryContext aQ) {
                System.out.println("remove");
                MapRemoteOperations.super.remove(aQ);
            }
        }
        ).createPersistedTo(new File("tmp1.txt"));
        


        System.out.println("map=" + map.size());
        
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            
        }
        

    }

}
