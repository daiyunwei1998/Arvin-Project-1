package com.stylish.stylish.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stylish.stylish.model.Color;
import com.stylish.stylish.model.Variant;
import com.stylish.stylish.utlis.ProductDTOSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@ToString
@JsonSerialize(using = ProductDTOSerializer.class)
public class ProductDTO {
    private long id;
    private String title;
    private BigDecimal price;
    private String category;
    private int categoryId;
    private int placeId;
    private String description;
    private String texture;
    private String wash;
    private String place;
    private String note;
    private String story;
    private List<String> sizes;
    private String mainImage;
    private List<MultipartFile> images;
    private List<String> imagesURL;
    private int mainImageID;
    private List<Integer> imagesIDs;
    private String colors; //for json parser
    private String variants; //for json parser
    private List<Color> colorList;
    private List<Variant> variantList;
}
