package com.stylish.stylish.repository;

import com.stylish.stylish.model.Campaign;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface MarketingDAO {
    public void addCampaign(Campaign campaign);
    public List<Campaign> getCampaigns();
}
