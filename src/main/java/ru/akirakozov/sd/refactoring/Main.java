package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.datasource.DataSource;
import ru.akirakozov.sd.refactoring.repository.impl.ProductRepositoryImpl;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.view.impl.ProductsHtmlMapperImpl;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("First argument must be valid jdbc url");
            System.exit(1);
        }

        var dataSource = new DataSource(args[0]);
        var productRepository = new ProductRepositoryImpl(dataSource);
        var productsHtmlMapper = new ProductsHtmlMapperImpl();
        productRepository.createTable();

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(productRepository)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(productRepository, productsHtmlMapper)), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(productRepository, productsHtmlMapper)), "/query");

        server.start();
        server.join();
    }
}
