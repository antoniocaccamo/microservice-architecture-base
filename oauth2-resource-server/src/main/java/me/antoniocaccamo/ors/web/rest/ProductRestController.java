package me.antoniocaccamo.ors.web.rest;


import me.antoniocaccamo.ors.domain.Product;
import me.antoniocaccamo.ors.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ahmed on 30.5.18.
 */

@RestController
@RequestMapping(value = "/product")
public class ProductRestController {


    private final ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @GetMapping
    public List<Product> products() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PRODUCT_ADMIN')")
    public Product getProduct(@PathVariable("id") Long id) {
        return productRepository.findById(id)
                                 .orElse(null);
    }

    @GetMapping("/search/")
    @PreAuthorize("hasRole('ROLE_PRODUCT_ADMIN')")
    public Product findByName(@RequestParam("name") String name) {
        return productRepository.findByNameLike("%" + name + "%");
    }
    @GetMapping("/products")
    @PreAuthorize("hasRole('ROLE_PRODUCT_ADMIN')")
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
