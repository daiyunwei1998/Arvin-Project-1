package com.stylish.stylish.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylish.stylish.model.Campaign;
import com.stylish.stylish.repository.MarketingDAO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class MarketingService {
    private final MarketingDAO marketingDAO;
    private RedisTemplate<String, Object> redisTemplate; // Use Object if you're using a generic template
    private final ObjectMapper objectMapper;

    public MarketingService(MarketingDAO marketingDAO, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.marketingDAO = marketingDAO;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void createCampaigns(List<Campaign> campaigns) {

        // clear cache first
        redisTemplate.delete("campaigns");
        log.info("New data in! Clearing redit cache");

        for (Campaign campaign: campaigns) {
            marketingDAO.addCampaign(campaign);
        }
    }

    public List<Campaign> getCampaigns() {
        // Cache: attempt to get from redis
        String campaignsJson = (String) redisTemplate.opsForValue().get("campaigns");

        if (campaignsJson != null) {
            try {
                // Deserialize JSON string to List<Campaign>
                log.info("Data found in Redis!");
                return objectMapper.readValue(campaignsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Campaign.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // Handle deserialization error
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        // MISS: get from database
        log.info("No data in redis, you gotta do what you gotta do");
        List<Campaign> campaigns = marketingDAO.getCampaigns();

        // put into redis
        try {
            // Serialize List<Campaign> to JSON string and save in Redis
            String campaignsJsonString = objectMapper.writeValueAsString(campaigns);
            redisTemplate.opsForValue().set("campaigns", campaignsJsonString);
            log.info("put data in redis. One for all, all for one");
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle serialization error
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return campaigns;

    }
}
