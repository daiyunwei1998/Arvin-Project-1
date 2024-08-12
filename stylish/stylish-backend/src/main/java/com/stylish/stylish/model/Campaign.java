package com.stylish.stylish.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Campaign {
    @JsonProperty("product_id")
    private long productId;
    private String picture;
    private String story;
    private String content;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("close_date")
    private String closeDate;
}
