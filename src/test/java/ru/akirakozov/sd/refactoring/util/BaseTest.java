package ru.akirakozov.sd.refactoring.util;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import ru.akirakozov.sd.refactoring.config.DataSource;
import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseTest {

    protected final DataSource dataSource;

    public BaseTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @SneakyThrows
    public void createTable() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    @SneakyThrows
    public void fillTable(List<Product> products) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            for (var product : products) {
                String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"" + product.name() + "\"," + product.price() + ")";
                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        }
    }

    @SneakyThrows
    public void cleanTable() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DELETE from PRODUCT";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public String htmlBody(String html) {
        return Jsoup.parse(html).body().text();
    }

    public List<Product> parseProductsFromHtml(String resultHtml) {
        var result = new ArrayList<Product>();
        var data = Jsoup.parse(resultHtml).body().text().split(" ");

        for (int i = 0; i < data.length / 2; i++) {
            var name = data[i * 2];
            var price = Integer.parseInt(data[i * 2 + 1]);
            result.add(new Product(name, price));
        }

        return result;
    }

    public void assertProductsInHtml(String resultHtml, List<Product> expected) {
        var actual = parseProductsFromHtml(resultHtml);
        assertEquals(expected, actual);
    }

    @SneakyThrows
    public List<Product> getAllProducts() {
        var result = new ArrayList<Product>();
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");

            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                result.add(new Product(name, price));
            }

            rs.close();
            stmt.close();
        }
        return result;
    }

}
