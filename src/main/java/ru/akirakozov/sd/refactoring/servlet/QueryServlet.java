package ru.akirakozov.sd.refactoring.servlet;

import lombok.SneakyThrows;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private final ProductRepository productRepository;

    public QueryServlet(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        var responseWriter = response.getWriter();
        switch (command) {
            case "max" -> queryMax(responseWriter);
            case "min" -> queryMin(responseWriter);
            case "count" -> queryCount(responseWriter);
            case "sum" -> querySum(responseWriter);
            default -> responseWriter.println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }


    @SneakyThrows
    private void queryMax(PrintWriter responseWriter) {
        responseWriter.println("<html><body>");
        responseWriter.println("<h1>Product with max price: </h1>");
        productRepository.findMaxByPrice().ifPresent(product -> {
            responseWriter.println(product.name() + "\t" + product.price() + "</br>");
        });
        responseWriter.println("</body></html>");
    }

    @SneakyThrows
    private void queryMin(PrintWriter responseWriter) {
        responseWriter.println("<html><body>");
        responseWriter.println("<h1>Product with min price: </h1>");
        productRepository.findMinByPrice().ifPresent(product -> {
            responseWriter.println(product.name() + "\t" + product.price() + "</br>");
        });
        responseWriter.println("</body></html>");
    }

    @SneakyThrows
    private void querySum(PrintWriter responseWriter) {
        responseWriter.println("<html><body>");
        responseWriter.println("Summary price: ");
        var sum = productRepository.sumByPrice();
        responseWriter.println(sum);
        responseWriter.println("</body></html>");
    }

    @SneakyThrows
    private void queryCount(PrintWriter responseWriter) {
        responseWriter.println("<html><body>");
        responseWriter.println("Number of products: ");
        var sum = productRepository.countAll();
        responseWriter.println(sum);
        responseWriter.println("</body></html>");
    }

}
