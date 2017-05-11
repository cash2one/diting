package com.diting.util;

import java.sql.*;

public class DBHelper {

	public static final String url = Constants.ROBOT_DATABASE_URL;
	public static final String name = Constants.ROBOT_DATABASE_DRIVER;
	public static final String user = Constants.ROBOT_DATABASE_USER;
	public static final String password = Constants.ROBOT_DATABASE_PASSWORD;

	public Connection conn = null;
	public PreparedStatement pst = null;

	public DBHelper(String sql) {
		try {
			Class.forName(name);//指定连接类型
			conn = DriverManager.getConnection(url, user, password);//获取连接
			pst = conn.prepareStatement(sql);//准备执行语句
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConn(){
		String driver = Constants.ROBOT_DATABASE_DRIVER;
		String url = Constants.ROBOT_DATABASE_URL;
		String username = Constants.ROBOT_DATABASE_USER;
		String password = Constants.ROBOT_DATABASE_PASSWORD;
		Connection conn = null;
		try {
			Class.forName(driver); //classLoader,加载对应驱动
			conn = (Connection) DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(conn!=null) {
				// conn.close();
			}
		}
		return conn;
	}

	public void close() {
		try {
			this.conn.close();
			this.pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
