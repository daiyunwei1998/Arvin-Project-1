package com.stylish.stylish.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class RateLimitingService {
    private final RedisTemplate redisTemplate;

    public RateLimitingService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Checks if a request is allowed based on the rate limit and sliding window.
     *
     * @param key          The unique identifier for the rate-limited entity (e.g., user ID or API key).
     * @param limit        The maximum number of allowed requests within the time window.
     * @param windowMillis The duration of the time window in milliseconds.
     * @return true if the request is allowed, false if the rate limit is exceeded.
     */
    public boolean isAllowed(String key, int limit, long windowMillis) {
        long windowEnd = System.currentTimeMillis();
        long windowStart = windowEnd - windowMillis;
        return redisTemplate.opsForZSet().count(key, windowStart, windowEnd) < limit;
    }

    /**
     * Adds a timestamp to the sorted set for a given key.
     *
     * @param key       The unique identifier for the rate-limited entity.
     * @param timestamp The current timestamp of the request.
     */
    public void addRequestTimestamp(String key, long timestamp, long windowMillis) {
        System.out.println("Adding to Redis - Key: " + key + ", Member: " + timestamp + ", Score: " + timestamp);
        redisTemplate.opsForZSet().add(key, String.valueOf(timestamp), timestamp);
        redisTemplate.expire(key, windowMillis, TimeUnit.MILLISECONDS);
    }

    @Deprecated
    /**
     * Removes timestamps from the sorted set that are older than the start of the time window.
     *
     * @param key         The unique identifier for the rate-limited entity.
     * @param windowStart The timestamp marking the start of the time window.
     */
    public void removeOldTimestamps(String key, long windowStart) {
        // Method implementation
        redisTemplate.opsForZSet().removeRangeByScore(key, Double.NEGATIVE_INFINITY, windowStart - 1);
    }

}
