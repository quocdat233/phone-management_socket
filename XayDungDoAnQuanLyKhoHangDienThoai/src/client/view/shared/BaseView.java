package client.view.shared;

import shared.MiniSidebarPanel;

import javax.swing.*;
import java.awt.*;

public class BaseView extends JFrame {
    protected JPanel mainPanel;
    protected CardLayout cardLayout;
    protected SidebarMenu sidebarMenu;
    protected MiniSidebarPanel miniSidebar;
    protected JLayeredPane layeredPane;
    private JPanel overlayPanel;

    private final int SIDEBAR_WIDTH = 220;
    private boolean sidebarVisible = false;

    public BaseView() {
        setTitle("Hệ Thống Quản Lý Kho Hàng");
        setSize(1300, 770);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // LayeredPane để chồng sidebar lên
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

        // Main content panel với CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);

        // Mini sidebar chứa nút mở
        miniSidebar = new MiniSidebarPanel(this::openSidebar);
        miniSidebar.setBounds(0, 10, 40, 40);
        layeredPane.add(miniSidebar, JLayeredPane.PALETTE_LAYER);

        // Sidebar chính
        sidebarMenu = new SidebarMenu();
        sidebarMenu.setBounds(-SIDEBAR_WIDTH, 0, SIDEBAR_WIDTH, getHeight());
//        sidebarMenu.setOnCloseListener(this::closeSidebar);
        layeredPane.add(sidebarMenu, JLayeredPane.MODAL_LAYER);

        // Overlay trong suốt (bắt sự kiện click ngoài)
        overlayPanel = new JPanel() {
            @Override
            public boolean isOpaque() {
                return false;
            }

            @Override
            protected void paintComponent(Graphics g) {
                // không vẽ gì (trong suốt)
            }

            @Override
            public boolean contains(int x, int y) {
                // Chỉ bắt sự kiện ngoài sidebar
                if (x < sidebarMenu.getWidth()) {
                    return false;
                }
                return true;
            }
        };
        overlayPanel.setLayout(null);
        overlayPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                slideCloseSidebar();
            }
        });
        setGlassPane(overlayPanel);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int width = getWidth();
                int height = getHeight();

                mainPanel.setSize(width, height);
                overlayPanel.setSize(width, height);

                sidebarMenu.setSize(SIDEBAR_WIDTH, height);
                if (sidebarVisible) {
                    sidebarMenu.setLocation(0, 0); // đã mở
                } else {
                    sidebarMenu.setLocation(-SIDEBAR_WIDTH, 0); // đang đóng
                }

                miniSidebar.setLocation(0, 10); // giữ ở góc trên trái
                miniSidebar.setSize(40, 40);
                layeredPane.revalidate();
                layeredPane.repaint();
            }
        });

    }

    // Mở sidebar có hiệu ứng trượt
    public void openSidebar() {
        overlayPanel.setVisible(true);
        miniSidebar.setVisible(false);

        new Thread(() -> {
            for (int x = -SIDEBAR_WIDTH; x <= 0; x += 10) {
                sidebarMenu.setLocation(x, 0);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {}
            }
            sidebarVisible = true;
        }).start();
    }

    // Đóng sidebar có hiệu ứng trượt
    public void slideCloseSidebar() {
        new Thread(() -> {
            for (int x = 0; x >= -SIDEBAR_WIDTH; x -= 10) {
                sidebarMenu.setLocation(x, 0);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {}
            }
            sidebarVisible = false;
            SwingUtilities.invokeLater(() -> {
                overlayPanel.setVisible(false);
                miniSidebar.setVisible(true);
            });
        }).start();
    }

    public void closeSidebar() {
        slideCloseSidebar();
    }

    public void addView(JPanel panel, String name) {
        mainPanel.add(panel, name);
    }

    public void showView(String name) {
        cardLayout.show(mainPanel, name);
    }

    // Getters
    public SidebarMenu getSidebarMenu() { return sidebarMenu; }
    public JPanel getMainPanel() { return mainPanel; }
    public MiniSidebarPanel getMiniSidebar() { return miniSidebar; }
    public CardLayout getCardLayout() { return cardLayout; }
}