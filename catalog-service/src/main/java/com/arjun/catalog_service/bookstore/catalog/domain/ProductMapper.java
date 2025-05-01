package com.arjun.catalog_service.bookstore.catalog.domain;

public class ProductMapper {

    /*  Mapper class to not expose the Product Entity  */

    static Product toProduct(ProductEntity productEntity){

        return new Product(
                productEntity.getCode(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getImageUrl(),
                productEntity.getPrice()
        );
    }


}
