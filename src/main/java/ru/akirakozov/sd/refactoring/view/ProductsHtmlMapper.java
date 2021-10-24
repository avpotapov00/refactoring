package ru.akirakozov.sd.refactoring.view;

import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;

public interface ProductsHtmlMapper {

    String toSingleProduct(String message, Product product);

    String toHeaderMessage(String message);

    String toMessage(String message);

    String toRawText(String message);

    String toProductsList(List<Product> products);

}
