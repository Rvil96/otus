package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(MyCache.class);

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        var actionMessage = "remove an object from the cache";
        notifyListeners(key, value, actionMessage);
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        var actionMessage = "remove an object from the cache";
        notifyListeners(key, value, actionMessage);
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        var actionMessage = "getting an object from the cache";
        notifyListeners(key, value, actionMessage);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String actionMessage) {
        if (!listeners.isEmpty()) {
            for (HwListener<K, V> listener : listeners) {
                try {
                    listener.notify(key, value, actionMessage);
                } catch (Exception e) {
                    log.error("Listener error", e);
                }
            }
        }
    }
}
