package com.stylish.stylish.repository;

import com.stylish.stylish.dto.ProductDTO;
import com.stylish.stylish.exception.NoVariantException;
import com.stylish.stylish.exception.NotEnoughStockException;
import com.stylish.stylish.model.Color;
import com.stylish.stylish.model.Product;
import com.stylish.stylish.model.Variant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;

import java.util.List;


@Log4j2
@Repository
public class ProductDAOImpl implements ProductDAO {
    @Autowired
    private Logger logger;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbctemplate;
    @Override
    public void addProduct(Product product) {

        String sql = "INSERT INTO products (fk_category_id, title, description, price, texture, wash, fk_place_id, note, story, fk_main_image_id) " +
                "VALUES (:categoryId, :title, :description, :price, :texture, :wash, :placeId,:note, :story, :mainImageId)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("categoryId", Category.getIdByLabel(product.getCategory()))
                .addValue("title", product.getTitle())
                .addValue("description", product.getDescription())
                .addValue("price", product.getPrice())
                .addValue("texture", product.getTexture())
                .addValue("wash", product.getWash())
                .addValue("placeId", setProductPlace(product.getPlace()))
                .addValue("note", product.getNote())
                .addValue("story", product.getStory())
                .addValue("mainImageId", null);
        namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
        product.setId(keyHolder.getKey().longValue());
        setProductColors(product.getColors(), product.getId());
        setProductSizes(product.getSizes(), product.getId());
        for (String imageURL : product.getImages()) {
            addProductImage(imageURL, product.getId());
        }
        for (Variant variant : product.getVariants()) {
            addProductVariant(variant, product.getId());
        }
        setProductMainImage(product.getMainImage(), product.getId());

    }

    public List<ProductDTO> getProducts(String sql, SqlParameterSource parameters) {
        List<ProductDTO> dtos = namedParameterJdbcTemplate.query(sql, parameters, productDTORowMapper());

        for (ProductDTO dto: dtos) {
            /* update fields from linked tables */
            // update category using id
            dto.setCategory(Category.getLabelbyId(dto.getCategoryId()));
            // get place by placeID
            dto.setPlace(getPlaceNameById(dto.getPlaceId()));
            // get colors
            dto.setColorList(getColorsByProductId(dto.getId()));
            // get sizes
            dto.setSizes(getSizesByProductId((int) dto.getId()));
            // get main image
            dto.setMainImage(getImageURLById(dto.getMainImageID()));
            // get images
            dto.setImagesURL(getImagesURLsByProductId(dto.getId()));
            // get variants
            dto.setVariantList(getVariantsByProductId(dto.getId()));
        }
        return dtos;
    }

    public ProductDTO getProduct(String sql, SqlParameterSource parameters) {
        ProductDTO dto = namedParameterJdbcTemplate.queryForObject(sql, parameters, productDTORowMapper());
        /* update fields from linked tables */
        // update category using id
        dto.setCategory(Category.getLabelbyId(dto.getCategoryId()));
        // get place by placeID
        dto.setPlace(getPlaceNameById(dto.getPlaceId()));
        // get colors
        dto.setColorList(getColorsByProductId(dto.getId()));
        // get sizes
        dto.setSizes(getSizesByProductId((int) dto.getId()));
        // get main image
        dto.setMainImage(getImageURLById(dto.getMainImageID()));
        // get images
        dto.setImagesURL(getImagesURLsByProductId(dto.getId()));
        // get variants
        dto.setVariantList(getVariantsByProductId(dto.getId()));
        return dto;
    }

    @Override
    public List<ProductDTO> getProductsByOffsetAndLimit(int offset, int limit) {
        String sql = "SELECT id, title, description, price, texture, wash, fk_category_id, fk_place_id, note, story, fk_main_image_id " +
                "FROM products " +
                "LIMIT :limit OFFSET :offset";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit);
        return getProducts(sql, parameters);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category, int offset, int limit) {
        String sql = "SELECT id, title, description, price, texture, wash, fk_category_id, fk_place_id, note, story, fk_main_image_id " +
                "FROM products ";
        if (!"all".equalsIgnoreCase(category)) {
            sql += "WHERE fk_category_id = :categoryId ";
        }
        sql += "LIMIT :limit OFFSET :offset";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if (!"all".equalsIgnoreCase(category)) {
            parameters.addValue("categoryId", Category.getIdByLabel(category));
        }
        parameters.addValue("limit", limit);
        parameters.addValue("offset", offset);
        return getProducts(sql, parameters);

    }

    @Override
    public List<ProductDTO> getProductsByKeyword(String keyword, int offset, int limit) {
        String sql = "SELECT id, title, description, price, texture, wash, fk_category_id, fk_place_id, note, story, fk_main_image_id " +
                "FROM products " +
                "WHERE title LIKE :pattern "+
                "LIMIT :limit OFFSET :offset";

        String pattern = "%" + keyword + "%";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", offset)
                .addValue("pattern",pattern);

        return getProducts(sql, parameters);
    }

    @Override
    public ProductDTO getProductById(long id) {
        String sql = "SELECT id, title, description, price, texture, wash, fk_category_id, fk_place_id, note, story, fk_main_image_id " +
                "FROM products " +
                "WHERE id = :id";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return getProduct(sql, parameters);
    }

    // RowMapper for Product
    private RowMapper<ProductDTO> productDTORowMapper() {
        return (rs, rowNum) -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(rs.getLong("id"));
            dto.setTitle(rs.getString("title"));
            dto.setDescription(rs.getString("description"));
            dto.setPrice(rs.getBigDecimal("price"));
            dto.setTexture(rs.getString("texture"));
            dto.setWash(rs.getString("wash"));
            dto.setCategoryId(rs.getInt("fk_category_id"));
            dto.setPlaceId(rs.getInt("fk_place_id"));
            dto.setNote(rs.getString("note"));
            dto.setStory(rs.getString("story"));
            dto.setMainImageID(rs.getInt("fk_main_image_id"));

            return dto;
        };
    }

    @Override
    public void setProductMainImage(String mainImageURL, long productId) {
        String sql = "UPDATE products SET fk_main_image_id = :imageId WHERE id = :productId";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("imageId", addProductImage(mainImageURL, productId))
                .addValue("productId", productId);
        namedParameterJdbcTemplate.update(sql, parameters);

    }
    @Override
    public void setProductColors(List<Color> colors, long productId) {
        String sql = "INSERT INTO product_colors (fk_product_id, fk_color_code) VALUES (:productId, :colorCode)";

        for (Color color: colors) {
            if (getColorByCode(color.getCode())==null) {
                // add color first if not registered in colors table
                addColor(color);
            }
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("productId", productId)
                    .addValue("colorCode", color.getCode());
            namedParameterJdbcTemplate.update(sql, parameters);
        }
    }

    @Override
    public List<Color> getColorsByProductId(long productId) {
        String sql = "SELECT * FROM colors WHERE code IN (SELECT fk_color_code FROM product_colors WHERE fk_product_id = :productId)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("productId", productId);
        List<Color> colors = namedParameterJdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(Color.class));
        return colors;

    }

    @Override
    public long addColor(Color color) {
        String sql = "INSERT INTO colors (name, code) VALUES (:name, :code)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", color.getName())
                .addValue("code", color.getCode());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
            Number key = keyHolder.getKey();
            return (key != null) ? key.longValue() : -1;
        } catch (DataAccessException e) {
            logger.error("Failed to insert color into database: {}", e.getMessage(), e);
            return -1;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while inserting color: {}", e.getMessage(), e);
            return -1;
        }
    }


    @Override
    public Color getColorByCode(String  code) {
        String sql = "SELECT * FROM colors WHERE code = :code";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("code", code);
        Color color = null;
        try {
            color = namedParameterJdbcTemplate.queryForObject(sql, parameters, new BeanPropertyRowMapper<>(Color.class));
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No color found with code: {}", code);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving color: {}", e.getMessage(), e);
        }
        return color;
    }

    @Override
    public void addProductVariant(Variant variant, long productId) {
        String sql = "INSERT INTO variants (fk_product_id, fk_color_code, fk_size_id, stock) VALUES (:productId, :colorCode, :sizeId, :stock)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("productId", productId)
                .addValue("colorCode", variant.getColorCode())
                .addValue("sizeId", Size.getByLabel(variant.getSize()).getId())
                .addValue("stock", variant.getStock());
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public int setProductCategory(String category) {
        String sql = "INSERT INTO categories (id, name) VALUES (:id, :name)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", Category.getIdByLabel(category))
                .addValue("name", category);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
            Number key = keyHolder.getKey();
            return (key != null) ? key.intValue() : -1;
        } catch (DataAccessException e) {
            logger.error("Failed to insert color into database: {}", e.getMessage(), e);
            return -1;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while inserting color: {}", e.getMessage(), e);
            return -1;
        }
    }

    // Method to retrieve the current stock value
    public Integer getStock(long productId, String colorCode, String size) {
        String sql = "SELECT stock FROM variants WHERE fk_product_id = :fk_product_id AND fk_color_code = :fk_color_code AND fk_size_id = :fk_size_id";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("fk_product_id", productId);
        parameters.addValue("fk_color_code", colorCode);
        parameters.addValue("fk_size_id", Size.getIdByLabel(size));
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);
        } catch (IncorrectResultSizeDataAccessException e) {
            return 0;  // Assuming zero stock if no results found
        }
    }

    @Override
    public void reduceStockByColorAndSize(long productId, String colorCode, String size, int qty) {
        if (getStock(productId, colorCode, size) < qty) {
            throw new NotEnoughStockException("not enough stock for this variant");
        }

        String sql = "UPDATE variants SET stock = stock - :qty WHERE fk_color_code = :colorCode AND fk_size_id = :sizeId";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("qty", qty);
        parameters.addValue("colorCode", colorCode);
        parameters.addValue("sizeId", Size.getIdByLabel(size));

        namedParameterJdbcTemplate.update(sql, parameters);

    }

    @Override
    public long getVariantId(long productId, String colorCode, String size) {
        String sql = "SELECT id FROM variants  WHERE fk_color_code = :colorCode AND fk_size_id = :sizeId AND fk_product_id = :productId";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("colorCode", colorCode);
        parameters.addValue("sizeId", Size.getIdByLabel(size));
        parameters.addValue("productId", productId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, parameters, Long.class);
        } catch (EmptyResultDataAccessException e) {
            throw new NoVariantException("Variant not found");
        }

    }

    @Override
    public void setProductSizes(List<String> sizes, long productId) {
        String sql = "INSERT INTO product_sizes (fk_product_id, fk_size_id) VALUES (:productId, :size)";
        for (String size: sizes) {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("productId", productId)
                    .addValue("size", Size.getIdByLabel(size));
            namedParameterJdbcTemplate.update(sql, parameters);
        }
    }

    @Override
    public int setProductPlace(String place) {
        // Check if the place already exists
        String checkSql = "SELECT id FROM places WHERE name = :name";
        SqlParameterSource checkParameters = new MapSqlParameterSource()
                .addValue("name", place);

        try {
            Integer existingId = namedParameterJdbcTemplate.queryForObject(checkSql, checkParameters, Integer.class);
            if (existingId != null) {
                return existingId; // Place already exists, return its ID
            }
        } catch (EmptyResultDataAccessException e) {
            // Place does not exist, proceed to insert
        }

        String sql = "INSERT INTO places (id, name) VALUES (NULL, :name)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", place);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
            Number key = keyHolder.getKey();
            return (key != null) ? key.intValue() : -1;
        } catch (DataAccessException e) {
            logger.error("Failed to insert color into database: {}", e.getMessage(), e);
            return -1;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while inserting color: {}", e.getMessage(), e);
            return -1;
    }
    }

    @Override
    public String getPlaceNameById(int placeId) {
        String sql = "SELECT name FROM places WHERE id = :placeId";
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("placeId", placeId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, parameter, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "";
        }
    }


    @Override
    public List<String> getSizesByProductId(int productId) {
        String sql = "SELECT fk_size_id FROM product_sizes WHERE fk_product_id = :productId";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("productId", productId);
        List<Integer> sizeIds= namedParameterJdbcTemplate.queryForList(sql, parameters, Integer.class);
        return Size.getSizeList(sizeIds);
    }

    @Override
    public long addProductImage(String image, long productId) {
        // Check if the image already exists
        String checkSql = "SELECT id FROM product_images WHERE image_url = :image";
        SqlParameterSource checkParameters = new MapSqlParameterSource()
                .addValue("image", image);

        try {
            Integer existingId = namedParameterJdbcTemplate.queryForObject(checkSql, checkParameters, Integer.class);
            if (existingId != null) {
                return existingId; // Image already exists, return its ID
            }
        } catch (EmptyResultDataAccessException e) {
            // Image does not exist, proceed to insert
        }

        String sql = "INSERT INTO product_images (image_url, fk_product_id) VALUES (:image, :productId)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("image", image)
                .addValue("productId", productId);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
            Number key = keyHolder.getKey();
            return (key != null) ? key.longValue() : -1;
        } catch (DataAccessException e) {
            logger.error("Failed to insert color into database: {}", e.getMessage(), e);
            return -1;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while inserting color: {}", e.getMessage(), e);
            return -1;
        }
    }

    @Override
    public String getImageURLById(int imageId) {
        String sql = "SELECT image_url FROM product_images WHERE id = :imageId";
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("imageId", imageId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, parameter, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "";
    }
    }

    @Override
    public List<String> getImagesURLsByProductId(long productId) {
        String sql = "SELECT image_url FROM product_images WHERE fk_product_id = :productId";
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("productId", productId);
        return namedParameterJdbcTemplate.queryForList(sql, parameter, String.class);
    }

    @Override
    public List<Variant> getVariantsByProductId(long productId) {
        String sql = "SELECT id, fk_product_id, fk_color_code, fk_size_id, stock " +
                "FROM variants " +
                "WHERE fk_product_id = :productId";

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("productId", productId);

        List<Variant> variantsList = namedParameterJdbcTemplate.query(sql, parameters, variantRowMapper());
        for (Variant variant: variantsList) {
            variant.setSize(Size.getLabelById(Integer.parseInt(variant.getSize())));
        }
        return variantsList;

    }

    private RowMapper<Variant> variantRowMapper() {
        return (rs, rowNum) -> {
            Variant variant = new Variant();
            variant.setColorCode(rs.getString("fk_color_code"));
            variant.setSize(rs.getString("fk_size_id"));
            variant.setStock(rs.getInt("stock"));

            return variant;
        };
    }

    @Override
    public Product findProductById(Long id) {
        //todo
        return null;
    }

    @Override
    public List<Product> findAll() {
        //todo
        return List.of();
    }

    @Override
    public void update(Product product) {
        //todo

    }

    @Override
    public void delete(Long id) {
        //todo

    }
}
