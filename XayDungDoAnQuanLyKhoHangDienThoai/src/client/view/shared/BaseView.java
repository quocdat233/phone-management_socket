package client.view.shared;

import javax.swing.*;
import java.awt.*;

public class BaseView extends JFrame {
    protected JPanel mainPanel;
    protected CardLayout cardLayout;
    protected SidebarMenu sidebarMenu;

    public BaseView() {
        setTitle("Hệ Thống Quản Lý Kho Hàng");
        setSize(1300, 770);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        sidebarMenu = new SidebarMenu();
        add(sidebarMenu, BorderLayout.WEST);

        // CardLayout để chuyển giữa các view
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel, BorderLayout.CENTER);
    }

    public void setCardLayout(CardLayout cardLayout) {
        this.cardLayout = cardLayout;
    }

    public SidebarMenu getSidebarMenu() {
        return sidebarMenu;
    }

    public void setSidebarMenu(SidebarMenu sidebarMenu) {
        this.sidebarMenu = sidebarMenu;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }
}
