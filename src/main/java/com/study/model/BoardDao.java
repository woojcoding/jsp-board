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
     * @param boardSearchCondition 검색 조건
     * @param startRow   페이지 시작 번호
     * @param endRow     페이지 끝 번호
     * @return List<BoardBean> boards
     */
    public List<BoardBean> getBoards(BoardSearchCondition boardSearchCondition,
                                     int startRow,
                                     int endRow
    ) throws Exception {
        List<BoardBean> list = new ArrayList<>();

        connection = ConnectionUtil.getConnection();

        try {
            StringBuilder sql = new StringBuilder(
                    "SELECT * FROM board " +
                            "WHERE 1=1");

            sql = conditionSqlBuild(boardSearchCondition, sql);

            sql.append(" ORDER BY boardId DESC LIMIT ?, ?");

            pstmt = connection.prepareStatement(sql.toString());

            int index = setPreparedStatementConditions(boardSearchCondition);

            pstmt.setInt(index++, startRow - 1);
            pstmt.setInt(index, endRow);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                BoardBean boardBean = new BoardBean();

                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("yyyy.MM.dd HH:mm");

                boardBean.setBoardId(rs.getLong("boardId"));
                boardBean.setWriter(rs.getString("writer"));
                boardBean.setPassword(rs.getString("password"));
                boardBean.setTitle(rs.getString("title"));
                boardBean.setContent(rs.getString("content"));
                boardBean.setAttached(rs.getBoolean("isAttached"));
                boardBean.setViews(rs.getString("views"));
                boardBean.setCreatedAt(dateFormat.format(rs.getTimestamp("createdAt")));
                boardBean.setCategoryId(rs.getLong("categoryId"));

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
     * @param boardSearchCondition 검색조건
     * @return the board count
     */
    public int getBoardCount(BoardSearchCondition boardSearchCondition) throws Exception {
        int count = 0;

        connection = ConnectionUtil.getConnection();

        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM board WHERE 1=1");

            sql = conditionSqlBuild(boardSearchCondition, sql);

            pstmt = connection.prepareStatement(sql.toString());

            setPreparedStatementConditions(boardSearchCondition);

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
     * 검색 조건에 따라 sql을 build 해주는 메서드
     *
     * @param boardSearchCondition 검색조건
     * @return sql sql을 반환해준다
     */
    private StringBuilder conditionSqlBuild(BoardSearchCondition boardSearchCondition, StringBuilder sql) {
        StringBuilder conditionSql = new StringBuilder();

        String startDate = boardSearchCondition.getStartDate();

        String endDate = boardSearchCondition.getEndDate();

        String categoryId = boardSearchCondition.getCategoryId();

        String keyword = boardSearchCondition.getKeyword();

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

        return sql;
    }

    /**
     * 조건에 따라 preparedStatement에 값을 지정해주는 메서드
     *
     * @param boardSearchCondition 검색조건
     * @return index
     */
    private int setPreparedStatementConditions(BoardSearchCondition boardSearchCondition) throws SQLException {
        int index = 1;

        String startDate = boardSearchCondition.getStartDate();

        String endDate = boardSearchCondition.getEndDate();

        String categoryId = boardSearchCondition.getCategoryId();

        String keyword = boardSearchCondition.getKeyword();

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
        return index;
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
     * 게시글의 정보를 조회하며 조회 시 조회수를 1 올리는 메서드
     * 수정을 위한 조회 시 조회수는 올리지 않는다.
     *
     * @param boardId the board id
     * @param updateViews 조회수 수정 여부
     * @return the one board
     */
    public BoardBean getOneBoard(String boardId, boolean updateViews) throws Exception {
        BoardBean boardBean = null;

        connection = ConnectionUtil.getConnection();

        try {
            if (updateViews) {
                String readSql = "UPDATE board SET views = views + 1 WHERE boardId = ?";

                pstmt = connection.prepareStatement(readSql);
                pstmt.setLong(1, Long.parseLong(boardId));

                pstmt.executeUpdate();
            }

            String query = "SELECT * FROM board WHERE boardId = ?";

            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, Long.parseLong(boardId));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                boardBean = new BoardBean();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

                boardBean.setBoardId(rs.getLong("boardId"));
                boardBean.setWriter(rs.getString("writer"));
                boardBean.setPassword(rs.getString("password"));
                boardBean.setTitle(rs.getString("title"));
                boardBean.setContent(rs.getString("content"));
                boardBean.setAttached(rs.getBoolean("isAttached"));
                boardBean.setViews(rs.getString("views"));
                boardBean.setCreatedAt(dateFormat.format(rs.getTimestamp("createdAt")));
                boardBean.setCategoryId(rs.getLong("categoryId"));

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
