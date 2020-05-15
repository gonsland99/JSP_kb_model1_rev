package com.test.model1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.test.model1.vo.Bbs;

public class BbsDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public BbsDAO() {
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/bbs");
			conn = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void bdClose() {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getNext() {
		String sql = "select bbsId from bbs order by bbsId desc limit 1";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 1; // 아무것도 없으면 1을 리턴
	}

	public int write(String bbsTitle, String bbsContent, String userId) {
		String sql = "insert into bbs values(?, ?, ?, now(), ?, 1)";
		int bbsId = getNext();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbsId);
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userId);
			pstmt.setString(4, bbsContent);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public Vector<Bbs> getList(int pageNumber) {
		String sql = "SELECT * FROM bbs WHERE bbsId < ? AND bbsAvailable = 1 ORDER BY bbsId DESC LIMIT 10";
		Vector<Bbs> list = new Vector<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsId(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserId(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public int totalPage() {
		String sql = "select count(*) from bbs where bbsAvailable  = 1";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) / 10 + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 1;
	}
}
