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

import vn.iotstar.entity.Product;
import vn.iotstar.model.ApiResponse;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.IStorageService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductApiController {

    private final IProductService productService;
    private final IStorageService storage;

   
    public ProductApiController(IProductService productService, IStorageService storage) {
        this.productService = productService;
        this.storage = storage;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> data = productService.search(keyword,
                PageRequest.of(page, size, Sort.by("productId").descending()));
        return ResponseEntity.ok(new ApiResponse(true, "OK", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> get(@PathVariable Long id) {
        Optional<Product> p = productService.findById(id);
        return p.map(prod -> ResponseEntity.ok(new ApiResponse(true, "OK", prod)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Product not found", null)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> create(
            @Validated @RequestParam @NotBlank String productName,
            @Validated @RequestParam @PositiveOrZero int quantity,
            @Validated @RequestParam @PositiveOrZero double unitPrice,
            @RequestParam(required = false) MultipartFile images,
            @Validated @RequestParam @NotBlank String description,
            @RequestParam(defaultValue = "0") double discount,
            @RequestParam(defaultValue = "1") short status,
            @RequestParam Long categoryId) {

        Product p = new Product();
        p.setProductName(productName);
        p.setQuantity(quantity);
        p.setUnitPrice(unitPrice);
        p.setDescription(description);
        p.setDiscount(discount);
        p.setStatus(status);

        if (images != null && !images.isEmpty()) {
            String fileName = storage.getStorageFilename(images, "pro-" + UUID.randomUUID());
            storage.store(images, fileName);
            p.setImages(fileName);
        }

        Product saved = productService.saveWithCategory(p, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Created", saved));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> update(
            @PathVariable Long id,
            @Validated @RequestParam @NotBlank String productName,
            @Validated @RequestParam @PositiveOrZero int quantity,
            @Validated @RequestParam @PositiveOrZero double unitPrice,
            @RequestParam(required = false) MultipartFile images,
            @Validated @RequestParam @NotBlank String description,
            @RequestParam(defaultValue = "0") double discount,
            @RequestParam(defaultValue = "1") short status,
            @RequestParam Long categoryId) {

        Optional<Product> opt = productService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Product not found", null));
        }
        Product p = opt.get();
        p.setProductName(productName);
        p.setQuantity(quantity);
        p.setUnitPrice(unitPrice);
        p.setDescription(description);
        p.setDiscount(discount);
        p.setStatus(status);

        if (images != null && !images.isEmpty()) {
            String fileName = storage.getStorageFilename(images, "pro-" + UUID.randomUUID());
            storage.store(images, fileName);
            p.setImages(fileName);
        }

        Product saved = productService.saveWithCategory(p, categoryId);
        return ResponseEntity.ok(new ApiResponse(true, "Updated", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        Optional<Product> opt = productService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Product not found", null));
        }
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true, "Deleted", null));
    }
}
