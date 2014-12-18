package io.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Connect database
 * @author xiaolei
 * @version 1.0
 */
public class ConnDB {
	public Connection conn=null;
	public Statement stmt=null;
	public ResultSet rs=null;
	public PreparedStatement ps;
	
	private static String dbClassName;
	private static String dbUrl;
	private static String dbUser;
	private static String dbPwd;
	
	/**
	 * Connect database
	 * @param cname ClassName of database driver, like com.mysql.jdbc.Driver
	 * @param url the database's URL, like jdbc:mysql://localhost:3306?DatabaseName=posts
	 * @param uname user name of database
	 * @param pwd password of database
	 */
	public ConnDB(String cname,String url,String uname,String pwd){
		dbClassName= cname;
		dbUrl = url;
		dbUser= uname;
		dbPwd= pwd;
	}
	
	/**
	 * build connection with Database
	 * @return a connected connector
	 */
	public static Connection getConnection(){
		Connection conn=null;
		try{
			Class.forName(dbClassName).newInstance();
			conn=DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(conn==null){
			System.err.println(dbClassName+"error");
		}
		return conn;
	}
	
	/**
	 * 
	 * @param sql read command SQL
	 * @return 
	 */
	public ResultSet executeQuery(String sql){
		try{
			conn=getConnection();
			stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=stmt.executeQuery(sql);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		return rs;
	}
	
	/**
	 * execute update SQL to database
	 * @param sql updating SQL
	 * @return an Integer intimation
	 */
	public int executeUpdate(String sql){
		int result=0;
		try{
			conn=getConnection();
			stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			result=stmt.executeUpdate(sql);
		}catch(SQLException e){
			result=0;
		}
		return result;
	}
	
	/**
	 * close database connection and release resource
	 */
	public void close(){
		try { 
			if (rs != null) { 
				rs.close(); 
			}
			if (stmt != null) { 
				stmt.close();
			}
			if (conn != null) { 
				conn.close(); 
			}
			if(ps!=null){
				ps.cancel();
				ps.close();
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}