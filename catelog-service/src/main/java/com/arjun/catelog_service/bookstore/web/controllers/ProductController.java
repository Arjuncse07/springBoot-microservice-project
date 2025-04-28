package com.arjun.catelog_service.bookstore.web.controllers;

import com.arjun.catelog_service.bookstore.catelog.domain.PagedResult;
import com.arjun.catelog_service.bookstore.catelog.domain.Product;
import com.arjun.catelog_service.bookstore.catelog.domain.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/products")
class ProductController {

    private final ProductService productService;

     ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo ){
      return productService.getProducts(pageNo);
    }

}
