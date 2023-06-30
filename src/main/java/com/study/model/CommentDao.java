package com.study.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
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

    public List<CommentBean> getComments(String boardId) {
        ArrayList<CommentBean> list = new ArrayList<>();

        getCon();

        try {
            String query = "SELECT * FROM comment WHERE boardId = ? "
                    + "ORDER BY commentId DESC";

            pstmt = con.prepareStatement(query);
            pstmt.setLong(1, Long.parseLong(boardId));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                CommentBean commentBean = new CommentBean();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

                commentBean.setCommentId(rs.getLong(1));
                commentBean.setContent(rs.getString(2));
                commentBean.setCreatedAt(dateFormat.format(rs.getTimestamp(3)));
                commentBean.setBoardId(rs.getLong(4));

                list.add(commentBean);
            }

            rs.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insertComment(CommentBean commentBean) {
        getCon();

        try {
            String query = "INSERT INTO comment (content, createdAt, boardId)  "
                    + "VALUES (?, CURRENT_TIMESTAMP, ?)";

            pstmt = con.prepareStatement(query);
            pstmt.setString(1,commentBean.getContent());
            pstmt.setLong(2, commentBean.getBoardId());
            pstmt.executeUpdate();

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
