package fr.communaywen.core.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class Queue<K, V> {

    private final LinkedHashMap<K, V> queue;

    public Queue(int size) {
        this.queue = new LinkedHashMap<K, V>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                return size() > size;
            }
        };
    }

    public void add(K key, V value) {
        queue.put(key, value);
    }

    public void remove(K key) {
        queue.remove(key);
    }

    public V get(K key) {
        return queue.get(key);
    }

    public LinkedHashMap<K, V> getQueue() {
        return queue;
    }
}
