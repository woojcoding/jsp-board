package com.study.model;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FileDao {
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

    public long uploadFile(InputStream fileStream, String fileName, long boardId) {

        getCon();

        long fileId = -1;

        try {
            getCon();

            // 파일 정보를 file 테이블에 저장
            String insertQuery = "INSERT INTO file (name, boardId, file) VALUES (?, ?, ?)";

            pstmt = con.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, fileName);
            pstmt.setLong(2, boardId);
            pstmt.setBinaryStream(3, fileStream);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // 저장된 파일의 ID를 가져옴
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    fileId = rs.getLong(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileId;
    }
}
