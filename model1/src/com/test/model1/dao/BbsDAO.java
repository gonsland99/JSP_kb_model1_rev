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
	private int widthBlock = 5;
	private int pageRows = 10;

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

	public Bbs getBbs(int bbsId){
		Bbs bbs = new Bbs();
		String sql = "select * from bbs where bbsId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbsId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				bbs.setBbsId(rs.getInt("bbsId"));
				bbs.setBbsTitle(rs.getString("bbsTitle"));
				bbs.setUserId(rs.getString("userId"));
				bbs.setBbsDate(rs.getString("bbsDate"));
				bbs.setBbsContent(rs.getString("bbsContent"));
				bbs.setBbsAvailable(rs.getInt("bbsAvailable"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bbs;
	}
	public int delete(int bbsId) {
		String sql = "update bbs set bbsAvailable=0 where bbsId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbsId);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}
	public int modify(String bbsTitle, String bbsContent, int bbsId) {
		String sql = "update bbs set bbsTitle=?, bbsContent=? where bbsId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsId);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	

	public int getWidthBlock() {	//한번에 보여지는 페이지 블록 수
		return widthBlock;
	}
	public int getPageRows() {		//한 페이지당 보여지는 글의 수
		return pageRows;
	}
	public int getViewList() {		//게시판에 있는 총 글의 수
		String sql="select count(*) from bbs where bbsAvailable = 1";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) return rs.getInt(1);
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	public int totalBlock() {		//전체 블록수
		if(getViewList() % (widthBlock * pageRows) > 0) {
			return getViewList() / (widthBlock * pageRows) +1;
		}
		return getViewList() / (widthBlock * pageRows);
	}
	public int currentBlock(int pageNumber) {	//현재 블록 수
		if(pageNumber % widthBlock > 0) {
			return pageNumber / widthBlock +1;
		}
		return pageNumber / widthBlock;
	}
	public int totalPage() {		// 전체 페이지 수 계산 메소드
		if(getViewList() % pageRows > 0) {
			return getViewList() / pageRows +1;
		}
		return getViewList() / pageRows;
	}
	public boolean nextPage(int pageNumber)	{	//리턴값의 true, flase를 변경
		String sql = "select * from bbs where bbsId < ? and bbsAvailable = 1";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				return false;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
