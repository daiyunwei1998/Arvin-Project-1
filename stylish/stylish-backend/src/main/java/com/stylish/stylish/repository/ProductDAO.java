package com.stylish.stylish.repository;

import com.stylish.stylish.dto.ProductDTO;
import com.stylish.stylish.model.Color;
import com.stylish.stylish.model.Product;
import com.stylish.stylish.model.Variant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO {
    void addProduct(Product product);
    public List<ProductDTO> getProductsByOffsetAndLimit(int offset, int limit);
    public List<ProductDTO> getProductsByCategory(String category, int offset, int limit);
    public List<ProductDTO> getProductsByKeyword(String keyword, int offset, int limit);
    public ProductDTO getProductById(long id);

    void setProductMainImage(String mainImageURL, long productId);

    void setProductColors(List<Color> colors, long productId);
    List<Color> getColorsByProductId(long productId);
    long addColor(Color color);
    Color getColorByCode(String code);

    void addProductVariant(Variant variant, long productId);
    int setProductCategory(String category);

    void reduceStockByColorAndSize(long productId, String colorCode, String size, int qty);

    long getVariantId(long productId, String colorCode, String size);

    void setProductSizes(List<String> sizes, long productId);
    int setProductPlace(String place);
    String getPlaceNameById(int placeId);
    List<String> getSizesByProductId(int productId);

    long addProductImage(String image, long productId);
    String getImageURLById(int imageId);
    List<String> getImagesURLsByProductId(long productId);

    List<Variant> getVariantsByProductId(long productId);
    Product findProductById(Long id);
    List<Product> findAll();
    void update(Product product);
    void delete(Long id);
}
