package com.study.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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

                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("yyyy.MM.dd HH:mm");

                boardBean.setBoardId(rs.getLong(1));
                boardBean.setWriter(rs.getString(2));
                boardBean.setPassword(rs.getString(3));
                boardBean.setTitle(rs.getString(4));
                boardBean.setContent(rs.getString(5));
                boardBean.setAttached(rs.getBoolean(6));
                boardBean.setViews(rs.getString(7));
                boardBean.setCreatedAt(dateFormat.format(rs.getTimestamp(8)));
                boardBean.setCategoryId(rs.getLong(10));

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

    public void insertBoard(BoardBean boardBean) {
        getCon();

        try {

            String query = "INSERT INTO board (writer, password, title, content, isAttached, views, createdAt, modifiedAt, categoryId) " +
                    "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, NULL, ?)";

            pstmt = con.prepareStatement(query);
            pstmt.setString(1, boardBean.getWriter());
            pstmt.setString(2, boardBean.getPassword());
            pstmt.setString(3, boardBean.getTitle());
            pstmt.setString(4, boardBean.getContent());
            pstmt.setBoolean(5, boardBean.isAttached());
            pstmt.setInt(6, 0);
            pstmt.setLong(7, boardBean.getCategoryId());
            pstmt.executeUpdate();

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BoardBean getOneBoard(String boardId) {
        BoardBean boardBean = null;

        getCon();

        try {

            String readSql = "UPDATE board SET views = views+1 where boardId =?";

            pstmt = con.prepareStatement(readSql);
            pstmt.setLong(1, Long.parseLong(boardId));

            pstmt.executeUpdate();

            String query = "SELECT * FROM board WHERE boardId = ?";

            pstmt = con.prepareStatement(query);
            pstmt.setLong(1, Long.parseLong(boardId));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                boardBean = new BoardBean();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

                boardBean.setBoardId(rs.getLong(1));
                boardBean.setWriter(rs.getString(2));
                boardBean.setPassword(rs.getString(3));
                boardBean.setTitle(rs.getString(4));
                boardBean.setContent(rs.getString(5));
                boardBean.setAttached(rs.getBoolean(6));
                boardBean.setViews(rs.getString(7));
                boardBean.setCreatedAt(dateFormat.format(rs.getTimestamp(8)));
                boardBean.setCategoryId(rs.getLong(10));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return boardBean;
    }
}
