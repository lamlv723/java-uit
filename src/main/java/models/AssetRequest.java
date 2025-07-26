package models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AssetRequest")
public class AssetRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer requestId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "request_type", nullable = false)
    private String requestType;

    @Column(name = "request_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private Employee approver;

    @Column(name = "approval_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalDate;

    @Column(name = "expected_return_date")
    @Temporal(TemporalType.DATE)
    private Date expectedReturnDate;

    // Getters and setters
    public Integer getRequestId() { return requestId; }
    public void setRequestId(Integer requestId) { this.requestId = requestId; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public Date getRequestDate() { return requestDate; }
    public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Employee getApprover() { return approver; }
    public void setApprover(Employee approver) { this.approver = approver; }
    public Date getApprovalDate() { return approvalDate; }
    public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; }
    public Date getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(Date expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }
}
