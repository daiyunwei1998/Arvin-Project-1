package com.stylish.stylish.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stylish.stylish.dto.ProductDTO;
import com.stylish.stylish.model.Product;
import com.stylish.stylish.repository.ProductDAO;
import com.stylish.stylish.utlis.ProductListSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.stylish.stylish.utlis.ProductDTOSerializer;
import java.net.Inet4Address;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private static final int PAGE_SIZE = 6;
    private   ProductDAO productDAO;
    private   ProductListSerializer productListSerializer;
    private ObjectMapper objectMapper;
    private ProductDTOSerializer productDTOSerializer;
    @Autowired
    ProductService(ProductDAO productDAO, ProductListSerializer productListSerializer, ObjectMapper objectMapper, ProductDTOSerializer productDTOSerializer) {
        this.productDAO = productDAO;
        this.productListSerializer = productListSerializer;
        this.objectMapper = objectMapper;
        this.productDTOSerializer = productDTOSerializer;
    }

    // convert ProductDTO to Product entity
    public  Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setColors(null);
        product.setDescription(dto.getDescription());
        product.setTexture(dto.getTexture());
        product.setWash(dto.getWash());
        product.setPlace(dto.getPlace());
        product.setSizes(dto.getSizes());
        product.setNote(dto.getNote());
        product.setStory(dto.getStory());
        product.setMainImage(dto.getMainImage());
        product.setImages(dto.getImagesURL());
        product.setVariants(null);
        return product;
    }

    public ResponseEntity<JsonNode> getProductByCategory(String Category, Integer paging) throws JsonProcessingException {
        // edge case
        if (paging < 0) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("error", "Paging parameter out of range");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // helper function
        List<ProductDTO> products = productDAO.getProductsByCategory(Category,
                paging * PAGE_SIZE,
                PAGE_SIZE + 1);  // get one more product than PAGE_SIZE to check end of table
        JsonNode root;
        if (products.isEmpty()) {
            // Return an empty array if there are no products
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("error", "Paging parameter out of range or empty database");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (products.size() <= PAGE_SIZE ) {
            root = productListSerializer.serializeList(products, null);
        } else {
            root = productListSerializer.serializeList(products.subList(0,6), paging += 1);
        }
        return ResponseEntity.ok(root);
    }

    public ResponseEntity<JsonNode> getProductByKeyword(String keyword, Integer paging) throws JsonProcessingException {
        // edge case
        if (paging < 0) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("error", "Paging parameter out of range");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        List<ProductDTO> products = productDAO.getProductsByKeyword(keyword,
                paging * PAGE_SIZE,
                PAGE_SIZE + 1);

        JsonNode root;

        if (products.isEmpty()) {
            // Return an empty array if there are no products
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("error", "Paging parameter out of range or empty database");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (products.size() <= PAGE_SIZE ) {
            root = productListSerializer.serializeList(products, null);
        } else {
            root = productListSerializer.serializeList(products.subList(0,6), paging += 1);
        }
        return ResponseEntity.ok(root);
    }

    public ResponseEntity<?> getProductById(long id) throws JsonProcessingException {
        JsonNode root;
        try {
        ProductDTO dto = productDAO.getProductById(id);
        root = productDTOSerializer.serializeToJsonNode(dto);
        return ResponseEntity.ok(Map.of("data", root));
        } catch (EmptyResultDataAccessException e) {
            // Handle the case where no result is found
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("error", "No product with this id");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    public void addProductImage(String url, long id) {
        productDAO.addProductImage(url, id);
    }

}
