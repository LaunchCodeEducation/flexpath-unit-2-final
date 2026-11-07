package org.example.controllers;

import org.example.daos.ProductDao;
import org.example.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/products")
@PreAuthorize("isAuthenticated()")
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @GetMapping
    public List<Product> list() {
        return productDao.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable long id) {
        Product product = productDao.getProductById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        }
        return product;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Product add(@RequestBody Product product) {
        return productDao.createProduct(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable long id, @RequestBody Product product) {
        Product existing = productDao.getProductById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        }
        return productDao.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable long id) {
        int rowsAffected = productDao.deleteProduct(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        }
        return rowsAffected;
    }
}
