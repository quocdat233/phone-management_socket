package shared;

import client.view.shared.SidebarMenu;

import javax.swing.*;

public class MainView extends JFrame {
    private SidebarMenu sidebarMenu;
    private JPanel mainContent;
    private JButton menuButton; // nút để mở sidebar
    private boolean sidebarVisible = false;

    public MainView() {
        setTitle("Quản lý cửa hàng");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // dùng layout null để điều khiển chính xác vị trí sidebar

        sidebarMenu = new SidebarMenu();
        sidebarMenu.setBounds(-220, 0, 220, getHeight()); // Ẩn ban đầu (nằm ngoài màn hình)
        add(sidebarMenu);

        mainContent = new JPanel();
        mainContent.setBounds(0, 0, getWidth(), getHeight());
        mainContent.setLayout(null);

        menuButton = new JButton("≡");
        menuButton.setBounds(10, 10, 40, 30);
        menuButton.addActionListener(e -> toggleSidebar());

        mainContent.add(menuButton);
        add(mainContent);
    }

    private void toggleSidebar() {
        if (sidebarVisible) {
            // Ẩn đi
            new Thread(() -> {
                for (int i = 0; i <= 220; i += 10) {
                    sidebarMenu.setLocation(-i, 0);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                sidebarVisible = false;
            }).start();
        } else {
            // Hiện ra
            new Thread(() -> {
                for (int i = -220; i <= 0; i += 10) {
                    sidebarMenu.setLocation(i, 0);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                sidebarVisible = true;
            }).start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainView().setVisible(true);
        });
    }
}
