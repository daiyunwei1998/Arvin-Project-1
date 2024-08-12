package com.stylish.stylish.controller;

import com.stylish.stylish.model.Campaign;
import com.stylish.stylish.service.MarketingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/${apiVersion}/marketing")
public class MarketingController {
    private final MarketingService marketingService;

    public MarketingController(MarketingService marketingService) {
        this.marketingService = marketingService;
    }

    @PostMapping("/campaigns")
    public ResponseEntity<String> createCampaign(@RequestBody List<Campaign> campaigns) {
        marketingService.createCampaigns(campaigns);
        return ResponseEntity.ok("success");

    }


    @GetMapping("/campaigns")
    public ResponseEntity<?> getCampaign() {
        return ResponseEntity.ok(Map.of("data", marketingService.getCampaigns()));
    }

}
