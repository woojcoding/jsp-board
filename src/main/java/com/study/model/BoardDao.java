package com.study.model;

import com.study.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {
    private Connection connection;

    private PreparedStatement pstmt;

    private ResultSet rs;

    /**
     * 게시글 조회에서 게시글 정보들을 가져오는 메서드
     *
     * @param startDate  날짜 범위 - 시작일
     * @param endDate    날짜 범위 - 종료일
     * @param categoryId the category id
     * @param keyword    the keyword
     * @param startRow   페이지 시작 번호
     * @param endRow     페이지 끝 번호
     * @return List<BoardBean> boards
     */
    public List<BoardBean> getBoards(String startDate,
                                     String endDate,
                                     String categoryId,
                                     String keyword,
                                     int startRow,
                                     int endRow
    ) throws Exception {
        List<BoardBean> list = new ArrayList<>();

        connection = ConnectionUtil.getConnection();

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

            pstmt = connection.prepareStatement(sql.toString());

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

                Timestamp modifiedTimestamp = rs.getTimestamp(9);

                if (modifiedTimestamp != null) {
                    boardBean.setModifiedAt(dateFormat.format(modifiedTimestamp));
                }

                list.add(boardBean);
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
     * 게시글의 조회수를 조회하는 메서드
     *
     * @param startDate  시작일
     * @param endDate    종료일
     * @param categoryId 카테고리 아이디
     * @param keyword    검색어 키워드(제목 + 내용 + 작성자)
     * @return the board count
     */
    public int getBoardCount(String startDate,
                             String endDate,
                             String categoryId,
                             String keyword) throws Exception {
        int count = 0;

        connection = ConnectionUtil.getConnection();

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

            pstmt = connection.prepareStatement(sql.toString());

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

        return count;
    }

    /**
     * 게시글을 작성하여 db에 반영하는 메서드
     *
     * @param boardBean the board bean
     */
    public void insertBoard(BoardBean boardBean) throws Exception {
        connection = ConnectionUtil.getConnection();

        try {
            String query = "INSERT INTO board (writer, password, title, content, isAttached, views, createdAt, modifiedAt, categoryId) " +
                    "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, NULL, ?)";

            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, boardBean.getWriter());
            pstmt.setString(2, boardBean.getPassword());
            pstmt.setString(3, boardBean.getTitle());
            pstmt.setString(4, boardBean.getContent());
            pstmt.setBoolean(5, boardBean.isAttached());
            pstmt.setInt(6, 0);
            pstmt.setLong(7, boardBean.getCategoryId());
            pstmt.executeUpdate();

            connection.close();

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

    /**
     * 게시글 정보를 조회하며 조회수를 1 올리는 메서드
     *
     * @param boardId the board id
     * @return the one board
     */
    public BoardBean getOneBoard(String boardId) throws Exception {
        BoardBean boardBean = null;

        connection = ConnectionUtil.getConnection();

        try {
            String readSql = "UPDATE board SET views = views+1 where boardId =?";

            pstmt = connection.prepareStatement(readSql);
            pstmt.setLong(1, Long.parseLong(boardId));

            pstmt.executeUpdate();

            String query = "SELECT * FROM board WHERE boardId = ?";

            pstmt = connection.prepareStatement(query);
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

                Timestamp modifiedTimestamp = rs.getTimestamp(9);

                if (modifiedTimestamp != null) {
                    boardBean.setModifiedAt(dateFormat.format(modifiedTimestamp));
                }
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

        return boardBean;
    }

    /**
     * 수정을 하기위해 게시글 정보를 조회하는 메서드
     *
     * @param boardId the board id
     * @return the board info
     */
    public BoardBean getBoardInfo(String boardId) throws Exception {
        BoardBean boardBean = null;

        connection = ConnectionUtil.getConnection();

        try {
            String query = "SELECT * FROM board WHERE boardId = ?";

            pstmt = connection.prepareStatement(query);
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
        return boardBean;
    }

    /**
     * 게시글을 수정하는 메서드
     *
     * @param boardBean the board bean
     */
    public void updateBoard(BoardBean boardBean) throws Exception {
        connection = ConnectionUtil.getConnection();

        try {
            String query = "UPDATE board "
                    + "SET writer = ?, "
                    + "title = ?, "
                    + "content = ?, "
                    + "modifiedAt = CURRENT_TIMESTAMP"
                    + " WHERE boardId = ?";

            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, boardBean.getWriter());
            pstmt.setString(2, boardBean.getTitle());
            pstmt.setString(3, boardBean.getContent());
            pstmt.setLong(4,boardBean.getBoardId());
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

    /**
     * 비밀번호를 검증하는 메서드
     *
     * @param boardBean the board bean
     * @return the boolean 검증 통과 여부
     */
    public boolean validatePassword(BoardBean boardBean) throws Exception {
        connection = ConnectionUtil.getConnection();

        boolean isValidated = false;

        try {
            String query = "SELECT password FROM board WHERE boardId=?";

            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, boardBean.getBoardId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString(1);

                String password = boardBean.getPassword();

                if (dbPassword.equals(password)) {
                    isValidated = true;
                }
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

        return isValidated;
    }

    public void deleteBoard(String boardId) {
        getCon();

        try {
            String query = "DELETE FROM board WHERE boardId = ?";

            pstmt = con.prepareStatement(query);
            pstmt.setLong(1,Long.parseLong(boardId));
            pstmt.executeUpdate();

            con.close();
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
