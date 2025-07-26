package models.device;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "AssetRequestItem")
public class AssetRequestItem {
    // Convenience getter/setter for UI compatibility
    public Integer getAssetId() {
        return asset != null ? asset.getAssetId() : null;
    }

    public void setAssetId(Integer assetId) {
        if (this.asset == null)
            this.asset = new Asset();
        this.asset.setAssetId(assetId);
    }

    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_item_id")
    private Integer requestItemId;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private AssetRequest assetRequest;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(name = "borrow_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date borrowDate;

    @Column(name = "return_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    @Column(name = "condition_on_borrow")
    private String conditionOnBorrow;

    @Column(name = "condition_on_return")
    private String conditionOnReturn;

    // Getters and setters
    public Integer getRequestItemId() {
        return requestItemId;
    }

    public void setRequestItemId(Integer requestItemId) {
        this.requestItemId = requestItemId;
    }

    public AssetRequest getAssetRequest() {
        return assetRequest;
    }

    public void setAssetRequest(AssetRequest assetRequest) {
        this.assetRequest = assetRequest;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getConditionOnBorrow() {
        return conditionOnBorrow;
    }

    public void setConditionOnBorrow(String conditionOnBorrow) {
        this.conditionOnBorrow = conditionOnBorrow;
    }

    public String getConditionOnReturn() {
        return conditionOnReturn;
    }

    public void setConditionOnReturn(String conditionOnReturn) {
        this.conditionOnReturn = conditionOnReturn;
    }
}
