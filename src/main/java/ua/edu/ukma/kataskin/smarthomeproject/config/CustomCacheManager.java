package ua.edu.ukma.kataskin.smarthomeproject.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.lang.Nullable;

import java.util.Collection;

public class CustomCacheManager extends ConcurrentMapCacheManager {

    private static final Logger log = LoggerFactory.getLogger(CustomCacheManager.class);

    public CustomCacheManager(String... cacheNames) {
        super(cacheNames);
        log.info("CustomCacheManager initialized with caches: {}", (Object) cacheNames);
    }

    @Override
    protected Cache createConcurrentMapCache(String name) {
        return new LoggingCache(name, super.createConcurrentMapCache(name));
    }

    private static class LoggingCache implements Cache {
        private static final Logger log = LoggerFactory.getLogger(LoggingCache.class);
        private final String name;
        private final Cache delegate;

        public LoggingCache(String name, Cache delegate) {
            this.name = name;
            this.delegate = delegate;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getNativeCache() {
            return delegate.getNativeCache();
        }

        @Override
        @Nullable
        public ValueWrapper get(Object key) {
            ValueWrapper result = delegate.get(key);
            if (result != null) {
                log.debug("Cache HIT: cache='{}' key='{}'", name, key);
            } else {
                log.debug("Cache MISS: cache='{}' key='{}'", name, key);
            }
            return result;
        }

        @Override
        @Nullable
        public <T> T get(Object key, @Nullable Class<T> type) {
            T result = delegate.get(key, type);
            if (result != null) {
                log.debug("Cache HIT: cache='{}' key='{}' type='{}'", name, key, type);
            } else {
                log.debug("Cache MISS: cache='{}' key='{}' type='{}'", name, key, type);
            }
            return result;
        }

        @Override
        @Nullable
        public <T> T get(Object key, java.util.concurrent.Callable<T> valueLoader) {
            return delegate.get(key, valueLoader);
        }

        @Override
        public void put(Object key, @Nullable Object value) {
            log.debug("Cache PUT: cache='{}' key='{}'", name, key);
            delegate.put(key, value);
        }

        @Override
        public void evict(Object key) {
            log.info("Cache EVICT: cache='{}' key='{}'", name, key);
            delegate.evict(key);
        }

        @Override
        public void clear() {
            log.info("Cache CLEAR: cache='{}'", name);
            delegate.clear();
        }
    }
}
