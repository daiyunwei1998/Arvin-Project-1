package com.stylish.stylish.utlis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stylish.stylish.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductListSerializer {
    private final ObjectMapper mapper;
    private final ProductDTOSerializer productSerializer;

    @Autowired
    public ProductListSerializer(ObjectMapper mapper, ProductDTOSerializer productSerializer) {
        this.mapper = mapper;
        this.productSerializer = productSerializer;
    }

    public JsonNode serializeList(List<ProductDTO> products, Integer nextPaging) throws JsonProcessingException {
        ObjectNode root = mapper.createObjectNode();
        ArrayNode productsArray = root.putArray("data");

        for (ProductDTO product : products) {
            JsonNode productNode = productSerializer.serializeToJsonNode(product);
            productsArray.add(productNode);
        }

        if (nextPaging != null) {
            root.put("next_paging", nextPaging);
        }

        return root;
    }
}