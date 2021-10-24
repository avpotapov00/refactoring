package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.view.HtmlContentComposer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {

    private final ProductRepository productRepository;

    public AddProductServlet(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        var product = extractProduct(request);
        productRepository.save(product);
        HtmlContentComposer.composeResponse(response, "OK");
    }

    private Product extractProduct(HttpServletRequest request) {
        String name = getPresentParameter(request, "name");
        long price = Long.parseLong(getPresentParameter(request, "price"));

        return new Product(name, price);
    }

    private String getPresentParameter(HttpServletRequest request, String name) {
        var parameterValue = request.getParameter(name);
        if (parameterValue == null) {
            throw new IllegalArgumentException("Parameter \"" + name + "\" is absent");
        }
        return parameterValue;
    }
}
