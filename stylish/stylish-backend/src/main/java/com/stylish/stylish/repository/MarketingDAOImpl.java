package com.stylish.stylish.repository;

import com.stylish.stylish.model.Campaign;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j2
@Repository
public class MarketingDAOImpl implements MarketingDAO{
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MarketingDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void addCampaign(Campaign campaign) {
        log.error(campaign);
        try {
            namedParameterJdbcTemplate.update(
                    "INSERT INTO campaigns (product_id, picture, story, content, release_date, close_date) VALUES (:product_id, :picture, :story, :content, :release_date, :close_date)",
                    new MapSqlParameterSource()
                            .addValue("product_id", campaign.getProductId())
                            .addValue("picture", campaign.getPicture())
                            .addValue("story", campaign.getStory())
                            .addValue("content", campaign.getContent())
                            .addValue("release_date", campaign.getReleaseDate())
                            .addValue("close_date", campaign.getCloseDate())
            );
        } catch (DuplicateKeyException e) {
            namedParameterJdbcTemplate.update(
                    "UPDATE campaigns SET picture = :picture, story = :story, content = :content, release_date = :release_date, close_date = :close_date WHERE product_id = :product_id",
                    new MapSqlParameterSource()
                            .addValue("product_id", campaign.getProductId())
                            .addValue("picture", campaign.getPicture())
                            .addValue("story", campaign.getStory())
                            .addValue("content", campaign.getContent())
                            .addValue("release_date", campaign.getReleaseDate())
                            .addValue("close_date", campaign.getCloseDate())
            );
        }
    }
    @Override
    public List<Campaign> getCampaigns() {
        String sql = "SELECT product_id, picture, story, content, release_date, close_date FROM campaigns";

        RowMapper<Campaign> rowMapper = (rs, rowNum) -> {
            Campaign campaign = new Campaign();
            campaign.setProductId(rs.getLong("product_id"));
            campaign.setPicture(rs.getString("picture"));
            campaign.setStory(rs.getString("story"));
            campaign.setContent(rs.getString("content"));
            campaign.setReleaseDate(rs.getString("release_date"));
            campaign.setCloseDate(rs.getString("close_date"));
            return campaign;
        };

        List<Campaign> campaigns = namedParameterJdbcTemplate.query(sql, rowMapper);

        return campaigns;
        }

}
