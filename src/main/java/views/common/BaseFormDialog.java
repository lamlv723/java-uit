package views.common;

import utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Dialog form cơ sở: header, drag, nút Save/Cancel bo góc.
@SuppressWarnings("serial")
public abstract class BaseFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private final String iconName;
    private final String titleText;
    protected boolean saved = false;
    private Point dragOffset;

    protected BaseFormDialog(Frame owner,
                             int width,
                             int height,
                             String iconName,
                             String titleText){
        super(owner, true);
        this.iconName = iconName;
        this.titleText = titleText;
        setUndecorated(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(owner);
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(new Color(210,210,210),2));
        setContentPane(content);
    }

    // Gọi sau khi subclass init xong field
    protected void buildUI(){
        getContentPane().add(buildHeader(), BorderLayout.NORTH);
        JPanel form = buildFormPanel();
        form.setOpaque(false);
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buildButtonBar(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader(){
    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(8,20,8,8));
        JLabel icon = new JLabel(ui.IconFactory.get(iconName,20));
        icon.setBorder(BorderFactory.createEmptyBorder(0,0,0,8));
        JLabel title = new JLabel(titleText);
        title.setFont(UITheme.fontBold(15));
    JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,0,6)); left.setOpaque(false); left.add(icon); left.add(title);
    header.add(left, BorderLayout.WEST);
    // drag
        MouseAdapter dragger=new MouseAdapter(){ public void mousePressed(MouseEvent e){dragOffset=e.getPoint();} public void mouseDragged(MouseEvent e){ if(dragOffset!=null){ Point p=e.getLocationOnScreen(); setLocation(p.x-dragOffset.x, p.y-dragOffset.y);} } };
        header.addMouseListener(dragger); header.addMouseMotionListener(dragger);
        return header;
    }

    private JPanel buildButtonBar(){
        JPanel bar=new JPanel(new FlowLayout(FlowLayout.CENTER,16,8)); bar.setOpaque(false); bar.setBorder(BorderFactory.createEmptyBorder(4,24,16,24));
        JButton save=button(getSaveButtonText(), UITheme.SUCCESS_GREEN); JButton cancel=button(getCancelButtonText(), new Color(107,114,128));
        save.addActionListener(e-> onSave()); cancel.addActionListener(e-> dispose());
        bar.add(save); bar.add(cancel); return bar;
    }

    protected String getSaveButtonText() {
        return "Lưu";
    }

    protected String getCancelButtonText() {
        return "Hủy";
    }

    private JButton button(String text, Color color){
        JButton b=new JButton(text){
            protected void paintComponent(Graphics g){ Graphics2D g2=(Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(getBackground()); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10); g2.dispose(); super.paintComponent(g);} public void updateUI(){ super.updateUI(); setContentAreaFilled(false); setOpaque(false);} };
        b.setBackground(color); b.setForeground(Color.WHITE); b.setFont(UITheme.fontMedium(14)); b.setBorder(BorderFactory.createEmptyBorder(8,18,8,18)); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); b.setFocusPainted(false); return b; }

    protected abstract JPanel buildFormPanel();
    protected abstract void onSave();

    public boolean isSaved(){ return saved; }
}
