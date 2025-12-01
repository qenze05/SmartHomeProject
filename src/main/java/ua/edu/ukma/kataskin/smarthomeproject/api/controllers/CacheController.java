package ua.edu.ukma.kataskin.smarthomeproject.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private static final Logger log = LoggerFactory.getLogger(CacheController.class);

    private final CacheManager cacheManager;

    public CacheController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @PostMapping("/evict/{cacheName}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, String>> evictCache(@PathVariable String cacheName) {
        log.info("Cache eviction requested for: {}", cacheName);

        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            log.warn("Cache '{}' not found", cacheName);
            return ResponseEntity.notFound().build();
        }

        cache.clear();
        log.info("Cache '{}' evicted successfully", cacheName);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cache '" + cacheName + "' evicted successfully");
        response.put("cache", cacheName);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/evict-all")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> evictAllCaches() {
        log.info("All caches eviction requested");

        Collection<String> cacheNames = cacheManager.getCacheNames();
        int evictedCount = 0;

        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                evictedCount++;
                log.info("Cache '{}' evicted", cacheName);
            }
        }

        log.info("All caches evicted successfully. Total: {}", evictedCount);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "All caches evicted successfully");
        response.put("evictedCount", evictedCount);
        response.put("caches", cacheNames);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> getCacheInfo() {
        Collection<String> cacheNames = cacheManager.getCacheNames();

        Map<String, Object> response = new HashMap<>();
        response.put("totalCaches", cacheNames.size());
        response.put("caches", cacheNames);

        return ResponseEntity.ok(response);
    }
}
