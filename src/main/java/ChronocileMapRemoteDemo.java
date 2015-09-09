import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.openhft.chronicle.hash.replication.TcpTransportAndNetworkConfig;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import net.openhft.chronicle.map.MapEntryOperations;

/**
 * <p>Main. </p>
 *
 * @author anavarro - Sep 6, 2015
 *
 */
public class ChronocileMapRemoteDemo {

    
    private static final Set<Integer> list = new HashSet<Integer>(); 
    /**
     * main.
     *
     * @param args
     */
    /**
     * main.
     *
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {

        Map map1;
        Map map2;

        
//  ----------  SERVER1 1 ----------
        {

            // we connect the maps via a TCP/IP socket connection on port 8077

            TcpTransportAndNetworkConfig tcpConfig = TcpTransportAndNetworkConfig
                    .of(8076, new InetSocketAddress("localhost", 8077))
                    .heartBeatInterval(500, TimeUnit.MILLISECONDS);
            ChronicleMapBuilder<Integer, Long> map1Builder =
                    ChronicleMapBuilder.of(Integer.class, Long.class)
                            .entries(100000L)
                            .entryOperations(new MapEntryOperations<Integer, Long, Void>() {
                                public  Void insert(net.openhft.chronicle.map.MapAbsentEntry<Integer, Long> absentEntry, net.openhft.chronicle.hash.Data<Long> value) {
                                    //System.out.println("insert + value=" + value.get() + " latency=" + (System.currentTimeMillis() - value.get()));
                                    list.add(absentEntry.absentKey().get());
                                    absentEntry.doInsert(value);
                                    
                                    return null;
                                };
                                public Void remove(net.openhft.chronicle.map.MapEntry<Integer, Long> entry) {
                                    System.out.println("remove + " + entry);
                                    entry.doRemove();
                                    return null;
                                };
                                public Void replaceValue(net.openhft.chronicle.map.MapEntry<Integer, Long> entry, net.openhft.chronicle.hash.Data<Long> newValue) {
                                    //System.out.println("replaceValue + " + (System.currentTimeMillis() - newValue.get()));
                                    list.add(entry.key().get());
                                    entry.doReplaceValue(newValue);
                                    return null;
                                };
                            })
                            .replication((byte) 1, tcpConfig);

            map1 = map1Builder.create();
        }
//  ----------  SERVER2 on the same server as ----------

        {
            TcpTransportAndNetworkConfig tcpConfig =
                    TcpTransportAndNetworkConfig.of(8077)
                    .heartBeatInterval(2000, TimeUnit.MILLISECONDS);
            ChronicleMapBuilder<Integer, Long> map2Builder =
                    ChronicleMapBuilder.of(Integer.class, Long.class)
                            .entries(100000L)
                            .entryOperations(new MapEntryOperations<Integer, Long, Void>() {
                                public  Void insert(net.openhft.chronicle.map.MapAbsentEntry<Integer, Long> absentEntry, net.openhft.chronicle.hash.Data<Long> value) {
                                    System.out.println("insert 2 + value=" + value.get() + " latency=" + (System.currentTimeMillis() - value.get()));
                                    absentEntry.doInsert(value);
                                    return null;
                                };
                                public Void remove(net.openhft.chronicle.map.MapEntry<Integer, Long> entry) {
                                    System.out.println("remove 2 + " + entry);
                                    entry.doRemove();
                                    return null;
                                };
                                public Void replaceValue(net.openhft.chronicle.map.MapEntry<Integer, Long> entry, net.openhft.chronicle.hash.Data<Long> newValue) {
                                    System.out.println("replaceValue 2 + " + (System.currentTimeMillis() - newValue.get()));
                                    entry.doReplaceValue(newValue);
                                    return null;
                                };
                            })
                            .replication((byte) 2, tcpConfig);
            map2 = map2Builder.create();
            
            

//            ChronicleMapBuilder<Integer, Long> statelessMap = ChronicleMapBuilder.of(Integer.class, Long.class)
//                    .statelessClient(new InetSocketAddress("localhost", 8076))
//                    .create();

            

            // we will stores some data into one map here
            for (int i = 0; i < 10000; i++) {
                map2.put(i, System.currentTimeMillis());
            }
            
            System.out.println("list=" + list);
            Thread.sleep(1000);
            System.out.println("list=" + list);
            Thread.sleep(10000);
            System.out.println("list=" + list);
            Thread.sleep(1000000);
            
            
        }
        
        

    }

}
