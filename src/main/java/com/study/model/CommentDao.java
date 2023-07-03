package com.study.model;

import com.study.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    private Connection connection;

    private PreparedStatement pstmt;

    private ResultSet rs;

    /**
     * 댓글들을 조회하는 메서드
     *
     * @param boardId the board id
     * @return the comments
     */
    public List<CommentBean> getComments(String boardId) throws Exception {
        List<CommentBean> list = new ArrayList<>();

        connection = ConnectionUtil.getConnection();

        try {
            String query = "SELECT * FROM comment WHERE boardId = ? "
                    + "ORDER BY commentId DESC";

            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, Long.parseLong(boardId));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                CommentBean commentBean = new CommentBean();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

                commentBean.setCommentId(rs.getLong("commentId"));
                commentBean.setContent(rs.getString("contentId"));
                commentBean.setCreatedAt(dateFormat.format(rs.getTimestamp("createdAt")));
                commentBean.setBoardId(rs.getLong("boardId"));

                list.add(commentBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 댓글을 작성하여 db에 반영하는 메서드
     *
     * @param commentBean the comment bean
     */
    public void insertComment(CommentBean commentBean) throws Exception {
        connection = ConnectionUtil.getConnection();

        try {
            String query = "INSERT INTO comment (content, createdAt, boardId)  "
                    + "VALUES (?, CURRENT_TIMESTAMP, ?)";

            pstmt = connection.prepareStatement(query);
            pstmt.setString(1,commentBean.getContent());
            pstmt.setLong(2, commentBean.getBoardId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
