package com.study.model;

import com.study.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private Connection connection;

    private PreparedStatement pstmt;

    private ResultSet rs;

    /**
     * 카테고리들을 조회하는 메서드
     *
     * @return the all categories
     */
    public List<CategoryBean> getAllCategories() throws Exception {
        List<CategoryBean> categories = new ArrayList<>();

        connection = ConnectionUtil.getConnection();

        try {
            String query = "SELECT * FROM category";

            pstmt = connection.prepareStatement(query);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                long categoryId = rs.getLong("categoryId");

                String categoryName = rs.getString("name");

                CategoryBean category = new CategoryBean(categoryId, categoryName);

                categories.add(category);
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

        return categories;
    }


    /**
     * 카테고리의 이름을 가져오는 메서드
     *
     * @param categoryId the category id
     * @return the category name
     */
    public String getCategoryName(Long categoryId) throws Exception {
        String categoryName = "";

        connection = ConnectionUtil.getConnection();

        try {
            String query = "SELECT name FROM category WHERE categoryId = ?";

            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, categoryId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                categoryName = rs.getString("name");
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

        return categoryName;
    }
}
