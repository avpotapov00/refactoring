package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.view.HtmlContentComposer;
import ru.akirakozov.sd.refactoring.view.ProductsHtmlMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    private final ProductRepository productRepository;

    private final ProductsHtmlMapper productsHtmlMapper;

    public GetProductsServlet(ProductRepository productRepository, ProductsHtmlMapper productsHtmlMapper) {
        this.productRepository = productRepository;
        this.productsHtmlMapper = productsHtmlMapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        var products = productRepository.findAll();
        HtmlContentComposer.composeResponse(response, productsHtmlMapper.toProductsList(products));
    }
}
