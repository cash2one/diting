package com.diting.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liufei on 2016/7/13.
 */
public class DBUpdate {
    public static void update(String table, Long id) {
        Connection conn = DBHelper.getConn();
        int i = 0;
        String sql = "update " + table + " set frequency = frequency +" + 1 + " where id= " + id;
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("resutl: " + i);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAll(String table) {
        Connection conn = DBHelper.getConn();
        int i = 0;
        String sql = "update " + table + " set frequency =" + 1 + " WHERE frequency IS NULL ";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("resutl: " + i);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> customerName() throws Exception {
        List<String> list = new ArrayList<>();
        String sql = "SELECT c_user_name from tb_customer";
        Connection conn = DBHelper.getConn();
        if (conn == null) {
            throw new Exception("数据库连接失败！");
        }
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(rs.getString("c_user_name"));
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        List<String> list = customerName();
        for (int i = 0; i < list.size(); i++) {
            String c_user_name = list.get(i);
            updateAll("tb_" + c_user_name + "_knowledge");
            System.out.println(c_user_name + "    " + i);
        }
//        update("tb_liufei_knowledge", Long.valueOf(475));
    }
}
