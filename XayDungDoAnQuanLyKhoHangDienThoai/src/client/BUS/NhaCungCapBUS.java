package client.BUS;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import server.DAO.NhaCungCapDAO;
import shared.models.NhaCungCap;

import java.util.ArrayList;

public class NhaCungCapBUS {

    private final NhaCungCapDAO NccDAO = new NhaCungCapDAO();
    private ArrayList<NhaCungCap> listNcc = new ArrayList<>();

    public NhaCungCapBUS() {
        this.listNcc = NccDAO.selectAll();
    }

    public ArrayList<NhaCungCap> getAll() {
        return this.listNcc;
    }

    public NhaCungCap getByIndex(int index) {
        return this.listNcc.get(index);
    }

    public boolean add(NhaCungCap ncc) {
        boolean check = NccDAO.insert(ncc) != 0;
        if (check) {
            this.listNcc.add(ncc);
        }
        return check;
    }

    public boolean delete(NhaCungCap ncc, int index) {
        boolean check = NccDAO.delete(Integer.toString(ncc.getMancc())) != 0;
        if (check) {
            this.listNcc.remove(index);
        }
        return check;
    }

    public boolean update(NhaCungCap ncc) {
        boolean check = NccDAO.update(ncc) != 0;
        if (check) {
            this.listNcc.set(getIndexByMaNCC(ncc.getMancc()), ncc);
        }
        return check;
    }

    public int getIndexByMaNCC(int mancc) {
        int i = 0;
        int vitri = -1;
        while (i < this.listNcc.size() && vitri == -1) {
            if (listNcc.get(i).getMancc() == mancc) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public ArrayList<NhaCungCap> search(String txt, String type) {
        ArrayList<NhaCungCap> result = new ArrayList<>();
        txt = txt.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (NhaCungCap i : listNcc) {
                    if (Integer.toString(i.getMancc()).contains(txt) || i.getTenncc().contains(txt) || i.getDiachi().contains(txt) || i.getEmail().contains(txt) || i.getSdt().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Mã nhà cung cấp" -> {
                for (NhaCungCap i : listNcc) {
                    if (Integer.toString(i.getMancc()).contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Tên nhà cung cấp" -> {
                for (NhaCungCap i : listNcc) {
                    if (i.getTenncc().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Địa chỉ" -> {
                for (NhaCungCap i : listNcc) {
                    if (i.getDiachi().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Số điện thoại" -> {
                for (NhaCungCap i : listNcc) {
                    if (i.getSdt().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Email" -> {
                for (NhaCungCap i : listNcc) {
                    if (i.getEmail().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    public void reloadFromDB() {
        this.listNcc = NccDAO.selectAll();
    }

    public String[] getArrTenNhaCungCap() {
        int size = listNcc.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listNcc.get(i).getTenncc();
        }
        return result;
    }

    public String getTenNhaCungCap(int mancc) {
        return this.listNcc.get(getIndexByMaNCC(mancc)).getTenncc();
    }

    public NhaCungCap findCT(ArrayList<NhaCungCap> ncc, String tenncc) {
        NhaCungCap p = null;
        int i = 0;
        while (i < ncc.size() && p == null) {
            if (ncc.get(i).getTenncc().equals(tenncc)) {
                p = ncc.get(i);
            } else {
                i++;
            }
        }
        return p;
    }
}
