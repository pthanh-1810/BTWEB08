package vn.iotstar.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.service.ICategoryService;

import java.util.*;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository repo;

    // ✅ Constructor injection thay cho Lombok
    public CategoryServiceImpl(CategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Category save(Category c) {
        if (c.getCategoryId() != null) {
            repo.findById(c.getCategoryId()).ifPresent(old -> {
                if (!StringUtils.hasText(c.getIcon())) {
                    c.setIcon(old.getIcon());
                }
            });
        }
        return repo.save(c);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return repo.findByCategoryNameIgnoreCase(name);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Category> findAll() {
        return repo.findAll();
    }

    @Override
    public Page<Category> search(String keyword, Pageable pageable) {
        String k = (keyword == null) ? "" : keyword;
        return repo.findByCategoryNameContainingIgnoreCase(k, pageable);
    }
}
