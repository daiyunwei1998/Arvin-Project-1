package com.stylish.stylish.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stylish.stylish.dto.ProductDTO;
import com.stylish.stylish.model.Color;
import com.stylish.stylish.model.Product;
import com.stylish.stylish.model.Variant;
import com.stylish.stylish.repository.ProductDAO;
import com.stylish.stylish.service.ProductService;
import com.stylish.stylish.service.S3Service;
import com.stylish.stylish.utlis.ProductListSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/${apiVersion}/products")  //todo make this version number a config variable someday
public class ProductController {
    private final S3Service s3Service;
    private final ProductDAO productDAO;
    private final ObjectMapper objectMapper;
    private final ProductListSerializer productListSerializer;
    private final ProductService productService;
    @Autowired
    public ProductController(ProductDAO productDAO, S3Service s3Service, ObjectMapper objectMapper, ProductListSerializer productListSerializer,ProductService productService) {
        this.s3Service = s3Service;
        this.productDAO = productDAO;
        this.objectMapper = objectMapper;
        this.productListSerializer = productListSerializer;
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@ModelAttribute ProductDTO productDTO,
            @RequestParam("main_image") MultipartFile mainImage,
            @RequestParam("images") List<MultipartFile> images) throws IOException {


       List<Color> colorList = objectMapper.readValue(productDTO.getColors(),  new TypeReference<List<Color>>(){});


        // Print all fields of ProductDTO explicitly
        System.out.println("color:" + productDTO.getColors());
        System.out.println("Title: " + productDTO.getTitle());
        System.out.println("Price: " + productDTO.getPrice());
        System.out.println("Category: " + productDTO.getCategory());
        System.out.println("Description: " + productDTO.getDescription());
        System.out.println("Texture: " + productDTO.getTexture());
        System.out.println("Wash: " + productDTO.getWash());
        System.out.println("Place: " + productDTO.getPlace());
        System.out.println("Note: " + productDTO.getNote());
        System.out.println("Story: " + productDTO.getStory());
        System.out.println("sizes:" + productDTO.getSizes());
        System.out.println("variants:" + productDTO.getVariants());

        // TODO: refactor
        // This logic should not be implemented in controller
        // Print main image details
        if (mainImage != null) {
            String uuid = s3Service.uploadFile(mainImage);
            productDTO.setMainImage(uuid);
        }

        // store images
        List<String> uuids = images.stream().map(image -> {
            try {
                return s3Service.uploadFile(image);

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file", e);
            }
        }).collect(Collectors.toList());
        productDTO.setImagesURL(uuids);
        // TODO refactor this terrible conversion
        Product p = productService.convertToEntity(productDTO);
        for (Color color : colorList) {
            String code = color.getCode();
            if (code.startsWith("#")) {
                color.setCode(code.substring(1));
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        // read the json strings and convert it into JsonNode
        JsonNode arrayNode = mapper.readTree(productDTO.getVariants());

        // TODO: refactor this trash
        List<Variant> variants = new ArrayList<>();
        if (arrayNode.isArray()) {
            // yes, loop the JsonNode and display one by one
            for (JsonNode node : arrayNode) {
                Variant v = new Variant(node.get("variant_color").asText().substring(1), node.get("variant_size").asText(), Integer.parseInt(node.get("variant_stock").asText()));
                variants.add(v);
            }
        }

            p.setVariants(variants);

        p.setColors(colorList);
        productDAO.addProduct(p);

        Map<String, String> response = new HashMap<>();
        response.put("message", "all good");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{category}")
    public ResponseEntity<JsonNode> getProducts(@RequestParam(value = "paging", required = false, defaultValue = "0") Integer paging,
                                                @PathVariable String category) throws JsonProcessingException {
        return productService.getProductByCategory(category, paging);
    }

    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchProductByKeyword(@RequestParam(value = "paging", required = false, defaultValue = "0") Integer paging,
                                                  @RequestParam(value = "keyword", required = true) String keyword) throws JsonProcessingException {
        return productService.getProductByKeyword(keyword, paging);
    }

    @GetMapping("/details")
    public ResponseEntity<?> searchProductById(@RequestParam(value = "id", required = true) long id) throws JsonProcessingException {
        try {
            if (id <= 0) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("error", "Invalid id");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            return productService.getProductById(id);
        } catch (Exception e){
            // Log the exception if necessary
            log.error("Error occurred while retrieving product with ID {}: {}", id, e.getMessage());
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable long id, @RequestParam("image") MultipartFile image) throws IOException {
        if (image != null) {
            String url = s3Service.uploadFile(image);
            productService.addProductImage(url, id);
            return ResponseEntity.ok(Map.of("url", url));
        } else {
            throw new IllegalArgumentException("Image cannot be null");
        }
    }
}
