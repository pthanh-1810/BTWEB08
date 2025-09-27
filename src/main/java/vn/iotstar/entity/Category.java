package vn.iotstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @NotBlank
    @Column(name = "category_name", nullable = false, length = 200)
    private String categoryName;

    @Column(name = "icon")
    private String icon;

    public Category() {
    }

    public Category(Long categoryId, String categoryName, String icon) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.icon = icon;
    }

    // ✅ Getter/Setter
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
