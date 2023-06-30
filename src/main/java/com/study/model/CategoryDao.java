package com.study.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
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

    public List<CategoryBean> getAllCategories() throws SQLException {
        List<CategoryBean> categories = new ArrayList<>();

        getCon();

        try {
            String query = "SELECT * FROM category";

            pstmt = con.prepareStatement(query);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                long categoryId = rs.getLong("categoryId");

                String categoryName = rs.getString("name");

                CategoryBean category = new CategoryBean(categoryId, categoryName);

                categories.add(category);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    public String getCategoryName(Long categoryId) {
        String categoryName = "";

        getCon();

        try {
            String query = "SELECT name FROM category WHERE categoryId = ?";

            pstmt = con.prepareStatement(query);
            pstmt.setLong(1, categoryId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                categoryName = rs.getString("name");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoryName;
    }
}
