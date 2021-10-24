package ru.akirakozov.sd.refactoring.view.impl;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.view.ProductsHtmlMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductsHtmlMapperImplTest {

    private final ProductsHtmlMapper productsHtmlMapper = new ProductsHtmlMapperImpl();

    @Test
    void shouldMapProduct() {
        var expected = """
                <html><body>
                <h1>Data: </h1>
                car	100</br>
                                
                </body></html>
                """;

        var product = new Product("car", 100);

        assertEquals(expected, productsHtmlMapper.toSingleProduct("Data: ", product));
    }


    @Test
    void shouldMapAllProducts() {
        var expected = """
                <html><body>
                car	100</br>
                table	10</br>
                                
                </body></html>
                """;

        var products = List.of(
                new Product("car", 100),
                new Product("table", 10)
        );

        assertEquals(expected, productsHtmlMapper.toProductsList(products));
    }

    @Test
    void shouldMapMessage() {
        var expected = """
                <html><body>
                Message
                                
                </body></html>
                """;

        assertEquals(expected, productsHtmlMapper.toMessage("Message"));
    }

    @Test
    void shouldMapHeaderMessage() {
        var expected = """
                <html><body>
                <h1>Message</h1>
                                
                </body></html>
                """;

        assertEquals(expected, productsHtmlMapper.toHeaderMessage("Message"));
    }

    @Test
    void shouldMapRawText() {
        assertEquals("OK", productsHtmlMapper.toRawText("OK"));
    }
}