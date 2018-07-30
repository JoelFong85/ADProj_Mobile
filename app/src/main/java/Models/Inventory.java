package Models;

public class Inventory {
    private String itemCode;
    private String description;
    private String category;
    private String unitOfMeasurement;
    private int currentQuantity;
    private int reorderLevel;
    private int reorderQuantity;
    private int itemBin;
    private String itemStatus;
    private double priorityUnitPrice;

    public Inventory(String itemCode, String description, String category, String unitOfMeasurement, int currentQuantity, int reorderLevel, int reorderQuantity, int itemBin, String itemStatus, double priorityUnitPrice) {
        this.itemCode = itemCode;
        this.description = description;
        this.category = category;
        this.unitOfMeasurement = unitOfMeasurement;
        this.currentQuantity = currentQuantity;
        this.reorderLevel = reorderLevel;
        this.reorderQuantity = reorderQuantity;
        this.itemBin = itemBin;
        this.itemStatus = itemStatus;
        this.priorityUnitPrice = priorityUnitPrice;
    }

    public double getPriorityUnitPrice() {
        return priorityUnitPrice;
    }

    public void setPriorityUnitPrice(double priorityUnitPrice) {
        this.priorityUnitPrice = priorityUnitPrice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(int reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public int getItemBin() {
        return itemBin;
    }

    public void setItemBin(int itemBin) {
        this.itemBin = itemBin;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }
}
