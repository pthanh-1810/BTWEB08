package vn.iotstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "products")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @NotBlank
    @Column(name = "product_name", nullable = false, length = 500, columnDefinition = "nvarchar(500)")
    private String productName;

    @Min(0)
    @Column(nullable = false)
    private int quantity;

    @PositiveOrZero
    @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    @Column(length = 255)
    private String images;

    @NotBlank
    @Column(nullable = false, columnDefinition = "nvarchar(500)")
    private String description;

    @PositiveOrZero
    @Column(nullable = false)
    private double discount;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private Instant createDate;

    @Column(nullable = false)
    private short status; // 1 active, 0 inactive

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // ✅ Constructors
    public Product() {
    }

    public Product(Long productId, String productName, int quantity, double unitPrice,
                   String images, String description, double discount, Instant createDate,
                   short status, Category category) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.images = images;
        this.description = description;
        this.discount = discount;
        this.createDate = createDate;
        this.status = status;
        this.category = category;
    }

    // ✅ Getters & Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public Instant getCreateDate() { return createDate; }
    public void setCreateDate(Instant createDate) { this.createDate = createDate; }

    public short getStatus() { return status; }
    public void setStatus(short status) { this.status = status; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
