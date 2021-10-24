package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.config.DataSource;
import ru.akirakozov.sd.refactoring.repository.impl.ProductRepositoryImpl;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.view.impl.ProductsHtmlMapperImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        var jdbcUrl = "jdbc:sqlite:test.db";
        try (Connection c = DriverManager.getConnection(jdbcUrl)) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        var dataSource = new DataSource(jdbcUrl);
        var productRepository = new ProductRepositoryImpl(dataSource);
        var productsHtmlMapper = new ProductsHtmlMapperImpl();

        context.addServlet(new ServletHolder(new AddProductServlet(productRepository)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(productRepository, productsHtmlMapper)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(productRepository, productsHtmlMapper)),"/query");

        server.start();
        server.join();
    }
}
