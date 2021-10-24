package ru.akirakozov.sd.refactoring.servlet;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.repository.impl.ProductRepositoryImpl;
import ru.akirakozov.sd.refactoring.util.TestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.akirakozov.sd.refactoring.util.TestUtils.cleanTable;

class AddProductServletTest {

    private final AddProductServlet addProductServlet = new AddProductServlet(new ProductRepositoryImpl());

    @BeforeEach
    void createTableIfNotExists() {
        TestUtils.createTable();
    }

    @Test
    @SneakyThrows
    void shouldPersistSingleCreatedProduct() {
        var product = new Product("car", 200);

        createProduct(product);

        assertEquals(List.of(product), TestUtils.getAllProducts());
    }

    @Test
    @SneakyThrows
    void shouldPersistAllCreatedProducts() {
        var products = List.of(
                new Product("car", 300),
                new Product("plane", 1000)
        );

        products.forEach(this::createProduct);

        assertEquals(products, TestUtils.getAllProducts());
    }

    @Test
    @SneakyThrows
    void shouldPersistAllCreatedProductsWithExisting() {
        var products = List.of(
                new Product("car", 300),
                new Product("plane", 1000)
        );

        var existedProducts = List.of(new Product("door", 50));
        TestUtils.fillTable(existedProducts);

        products.forEach(this::createProduct);

        var expectedProducts = Stream.of(existedProducts, products).flatMap(List::stream).toList();
        assertEquals(expectedProducts, TestUtils.getAllProducts());
    }

    @Test
    @SneakyThrows
    void shouldThrowOnNameParameterIsAbsent() {
        assertThrows(IllegalArgumentException.class, () -> {
            var request = Mockito.mock(HttpServletRequest.class);
            var response = Mockito.mock(HttpServletResponse.class);

            Mockito.when(request.getParameter("price")).thenReturn("10");

            addProductServlet.doGet(request, response);
        });
    }

    @Test
    @SneakyThrows
    void shouldThrowOnPriceParameterIsAbsent() {
        assertThrows(IllegalArgumentException.class, () -> {
            var request = Mockito.mock(HttpServletRequest.class);
            var response = Mockito.mock(HttpServletResponse.class);

            Mockito.when(request.getParameter("name")).thenReturn("car");

            addProductServlet.doGet(request, response);
        });
    }

    @Test
    @SneakyThrows
    void shouldThrowOnPriceParameterIsNotInteger() {
        assertThrows(NumberFormatException.class, () -> {
            var request = Mockito.mock(HttpServletRequest.class);
            var response = Mockito.mock(HttpServletResponse.class);

            Mockito.when(request.getParameter("name")).thenReturn("car");
            Mockito.when(request.getParameter("price")).thenReturn("abc");

            addProductServlet.doGet(request, response);
        });
    }

    @SneakyThrows
    private void createProduct(Product product) {
        var response = Mockito.mock(HttpServletResponse.class);

        var stringWriter = new StringWriter();
        var writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        addProductServlet.doGet(addProductRequest(product), response);

        assertEquals("OK", stringWriter.toString().trim());
    }

    private HttpServletRequest addProductRequest(Product product) {
        var request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getParameter("name")).thenReturn(product.name());
        Mockito.when(request.getParameter("price")).thenReturn(String.valueOf(product.price()));
        return request;
    }

    @AfterEach
    void clean() {
        cleanTable();
    }


}