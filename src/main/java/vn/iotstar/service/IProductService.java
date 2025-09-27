package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.entity.Product;

import java.util.Optional;

public interface IProductService {
    Page<Product> search(String keyword, Pageable pageable);
    Optional<Product> findById(Long id);
    Product save(Product product);
    void deleteById(Long id);

 
    Product saveWithCategory(Product product, Long categoryId);
}
