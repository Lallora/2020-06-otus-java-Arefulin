package hw17.cachehw;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MyCacheTest {

    @Test
    public void putWithNullKeyTest() {
        MyCache<Integer, Integer> cache = new MyCache<>();
        assertDoesNotThrow(() -> cache.put(null, null));
        assertEquals(0, cache.getSize());
    }

    @Test
    public void putExistTest() {
        MyCache<Integer, Integer> cache = new MyCache<>();
        assertDoesNotThrow(() -> cache.put(1, null));
        assertDoesNotThrow(() -> cache.put(1, null));
        assertEquals(1, cache.getSize());
    }

    @Test
    public void putNewKeyTest() {
        MyCache<Integer, Integer> cache = new MyCache<>();
        assertDoesNotThrow(() -> cache.put(1, null));
        assertEquals(1, cache.getSize());
    }

    @Test
    public void removeNullKeyTest() {
        MyCache<Integer, Integer> cache = new MyCache<>();
        assertDoesNotThrow(() -> cache.remove(null));
    }

    @Test
    public void removeExistKeyTest() {
        MyCache<Integer, Integer> cache = new MyCache<>();
        cache.put(1, 1);
        assertDoesNotThrow(() -> cache.remove(1));
    }

    @Test
    public void removeNotExistKeyTest() {
        MyCache<Integer, Integer> cache = new MyCache<>();
        assertDoesNotThrow(() -> cache.remove(1));
    }

    @Test
    public void getNullKeyTest() {
        MyCache<Integer, Integer> cache = new MyCache<>();
        assertNull(cache.get(null));
    }

    @Test
    public void getExistValueTest() {
        MyCache<Integer, Integer> cache = new MyCache<>();
        cache.put(1, 1);
        assertEquals(1, cache.get(1));
    }

    @Test
    public void getNoExistValueTest() {
        MyCache<Integer, Integer> cache = new MyCache<>();
        assertThrows(HwCacheException.class, () -> cache.get(1));
    }

    @Test
    public void addNullListenerTest() {
        assertThrows(HwCacheException.class, () -> new MyCache<>().addListener(null));
    }

    @Test
    public void addListenerTest() {
        final var listener = new HwListener<Integer, Integer>() {
            @Override
            public void notify(Integer key, Integer value, String action) {
            }
        };
        MyCache<Integer, Integer> cache = new MyCache<>();
        assertDoesNotThrow(() -> cache.addListener(listener));
        assertEquals(1, cache.getListenerCnt());
    }

    @Test
    public void removeNullListenerTest() {
        assertDoesNotThrow(() -> new MyCache<>().removeListener(null));
    }

    @Test
    public void removeNotExistListener() {
        final var listener = new HwListener<Integer, Integer>() {
            @Override
            public void notify(Integer key, Integer value, String action) {
            }
        };
        MyCache<Integer, Integer> cache = new MyCache<>();
        assertThrows(HwCacheException.class, () -> cache.removeListener(listener));
    }

    @Test
    public void removeListener() {
        final var listener = new HwListener<Integer, Integer>() {
            @Override
            public void notify(Integer key, Integer value, String action) {
            }
        };
        MyCache<Integer, Integer> cache = new MyCache<>();
        assertEquals(0, cache.getListenerCnt());

        assertDoesNotThrow(() -> cache.addListener(listener));
        assertEquals(1, cache.getListenerCnt());

        assertDoesNotThrow(() -> cache.removeListener(listener));
        assertEquals(0, cache.getListenerCnt());
    }

    @Test
    public void notifyListenerWhenSendPutTest() {
        HwListener<Integer, Integer> listener = Mockito.mock(HwListener.class);

        MyCache<Integer, Integer> cache = new MyCache<>();
        cache.addListener(listener);

        cache.put(1, 1);

        Mockito.verify(listener, Mockito.times(1)).notify(1, 1, "put");
        Mockito.verifyNoMoreInteractions(listener);
    }

    @Test
    public void notifyListenerWhenSendGetTest() {
        HwListener<Integer, Integer> listener = Mockito.mock(HwListener.class);

        MyCache<Integer, Integer> cache = new MyCache<>();
        cache.put(1, 1);

        cache.addListener(listener);
        cache.get(1);

        Mockito.verify(listener, Mockito.times(1)).notify(1, 1, "get");
        Mockito.verifyNoMoreInteractions(listener);
    }

    @Test
    public void notifyListenerWhenSendRemoveTest() {
        HwListener<Integer, Integer> listener = Mockito.mock(HwListener.class);

        MyCache<Integer, Integer> cache = new MyCache<>();
        cache.put(1, 1);

        cache.addListener(listener);
        cache.remove(1);

        Mockito.verify(listener, Mockito.times(1)).notify(1, 1, "remove");
        Mockito.verifyNoMoreInteractions(listener);
    }

    @Test
    public void sendNotifyManyListenerTest() {
        HwListener<Integer, Integer> listener1 = Mockito.mock(HwListener.class);
        HwListener<Integer, Integer> listener2 = Mockito.mock(HwListener.class);

        MyCache<Integer, Integer> cache = new MyCache<>();
        cache.addListener(listener1);
        cache.addListener(listener2);

        cache.put(1, 1);

        Mockito.verify(listener1, Mockito.times(1)).notify(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verifyNoMoreInteractions(listener1);

        Mockito.verify(listener2, Mockito.times(1)).notify(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verifyNoMoreInteractions(listener2);
    }
}
