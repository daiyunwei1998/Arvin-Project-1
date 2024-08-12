package com.stylish.stylish.utlis;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.stylish.stylish.dto.ProductDTO;
import com.stylish.stylish.model.Color;
import com.stylish.stylish.model.Variant;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProductDTOSerializer extends StdSerializer<ProductDTO> {
    private final ObjectMapper mapper = new ObjectMapper();
    public ProductDTOSerializer() {
        this(null);
    }

    public ProductDTOSerializer(Class<ProductDTO> t) {
        super(t);
    }

    public JsonNode serializeToJsonNode(ProductDTO dto) throws JsonProcessingException {
        ObjectNode root = mapper.createObjectNode();

        root.put("id", dto.getId());
        root.put("category", dto.getCategory());
        root.put("title", dto.getTitle());
        root.put("description", dto.getDescription());
        root.put("price", dto.getPrice());
        root.put("texture", dto.getTexture());
        root.put("wash", dto.getWash());
        root.put("place", dto.getPlace());
        root.put("note", dto.getNote());
        root.put("story", dto.getStory());

        ArrayNode colors = root.putArray("colors");
        for (Color color : dto.getColorList()) {
            ObjectNode colorNode = colors.addObject();
            colorNode.put("code", color.getCode());
            colorNode.put("name", color.getName());
        }

        ArrayNode sizes = root.putArray("sizes");
        for (String size : dto.getSizes()) {
            sizes.add(size);
        }

        ArrayNode variants = root.putArray("variants");
        for (Variant variant : dto.getVariantList()) {
            ObjectNode variantNode = variants.addObject();
            variantNode.put("color_code", variant.getColorCode());
            variantNode.put("size", variant.getSize());
            variantNode.put("stock", variant.getStock());
        }

        root.put("main_image", dto.getMainImage());

        ArrayNode images = root.putArray("images");
        for (String imageURL : dto.getImagesURL()) {
            images.add(imageURL);
        }

        return root;
    }

    @Override
    public void serialize(ProductDTO dto, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        JsonNode jsonNode = serializeToJsonNode(dto);
        jgen.writeObject(jsonNode);
    }



}
