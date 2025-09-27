package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.entity.Category;
import java.util.*;

public interface ICategoryService {
    Category save(Category c);
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    void deleteById(Long id);
    java.util.List<Category> findAll();
    Page<Category> search(String keyword, Pageable pageable);
}
