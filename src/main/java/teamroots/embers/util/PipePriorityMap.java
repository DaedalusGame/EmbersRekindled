package teamroots.embers.util;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class PipePriorityMap<K,V> {
    //TODO: examine if we can use a PriorityQueue instead
    TreeMap<K,ArrayList<V>> map = new TreeMap<>();

    public void put(K key, V value){
        ArrayList<V> list = map.get(key);
        if(list == null) {
            list = new ArrayList<>();
            map.put(key,list);
        }
        list.add(value);
    }

    public ArrayList<V> get(K key) {
        return map.get(key);
    }

    public Set<K> keySet() {
        return map.keySet();
    }
}
