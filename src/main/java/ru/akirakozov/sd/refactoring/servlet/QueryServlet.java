package ru.akirakozov.sd.refactoring.servlet;

import lombok.SneakyThrows;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.view.HtmlContentComposer;
import ru.akirakozov.sd.refactoring.view.ProductsHtmlMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static ru.akirakozov.sd.refactoring.view.HtmlContentComposer.composeResponse;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private final ProductRepository productRepository;

    private final ProductsHtmlMapper productsHtmlMapper;

    public QueryServlet(ProductRepository productRepository, ProductsHtmlMapper productsHtmlMapper) {
        this.productRepository = productRepository;
        this.productsHtmlMapper = productsHtmlMapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var command = request.getParameter("command");

        switch (command) {
            case "max" -> queryMax(response);
            case "min" -> queryMin(response);
            case "count" -> queryCount(response);
            case "sum" -> querySum(response);
            default ->  response.getWriter().println("Unknown command: " + command);
        }
    }


    @SneakyThrows
    private void queryMax(HttpServletResponse response) {
        var message = "Product with max price: ";
        var content = productRepository.findMaxByPrice()
                .map(product -> productsHtmlMapper.toSingleProduct(message, product))
                .orElse(productsHtmlMapper.toHeaderMessage(message));
        composeResponse(response, content);
    }

    @SneakyThrows
    private void queryMin(HttpServletResponse response) {
        var message = "Product with min price: ";
        var content = productRepository.findMinByPrice()
                .map(product -> productsHtmlMapper.toSingleProduct(message, product))
                .orElse(productsHtmlMapper.toHeaderMessage(message));
        composeResponse(response, content);
    }

    @SneakyThrows
    private void querySum(HttpServletResponse response) {
        var message = "Summary price: " + productRepository.sumByPrice();
        var content = productsHtmlMapper.toMessage(message);
        composeResponse(response, content);
    }

    @SneakyThrows
    private void queryCount(HttpServletResponse response) {
        var message = "Number of products: " + productRepository.countAll();
        var content = productsHtmlMapper.toMessage(message);
        composeResponse(response, content);
    }

}
