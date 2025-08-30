package views.main;

import controllers.main.DepartmentController;
import controllers.user.UserSession;
import models.main.Department;
import models.main.Employee;
import ui.IconFactory;
import utils.UITheme;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Dialog thêm / sửa Phòng ban với UI thống nhất. */
public class DepartmentFormDialog extends JDialog {
    private final DepartmentController controller;
    private final Department department; // null => add
    private JTextField tfName;
    private JTextField tfHeadId;
    private boolean saved = false;
    private Point dragOffset;

    public DepartmentFormDialog(Frame owner, DepartmentController controller, Department dept) {
        super(owner, true);
        this.controller = controller;
        this.department = dept;
        setUndecorated(true);
        setSize(480, 300);
        setLocationRelativeTo(owner);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(new Color(210,210,210),2));
        setContentPane(content);

        content.add(buildHeader(), BorderLayout.NORTH);
        initForm();
        content.add(buildFormPanel(), BorderLayout.CENTER);
        content.add(buildButtonBar(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 8));
    JLabel icon = new JLabel(IconFactory.get("cogs", 20));
        icon.setBorder(BorderFactory.createEmptyBorder(0,0,0,8));
        JLabel title = new JLabel(department == null ? "Thêm Phòng ban" : "Chỉnh sửa Phòng ban");
        title.setFont(UITheme.fontBold(15));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,0,6)); left.setOpaque(false); left.add(icon); left.add(title);
        header.add(left, BorderLayout.WEST);
    JButton btnClose = new JButton(IconFactory.get("times", 14));
    btnClose.setToolTipText("Đóng");
    btnClose.setBorderPainted(false);
    btnClose.setFocusPainted(false);
    btnClose.setContentAreaFilled(false);
    btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btnClose.addActionListener(e -> dispose());
    header.add(btnClose, BorderLayout.EAST);
        MouseAdapter dragger = new MouseAdapter(){ public void mousePressed(MouseEvent e){dragOffset=e.getPoint();} public void mouseDragged(MouseEvent e){ if(dragOffset!=null){ Point p=e.getLocationOnScreen(); setLocation(p.x-dragOffset.x,p.y-dragOffset.y);} } }; header.addMouseListener(dragger); header.addMouseMotionListener(dragger);
        return header;
    }

    private void initForm() {
        tfName = new JTextField();
        tfHeadId = new JTextField();
        if (department != null) {
            tfName.setText(department.getDepartmentName());
            if (department.getHeadEmployee()!=null)
                tfHeadId.setText(String.valueOf(department.getHeadEmployee().getEmployeeId()));
        }
    }

    private JPanel buildFormPanel() {
        JPanel p = new JPanel(new GridBagLayout()); p.setOpaque(false); p.setBorder(BorderFactory.createEmptyBorder(16,24,8,24));
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets=new Insets(6,8,6,8); gbc.anchor=GridBagConstraints.WEST; gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1; gbc.gridx=0; gbc.gridy=0;
        addField(p,gbc,"Tên phòng ban *", tfName);
        addField(p,gbc,"ID trưởng phòng", tfHeadId);
        return p;
    }
    private void addField(JPanel p, GridBagConstraints gbc, String label, JComponent input){ gbc.gridwidth=1; gbc.gridx=0; p.add(new JLabel(label),gbc); gbc.gridx=1; p.add(input,gbc); gbc.gridy++; }

    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(4, 24, 16, 24));
        JButton save = btn("Lưu", UITheme.SUCCESS_GREEN);
        JButton cancel = btn("Hủy", new Color(107, 114, 128));
        save.addActionListener(e -> onSave());
        cancel.addActionListener(e -> dispose());
        bar.add(save);
        bar.add(cancel);
        return bar;
    }
    private JButton btn(String text, Color color){ JButton b=new JButton(text){ protected void paintComponent(Graphics g){ Graphics2D g2=(Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(getBackground()); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10); g2.dispose(); super.paintComponent(g);} public void updateUI(){ super.updateUI(); setContentAreaFilled(false); setOpaque(false);} }; b.setBackground(color); b.setForeground(Color.WHITE); b.setFont(UITheme.fontMedium(14)); b.setBorder(BorderFactory.createEmptyBorder(8,18,8,18)); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); b.setFocusPainted(false); return b; }

    private void onSave(){ if(tfName.getText().trim().isEmpty()){ UIUtils.showErrorDialog(this,"Tên phòng ban không được rỗng","Lỗi"); return; } Department d = department==null? new Department(): department; d.setDepartmentName(tfName.getText().trim()); String headStr=tfHeadId.getText().trim(); if(!headStr.isEmpty()){ try{ int hid=Integer.parseInt(headStr); Employee head = new Employee(); head.setEmployeeId(hid); d.setHeadEmployee(head);} catch(NumberFormatException ex){ UIUtils.showErrorDialog(this,"ID trưởng phòng phải là số","Lỗi"); return;} } else { d.setHeadEmployee(null);} Employee user= UserSession.getInstance().getLoggedInEmployee(); try { if(department==null) controller.addDepartment(d,user); else controller.updateDepartment(d,user); saved=true; dispose(); } catch(Exception ex){ UIUtils.showErrorDialog(this,"Không thể lưu phòng ban: "+ex.getMessage(),"Lỗi Hệ thống"); ex.printStackTrace(); } }

    public boolean isSaved(){ return saved; }
}
