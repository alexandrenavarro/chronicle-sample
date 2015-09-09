import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import net.openhft.chronicle.map.MapEntryOperations;
import net.openhft.chronicle.map.MapMethods;
import net.openhft.lang.model.DataValueClasses;

/**
 * <p>Main. </p>
 *
 * @author anavarro - Sep 6, 2015
 *
 */
public final class ChronicleMapDemo {

    /**
     * main.
     *
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

        final ChronicleMap<String, DefaultPrice> map = ChronicleMapBuilder.of(String.class, DefaultPrice.class).entryOperations(new MapEntryOperations<String, DefaultPrice, Void>() {
            public  Void insert(net.openhft.chronicle.map.MapAbsentEntry<String,DefaultPrice> absentEntry, net.openhft.chronicle.hash.Data<DefaultPrice> value) {
                //System.out.println("insert + " + value);
                absentEntry.doInsert(value);
                return null;
            };
            public Void remove(net.openhft.chronicle.map.MapEntry<String,DefaultPrice> entry) {
                //System.out.println("remove + " + entry);
                entry.doRemove();
                return null;
            };
            public Void replaceValue(net.openhft.chronicle.map.MapEntry<String,DefaultPrice> entry, net.openhft.chronicle.hash.Data<DefaultPrice> newValue) {
                //System.out.println("replaceValue + " + newValue);
                entry.doReplaceValue(newValue);
                return null;
            };
        }
        ).createPersistedTo(new File("tmp1.txt"));
        

        map.put("s1", new DefaultPrice(1.0, 1.1));

        System.out.println("map=" + map);

        DefaultPrice currentPrice = new DefaultPrice(0, 0);

        currentPrice = map.getUsing("s1", currentPrice);
        
        currentPrice.setAsk(11);
        
        map.put("s1", currentPrice);
        map.put("s1", currentPrice);
        
        
        System.out.println("map=" + map);
        
        
        System.out.println("ChronicleMap");
        int nbInsert = 100000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < nbInsert; i++) {
            currentPrice.setBid(i - 0.1);
            currentPrice.setAsk(i + 0.1);
            currentPrice.setTimestamp(System.currentTimeMillis());
            map.put("s"+ (i % 10000), currentPrice);
        }
        long stop = System.currentTimeMillis();
        System.out.println("nbInsert/s=" + nbInsert * 1000 / (stop - start) );
        
        int nbGet = 100000;
        double mid = 0;
        start = System.currentTimeMillis();
        for (int i = 0; i < nbInsert; i++) {
            map.getUsing("s"+ (i % 10000), currentPrice);
            mid =+ currentPrice.getBid() + currentPrice.getAsk() / 2;
        }
        stop = System.currentTimeMillis();
        System.out.println("nbGet/s=" + nbGet * 1000 / (stop - start) +  " mid=" + mid);
        
        
        System.out.println("ConcurrentHashMap");
        ConcurrentHashMap<String, DefaultPrice> map1 = new ConcurrentHashMap<String, DefaultPrice>();
        start = System.currentTimeMillis();
        for (int i = 0; i < nbInsert; i++) {
            currentPrice.setBid(i - 0.1);
            currentPrice.setAsk(i + 0.1);
            currentPrice.setTimestamp(System.currentTimeMillis());
            map1.put("s"+ (i % 10000), currentPrice);
        }
        stop = System.currentTimeMillis();
        System.out.println("nbInsert/s=" + nbInsert * 1000 / (stop - start));
        
        nbGet = 100000;
        mid = 0;
        start = System.currentTimeMillis();
        for (int i = 0; i < nbInsert; i++) {
            DefaultPrice price = map1.get("s"+ (i % 10000));
            mid =+ price.getBid() + price.getAsk() / 2;
        }
        stop = System.currentTimeMillis();
        System.out.println("nbGet/s=" + nbGet * 1000 / (stop - start)  +  " mid=" + mid);
        
        
        
        
        final ChronicleMap<String, Price> map2 = ChronicleMapBuilder.of(String.class, Price.class).create();
        
        
//        final Price price = DataValueClasses.newDirectInstance(Price.class);
        
//        price.setBid(2.1);
//        price.setAsk(2.2);
//        
//        map2.put("key", price);
//        
//        System.out.println("map2=" + map);
        
        

    }

}
