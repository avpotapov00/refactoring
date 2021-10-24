package ru.akirakozov.sd.refactoring.view.impl;

import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.view.ProductsHtmlMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsHtmlMapperImpl implements ProductsHtmlMapper {

    private static final String HTML_TEMPLATE = """
            <html><body>
            %s
            </body></html>
            """;

    private static final String NEW_LINE = "\n";

    @Override
    public String toSingleProduct(String message, Product product) {
        String information = "<h1>" + message + "</h1>" + NEW_LINE +
                productToHtml(product) + NEW_LINE;
        return HTML_TEMPLATE.formatted(information);
    }

    @Override
    public String toHeaderMessage(String message) {
        String information = "<h1>" + message + "</h1>" + NEW_LINE;
        return HTML_TEMPLATE.formatted(information);
    }

    @Override
    public String toProductsList(List<Product> products) {
        var productsHtml = products.stream().map(product -> productToHtml(product) + NEW_LINE)
                .collect(Collectors.joining());
        return HTML_TEMPLATE.formatted(productsHtml);
    }

    @Override
    public String toMessage(String message) {
        String information = message + NEW_LINE;
        return HTML_TEMPLATE.formatted(information);
    }

    @Override
    public String toRawText(String message) {
        return message;
    }

    private String productToHtml(Product product) {
        return product.name() + "\t" + product.price() + "</br>";
    }

}
