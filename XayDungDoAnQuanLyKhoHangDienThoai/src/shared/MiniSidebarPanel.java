package shared;

import javax.swing.*;
import java.awt.*;

public class MiniSidebarPanel extends JPanel {
    public MiniSidebarPanel(Runnable onOpenClick) {
        setOpaque(false);
        setLayout(new BorderLayout());

        JButton openBtn = new JButton("≡");
        openBtn.setFont(new Font("Arial", Font.PLAIN, 30));
        openBtn.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        openBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        openBtn.setFocusPainted(false);
        openBtn.setContentAreaFilled(false);
        openBtn.setBorderPainted(false);

        // Căn trái + trên
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setOpaque(false);
        wrapper.add(openBtn);

        add(wrapper, BorderLayout.NORTH);

        openBtn.addActionListener(e -> onOpenClick.run());
    }
}
