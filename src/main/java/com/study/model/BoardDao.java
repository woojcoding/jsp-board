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

    public List<BoardBean> getBoards(String startDate,
                                     String endDate,
                                     String categoryId,
                                     String keyword,
                                     int startRow,
                                     int endRow
    ) {
        List<BoardBean> list = new ArrayList<>();

        getCon();

        try {
            StringBuilder sql = new StringBuilder(
                    "SELECT * FROM board " +
                            "WHERE 1=1");

            StringBuilder conditionSql = new StringBuilder();

            if (startDate != null && !startDate.isEmpty()) {
                conditionSql.append(" AND createdAt >= ?");
            }

            if (endDate != null && !endDate.isEmpty()) {
                conditionSql.append(" AND createdAt <= ?");
            }

            if (categoryId != null && !categoryId.equals("ALL") && !categoryId.isEmpty()) {
                conditionSql.append(" AND categoryId = ?");
            }

            if (keyword != null && !keyword.isEmpty()) {
                conditionSql.append(" AND (writer LIKE ? OR title LIKE ? OR content LIKE ?)");
            }

            sql.append(conditionSql);
            sql.append(" ORDER BY boardId DESC LIMIT ?, ?");

            pstmt = con.prepareStatement(sql.toString());

            int index = 1;

            if (startDate != null && !startDate.isEmpty()) {
                pstmt.setString(index++, startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
                pstmt.setString(index++, endDate);
            }

            if (categoryId != null
                    && !categoryId.equals("ALL")
                    && !categoryId.isEmpty()
            ) {
                pstmt.setString(index++, categoryId);
            }

            if (keyword != null && !keyword.isEmpty()) {
                pstmt.setString(index++, "%" + keyword + "%");
                pstmt.setString(index++, "%" + keyword + "%");
                pstmt.setString(index++, "%" + keyword + "%");
            }

            pstmt.setInt(index++, startRow - 1);
            pstmt.setInt(index, endRow);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                BoardBean boardBean = new BoardBean();

                boardBean.setBoardId(rs.getLong(1));
                boardBean.setWriter(rs.getString(2));
                boardBean.setPassword(rs.getString(3));
                boardBean.setTitle(rs.getString(4));
                boardBean.setContent(rs.getString(5));
                boardBean.setViews(rs.getString(6));
                boardBean.setCreatedAt(rs.getTimestamp(7).toString());
                boardBean.setModifiedAt(rs.getTimestamp(8).toString());
                boardBean.setCategoryId(rs.getLong(9));

                list.add(boardBean);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getBoardCount(String startDate,
                             String endDate,
                             String categoryId,
                             String keyword) {
        int count = 0;

        getCon();

        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM board WHERE 1=1");

            StringBuilder conditionSql = new StringBuilder();

            if (startDate != null && !startDate.isEmpty()) {
                conditionSql.append(" AND createdAt >= ?");
            }

            if (endDate != null && !endDate.isEmpty()) {
                conditionSql.append(" AND createdAt <= ?");
            }

            if (categoryId != null && !categoryId.equals("ALL") && !categoryId.isEmpty()) {
                conditionSql.append(" AND categoryId = ?");
            }

            if (keyword != null && !keyword.isEmpty()) {
                conditionSql.append(" AND (writer LIKE ? OR title LIKE ? OR content LIKE ?)");
            }

            sql.append(conditionSql);

            pstmt = con.prepareStatement(sql.toString());

            int index = 1;

            if (startDate != null && !startDate.isEmpty()) {
                pstmt.setString(index++, startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
                pstmt.setString(index++, endDate);
            }

            if (categoryId != null
                    && !categoryId.equals("ALL")
                    && !categoryId.isEmpty()
            ) {
                pstmt.setString(index++, categoryId);
            }

            if (keyword != null && !keyword.isEmpty()) {
                pstmt.setString(index++, "%" + keyword + "%");
                pstmt.setString(index++, "%" + keyword + "%");
                pstmt.setString(index++, "%" + keyword + "%");
            }

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
