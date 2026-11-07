package org.example.daos;

import org.example.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            products.add(mapRowToProduct(results));
        }
        return products;
    }

    public Product getProductById(long id) {
        Product product = null;
        String sql = "SELECT * FROM products WHERE product_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            product = mapRowToProduct(results);
        }
        return product;
    }

    public Product createProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, image_name) VALUES (?, ?, ?, ?) RETURNING product_id;";
        Long newId = jdbcTemplate.queryForObject(sql, Long.class, product.getName(), product.getDescription(), product.getPrice(), product.getImage_id());
        return getProductById(newId);
    }

    public Product updateProduct(long newId, Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ? WHERE product_id = ?;";
        jdbcTemplate.update(sql, product.getName(), product.getDescription(), product.getPrice(), product.getProductId());
        return getProductById(newId);
    }

    public int deleteProduct(long id) {
        String sql = "DELETE FROM products WHERE product_id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    private Product mapRowToProduct(SqlRowSet row) {
        Product product = new Product();
        product.setProductId(row.getLong("product_id"));
        product.setName(row.getString("name"));
        product.setDescription(row.getString("description"));
        product.setPrice(row.getBigDecimal("price"));
        return product;
    }
}
