package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.Product;
import java.util.*;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductNameIgnoreCase(String name);
    Page<Product> findByProductNameContainingIgnoreCase(String name, Pageable pageable);
}
