package com.study.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {
    private final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";

    private final String USER = "ebsoft";

    private final String PASS = "ebsoft";

    private Connection con;

    private PreparedStatement pstmt;

    private ResultSet rs;

    public void getCon() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BoardBean> getBoards(int startRow, int endRow) {
        List<BoardBean> list = new ArrayList<>();

        getCon();

        try {
            String sql = "SELECT * FROM board ORDER BY boardId DESC LIMIT ?, ?";

            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, startRow - 1);
            pstmt.setInt(2, endRow);

            rs = pstmt.executeQuery();

            while(rs.next()) {
                BoardBean boardBean = new BoardBean();

                boardBean.setBoardId(rs.getLong(1));
                boardBean.setWriter(rs.getString(2));
                boardBean.setPassword(rs.getString(3));
                boardBean.setTitle(rs.getString(4));
                boardBean.setContent(rs.getString(5));
                boardBean.setViews(rs.getString(6));
                boardBean.setCreatedAt(rs.getDate(7).toString());
                boardBean.setModifiedAt(rs.getDate(8).toString());
                boardBean.setCategoryId(rs.getLong(9));

                list.add(boardBean);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getBoardCount() {
        int count = 0;

        getCon();

        try {
            String sql = "SELECT COUNT(*) FROM board";

            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}
