package Models;

import java.util.Date;

public class RequisitionOrder {
    private String requisitionId;
    private String employeeId;
    private String requisitionStatus;
    private Date requisitionDate;
    private String headComment;

    private String itemNumber;
    private String itemRequisitionQuantity;
    private String itemDistributedQuantity;
    private String itemPendingQuantity;
    private String itemRequisitionPrice;

    private String description;


    public RequisitionOrder(String requisitionId, String employeeId, String requisitionStatus, Date requisitionDate, String headComment, String itemNumber, String itemRequisitionQuantity, String itemDistributedQuantity, String itemPendingQuantity, String itemRequisitionPrice) {
        this.requisitionId = requisitionId;
        this.employeeId = employeeId;
        this.requisitionStatus = requisitionStatus;
        this.requisitionDate = requisitionDate;
        this.headComment = headComment;
        this.itemNumber = itemNumber;
        this.itemRequisitionQuantity = itemRequisitionQuantity;
        this.itemDistributedQuantity = itemDistributedQuantity;
        this.itemPendingQuantity = itemPendingQuantity;
        this.itemRequisitionPrice = itemRequisitionPrice;
    }

    public RequisitionOrder(String requisitionId, String itemNumber, String itemRequisitionQuantity, String itemDistributedQuantity, String itemPendingQuantity, String itemRequisitionPrice) {
        this.requisitionId = requisitionId;
        this.itemNumber = itemNumber;
        this.itemRequisitionQuantity = itemRequisitionQuantity;
        this.itemDistributedQuantity = itemDistributedQuantity;
        this.itemPendingQuantity = itemPendingQuantity;
        this.itemRequisitionPrice = itemRequisitionPrice;
    }


    public RequisitionOrder(String requisitionId, String itemNumber, String itemRequisitionQuantity, String itemDistributedQuantity, String itemPendingQuantity, String itemRequisitionPrice, String description) {
        this.requisitionId = requisitionId;
        this.itemNumber = itemNumber;
        this.itemRequisitionQuantity = itemRequisitionQuantity;
        this.itemDistributedQuantity = itemDistributedQuantity;
        this.itemPendingQuantity = itemPendingQuantity;
        this.itemRequisitionPrice = itemRequisitionPrice;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(String requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public Date getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(Date requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public String getHeadComment() {
        return headComment;
    }

    public void setHeadComment(String headComment) {
        this.headComment = headComment;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemRequisitionQuantity() {
        return itemRequisitionQuantity;
    }

    public void setItemRequisitionQuantity(String itemRequisitionQuantity) {
        this.itemRequisitionQuantity = itemRequisitionQuantity;
    }

    public String getItemDistributedQuantity() {
        return itemDistributedQuantity;
    }

    public void setItemDistributedQuantity(String itemDistributedQuantity) {
        this.itemDistributedQuantity = itemDistributedQuantity;
    }

    public String getItemPendingQuantity() {
        return itemPendingQuantity;
    }

    public void setItemPendingQuantity(String itemPendingQuantity) {
        this.itemPendingQuantity = itemPendingQuantity;
    }

    public String getItemRequisitionPrice() {
        return itemRequisitionPrice;
    }

    public void setItemRequisitionPrice(String itemRequisitionPrice) {
        this.itemRequisitionPrice = itemRequisitionPrice;
    }
}
