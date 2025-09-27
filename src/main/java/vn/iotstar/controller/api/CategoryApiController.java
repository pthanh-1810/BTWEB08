package vn.iotstar.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.entity.Category;
import vn.iotstar.model.ApiResponse;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IStorageService;

import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryApiController {

    private final ICategoryService categoryService;
    private final IStorageService storage;


    public CategoryApiController(ICategoryService categoryService, IStorageService storage) {
        this.categoryService = categoryService;
        this.storage = storage;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Category> data = categoryService.search(keyword,
                PageRequest.of(page, size, Sort.by("categoryId").descending()));
        return ResponseEntity.ok(new ApiResponse(true, "OK", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> get(@PathVariable Long id) {
        Optional<Category> c = categoryService.findById(id);
        return c.map(cat -> ResponseEntity.ok(new ApiResponse(true, "OK", cat)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Category not found", null)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> create(
            @Validated @RequestParam @NotBlank String categoryName,
            @RequestParam(required = false) MultipartFile icon) {

        if (categoryService.findByName(categoryName).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Category name already exists", null));
        }
        Category c = new Category();
        c.setCategoryName(categoryName);

        if (icon != null && !icon.isEmpty()) {
            String fileName = storage.getStorageFilename(icon, "cat-" + UUID.randomUUID());
            storage.store(icon, fileName);
            c.setIcon(fileName);
        }
        Category saved = categoryService.save(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Created", saved));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> update(
            @PathVariable Long id,
            @Validated @RequestParam @NotBlank String categoryName,
            @RequestParam(required = false) MultipartFile icon) {

        Optional<Category> opt = categoryService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Category not found", null));
        }
        Category c = opt.get();
        c.setCategoryName(categoryName);

        if (icon != null && !icon.isEmpty()) {
            String fileName = storage.getStorageFilename(icon, "cat-" + UUID.randomUUID());
            storage.store(icon, fileName);
            c.setIcon(fileName);
        }
        Category saved = categoryService.save(c);
        return ResponseEntity.ok(new ApiResponse(true, "Updated", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        Optional<Category> opt = categoryService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Category not found", null));
        }
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true, "Deleted", null));
    }
}
