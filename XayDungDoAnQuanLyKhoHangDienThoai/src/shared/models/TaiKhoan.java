package shared.models;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class TaiKhoan implements Serializable {
    private static final long serialVersionUID = 1L;
    private int manv;
    private String username;
    private String matkhau;
    private int manhomquyen;
    private int trangthai;

    public TaiKhoan() {

    }

    public TaiKhoan(int manv, String username, String matkhau, int manhomquyen, int trangthai) {
        this.manv = manv;
        this.username = username;
        this.matkhau = matkhau;
        this.manhomquyen = manhomquyen;
        this.trangthai = trangthai;
    }

    public int getManv() {
        return manv;
    }

    public void setManv(int manv) {
        this.manv = manv;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public int getManhomquyen() {
        return manhomquyen;
    }

    public void setManhomquyen(int manhomquyen) {
        this.manhomquyen = manhomquyen;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.manv;
        hash = 19 * hash + Objects.hashCode(this.username);
        hash = 19 * hash + Objects.hashCode(this.matkhau);
        hash = 19 * hash + this.manhomquyen;
        hash = 19 * hash + this.trangthai;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaiKhoan other = (TaiKhoan) obj;
        if (this.manv != other.manv) {
            return false;
        }
        if (this.manhomquyen != other.manhomquyen) {
            return false;
        }
        if (this.trangthai != other.trangthai) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return Objects.equals(this.matkhau, other.matkhau);
    }

    @Override
    public String toString() {
        return "AccountDTO{" + "manv=" + manv + ", username=" + username + ", matkhau=" + matkhau + ", manhomquyen=" + manhomquyen + ", trangthai=" + trangthai + '}';
    }

    public boolean checkEmail(String email) {
        boolean check = false;
        String domain = email.substring(email.indexOf('@') + 1);
        try {
            // Kết nối đến máy chủ SMTP của domain
            Socket socket = new Socket("gmail-smtp-in.l.google.com", 25);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Đọc phản hồi từ server
            System.out.println("Server: " + reader.readLine());

            // Gửi các lệnh SMTP
            writer.write("HELO yourdomain.com\r\n");
            writer.flush();
            System.out.println("HELO Response: " + reader.readLine());

            writer.write("MAIL FROM:<your_email@yourdomain.com>\r\n");
            writer.flush();
            System.out.println("MAIL FROM Response: " + reader.readLine());

            writer.write("RCPT TO:<" + email + ">\r\n");
            writer.flush();
            String rcptResponse = reader.readLine();
            System.out.println("RCPT TO Response: " + rcptResponse);

            // Kiểm tra phản hồi
            if (rcptResponse.contains("250")) {
                System.out.println("Email tồn tại!");
                check = true;
            } else {
                System.out.println("Email không tồn tại.");
                check = false;
            }

            writer.write("QUIT\r\n");
            writer.flush();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean checkMatkhau(String pass) {
        boolean check = true;
        int demSoChuThuong = 0, demSoChuHoa = 0, demSoChuSo = 0, demSoKituDB = 0;
        for(int i = 0; i < pass.length(); i++) {
            if (pass.charAt(i) >= 'a' && pass.charAt(i) <= 'z') demSoChuThuong++;
            else if (pass.charAt(i) >= 'A' && pass.charAt(i) <= 'Z') demSoChuHoa++;
            else if (pass.charAt(i) >= '0' && pass.charAt(i) <= '9') demSoChuSo++;
            else demSoKituDB++;
        }

        if (pass.length() >= 6) {
            if (demSoChuHoa < 1 || demSoKituDB < 1) {
                check = false;
            }
        }
        return check;
    }

}