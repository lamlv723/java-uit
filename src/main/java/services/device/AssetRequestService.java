package services.device;

import dao.device.AssetRequestDAOImpl;
import dao.device.AssetRequestItemDAOImpl;
import dao.device.AssetDAOImpl;
import dao.device.interfaces.AssetRequestDAO;
import dao.device.interfaces.AssetRequestItemDAO;
import dao.device.interfaces.AssetDAO;
import models.device.Asset;
import models.device.AssetRequest;
import models.device.AssetRequestItem;
import models.main.Employee;
import exceptions.ValidationException;
import exceptions.NotFoundException;

import java.util.Date;
import java.util.List;

public class AssetRequestService {
    private AssetRequestDAO assetRequestDAO;
    private AssetRequestItemDAO assetRequestItemDAO;
    private AssetDAO assetDAO;

    public AssetRequestService() {
        this.assetRequestDAO = new AssetRequestDAOImpl();
        this.assetRequestItemDAO = new AssetRequestItemDAOImpl();
        this.assetDAO = new AssetDAOImpl();
    }

    // Các phương thức cũ
    public void addAssetRequest(AssetRequest request, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestDAO.addAssetRequest(request);
    }

    public void updateAssetRequest(AssetRequest request, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestDAO.updateAssetRequest(request);
    }

    public void deleteAssetRequest(int requestId, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestDAO.deleteAssetRequest(requestId);
    }

    public AssetRequest getAssetRequestById(int requestId) {
        return assetRequestDAO.getAssetRequestById(requestId);
    }

    public List<AssetRequest> getAllAssetRequests() {
        return assetRequestDAO.getAllAssetRequests();
    }

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm AssetRequest từ dữ liệu đầu vào dạng String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     * Phương thức cũ này chỉ tạo một request trống, chúng ta cần thay đổi nó.
     */
    // Phương thức cũ đã bị thay thế bởi phương thức mới dưới đây
    public String addAssetRequestFromInput(String title, String desc, String currentUserRole) {
        if (title == null || title.isEmpty()) {
            return "Tiêu đề không được để trống!";
        }
        AssetRequest req = new AssetRequest();
        req.setRequestType(title); // Dùng requestType làm tiêu đề
        req.setStatus(desc); // Dùng status làm mô tả
        try {
            addAssetRequest(req, currentUserRole);
        } catch (Exception ex) {
            return "Lỗi khi thêm yêu cầu: " + ex.getMessage();
        }
        return null;
    }

    // MARK: - LOGIC MỚI BỔ SUNG

    /**
     * Tạo một yêu cầu mượn thiết bị mới với các thông tin cơ bản.
     * @param employee Người tạo yêu cầu
     * @param requestType Loại yêu cầu (mượn/trả)
     * @param expectedReturnDate Ngày dự kiến trả
     * @return AssetRequest đã được tạo
     * @throws ValidationException nếu dữ liệu không hợp lệ
     */
    public AssetRequest createAssetRequest(Employee employee, String requestType, Date expectedReturnDate) throws ValidationException {
        if (employee == null) {
            throw new ValidationException("Thông tin nhân viên không được để trống!");
        }
        if (requestType == null || requestType.isEmpty()) {
            throw new ValidationException("Loại yêu cầu không được để trống!");
        }
        
        AssetRequest newRequest = new AssetRequest();
        newRequest.setEmployee(employee);
        newRequest.setRequestType(requestType);
        newRequest.setRequestDate(new Date());
        newRequest.setStatus("Pending"); // Trạng thái mặc định là chờ duyệt
        newRequest.setExpectedReturnDate(expectedReturnDate);
        
        assetRequestDAO.addAssetRequest(newRequest);
        return newRequest;
    }

    /**
     * Thêm một chi tiết yêu cầu vào một yêu cầu đã tồn tại.
     * @param requestId ID của yêu cầu cha
     * @param assetId ID của thiết bị
     * @param quantity Số lượng
     * @throws NotFoundException nếu không tìm thấy yêu cầu hoặc thiết bị
     * @throws ValidationException nếu số lượng không hợp lệ hoặc vượt quá số lượng tồn kho
     */
    public void addAssetRequestItemToRequest(int requestId, int assetId, int quantity) throws NotFoundException, ValidationException {
        AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
        if (request == null) {
            throw new NotFoundException("Không tìm thấy yêu cầu với ID: " + requestId);
        }
        
        Asset asset = assetDAO.getById(assetId);
        if (asset == null) {
            throw new NotFoundException("Không tìm thấy thiết bị với ID: " + assetId);
        }

        if (quantity <= 0) {
            throw new ValidationException("Số lượng phải lớn hơn 0!");
        }
        
        // TODO: Kiểm tra số lượng tồn kho của thiết bị (nếu cần)
        // Hiện tại không có trường quantity trong Asset, bạn cần bổ sung
        // if (asset.getQuantity() < quantity) {
        //     throw new ValidationException("Số lượng thiết bị không đủ!");
        // }
        
        AssetRequestItem newItem = new AssetRequestItem();
        newItem.setAssetRequest(request);
        newItem.setAsset(asset);
        newItem.setQuantity(quantity); // Quantity ở đây là số lượng mượn
        
        assetRequestItemDAO.addAssetRequestItem(newItem);
    }
    
    /**
     * Duyệt một yêu cầu mượn thiết bị.
     * @param requestId ID của yêu cầu cần duyệt
     * @param approver Người duyệt
     * @throws NotFoundException nếu không tìm thấy yêu cầu
     * @throws ValidationException nếu yêu cầu không ở trạng thái "Pending"
     */
    public void approveAssetRequest(int requestId, Employee approver) throws NotFoundException, ValidationException {
        AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
        if (request == null) {
            throw new NotFoundException("Không tìm thấy yêu cầu với ID: " + requestId);
        }
        
        if (!request.getStatus().equals("Pending")) {
            throw new ValidationException("Chỉ có thể duyệt yêu cầu ở trạng thái Pending.");
        }
        
        request.setStatus("APPROVED");
        request.setApprover(approver);
        request.setApprovalDate(new Date());
        
        assetRequestDAO.updateAssetRequest(request);
        
        // TODO: Cập nhật số lượng tồn kho của Asset khi duyệt
        // Bạn cần lấy tất cả AssetRequestItem của request này và giảm số lượng tồn kho của Asset
    }

    /**
     * Từ chối một yêu cầu mượn thiết bị.
     * @param requestId ID của yêu cầu cần từ chối
     * @param approver Người từ chối
     * @throws NotFoundException nếu không tìm thấy yêu cầu
     * @throws ValidationException nếu yêu cầu không ở trạng thái "Pending"
     */
    // public void rejectAssetRequest(int requestId, Employee approver) throws NotFoundException, ValidationException {
    //     AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
    //     if (request == null) {
    //         throw new NotFoundException("Không tìm thấy yêu cầu với ID: " + requestId);
    //     }

    //     if (!request.getStatus().equals("Pending")) {
    //         throw new ValidationException("Chỉ có thể từ chối yêu cầu ở trạng thái Pending.");
    //     }

    //     request.setStatus("Rejected");
    //     request.setApprover(approver);
    //     request.setApprovalDate(new Date());

    //     assetRequestDAO.updateAssetRequest(request);
    // }

    public void rejectAssetRequest(int requestId) throws NotFoundException, ValidationException {
        AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
        if (request == null) {
            throw new NotFoundException("Không tìm thấy yêu cầu với ID: " + requestId);
        }

        if (!request.getStatus().equals("Pending")) {
            throw new ValidationException("Chỉ có thể từ chối yêu cầu ở trạng thái Pending.");
        }

        request.setStatus("Rejected");
        // request.setApprover(approver);
        request.setApprovalDate(new Date());

        assetRequestDAO.updateAssetRequest(request);
    }

    /**
     * Hoàn thành/trả một yêu cầu mượn thiết bị.
     * @param requestId ID của yêu cầu cần hoàn thành
     * @throws NotFoundException nếu không tìm thấy yêu cầu
     * @throws ValidationException nếu yêu cầu không ở trạng thái "APPROVED"
     */
    public void completeAssetRequest(int requestId) throws NotFoundException, ValidationException {
        AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
        if (request == null) {
            throw new NotFoundException("Không tìm thấy yêu cầu với ID: " + requestId);
        }
        
        if (!request.getStatus().equals("APPROVED")) {
            throw new ValidationException("Chỉ có thể hoàn thành yêu cầu đã được APPROVED.");
        }
        
        request.setStatus("COMPLETED");
        assetRequestDAO.updateAssetRequest(request);

        // TODO: Cập nhật returnDate cho các AssetRequestItem
        // TODO: Cập nhật số lượng tồn kho của Asset khi hoàn thành
    }
}