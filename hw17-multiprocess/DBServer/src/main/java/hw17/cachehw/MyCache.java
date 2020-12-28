package hw17.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final WeakHashMap<K, V> cache = new WeakHashMap<>();
    private final ReferenceQueue<HwListener<K, V>> refQueue = new ReferenceQueue<>();
    private final List<Reference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        logger.debug("Put {} : {}", key, value);
        if (key == null) {
            return;
        }
        cache.put(key, value);
        notifyAllListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        logger.debug("Remove key {}", key);
        if (key == null) {
            return;
        }
        final var value = cache.remove(key);
        notifyAllListeners(key, value, "remove");
    }

    @Override
    public V get(K key) {
        logger.debug("Get key {} ", key);
        if (key == null) {
            return null;
        }
        final var value = cache.get(key);
        if (!cache.containsKey(key)) {
            throw new HwCacheException("Key " + key + " not exist in the cache");
        }
        notifyAllListeners(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        logger.debug("Add listener {}", listener);
        if (listener == null) {
            throw new HwCacheException("New listener is null");
        }
        final var refListener = new SoftReference<>(listener, refQueue);
        listeners.add(refListener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        logger.debug("Remove listener {}", listener);
        if (listener == null) {
            return;
        }
        if (getListenerCnt() == 0) {
            throw new HwCacheException("Remove failed. Has no one listener");
        }
        final var refListener = listeners.stream().filter(l -> Objects.equals(l.get(), listener)).findFirst();
        if (refListener.isEmpty()) {
            throw new HwCacheException("Remove listener not found.");
        }
        listeners.remove(refListener.get());
    }

    private void notifyAllListeners(K key, V value, String action) {
        listeners.forEach(l -> {
            if (l.get() != null) {
                try {
                    Objects.requireNonNull(l.get()).notify(key, value, action);
                } catch (Exception e) {
                    logger.error("Notify listener failed");
                }
            }
        });
    }

    public int getSize() {
        return cache.size();
    }

    public int getListenerCnt() {
        return listeners.size();
    }
}
