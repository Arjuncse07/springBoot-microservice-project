package com.arjun.catalog_service.bookstore.web.controllers;

import com.arjun.catalog_service.bookstore.catalog.domain.PagedResult;
import com.arjun.catalog_service.bookstore.catalog.domain.Product;
import com.arjun.catalog_service.bookstore.catalog.domain.ProductNotFoundException;
import com.arjun.catalog_service.bookstore.catalog.domain.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        return productService.getProducts(pageNo);
    }

    @GetMapping("/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        sleep(); // method to check resiliance time out of microservice
        return productService
                .getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }

    void sleep(){
        try {
            Thread.sleep(6000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
