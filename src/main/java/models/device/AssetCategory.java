package models.device;

import javax.persistence.*;

@Entity
@Table(name = "AssetCategory")
public class AssetCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;

    @Column(name = "description")
    private String description;

    // Getters and setters
    public AssetCategory(String name) {
        this.categoryName = name;
    }

    public AssetCategory() {}


    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return categoryName; // Hiển thị tên danh mục trong JComboBox
    }
}
