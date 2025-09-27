package vn.iotstar.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.service.IProductService;

import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository repo;
    private final CategoryRepository categoryRepo;

    // ✅ Constructor injection (không dùng Lombok)
    public ProductServiceImpl(ProductRepository repo, CategoryRepository categoryRepo) {
        this.repo = repo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Product save(Product p) {
        return repo.save(p);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Page<Product> search(String keyword, Pageable pageable) {
        String k = (keyword == null) ? "" : keyword;
        return repo.findByProductNameContainingIgnoreCase(k, pageable);
    }

    @Override
    public Product saveWithCategory(Product product, Long categoryId) {
        return categoryRepo.findById(categoryId)
                .map(cat -> {
                    product.setCategory(cat);
                    return repo.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Category not found with id = " + categoryId));
    }
}
