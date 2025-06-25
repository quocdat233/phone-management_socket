package client.controller;

import client.VKULogin;
import client.view.shared.Toast;
import com.formdev.flatlaf.FlatIntelliJLaf;
import network.SocketManager;
import shared.models.TaiKhoan;
import shared.models.NhanVien;
import shared.request.LoginRequest;
import shared.response.LoginResponse;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dangNhapController {
    private VKULogin loginView;
    private final ImageIcon successIcon = new ImageIcon(getClass().getResource("/images/success.png"));
    private final ImageIcon warningIcon = new ImageIcon(getClass().getResource("/images/Warring.png"));

    public dangNhapController(VKULogin loginView) {
        this.loginView = loginView;
        initController();
    }

    private void initController() {
        loginView.getPnlDangNhap().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    checkLogin();
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(VKULogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void checkLogin() throws UnsupportedLookAndFeelException {
        String username = loginView.getTxtTaiKhoan().getText().trim();
        String password = new String(loginView.getTxtMatKhau().getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Vui lòng nhập đầy đủ tài khoản và mật khẩu", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Gửi yêu cầu đăng nhập
            LoginRequest request = new LoginRequest(username, password);
            SocketManager.getInstance().send(request);

            // Nhận phản hồi
            Object response = SocketManager.getInstance().receive();
            if (response instanceof LoginResponse loginResponse) {
                if (loginResponse.isSuccess()) {
                    TaiKhoan tk = loginResponse.getTaiKhoan();
                    NhanVien nv = loginResponse.getNhanVien();

                    if (tk.getTrangthai() == 0) {
                        JOptionPane.showMessageDialog(loginView, "Tài khoản đang bị khóa. Vui lòng liên hệ quản trị viên.", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    new Toast(loginView, "Thành công", "Đăng nhập thành công,\nChào " + tk.getUsername() + "!", 1500, successIcon);

                    Timer timer = new Timer(300, e -> {
                        try {
                            UIManager.setLookAndFeel(new FlatIntelliJLaf());
                            new AppController(nv, tk);
                            loginView.dispose();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();

                } else {
                    JOptionPane.showMessageDialog(loginView, loginResponse.getMessage(), "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                String className = (response != null) ? response.getClass().getName() : "null";
                JOptionPane.showMessageDialog(loginView,
                        "Phản hồi không hợp lệ từ server.\nLoại nhận được: " + className,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // 👉 In toàn bộ lỗi ra console
            JOptionPane.showMessageDialog(
                    loginView,
                    "Không thể kết nối tới server: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}