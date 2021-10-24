package ru.akirakozov.sd.refactoring.servlet;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.repository.impl.ProductRepositoryImpl;
import ru.akirakozov.sd.refactoring.view.impl.ProductsHtmlMapperImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.akirakozov.sd.refactoring.util.TestUtils.*;

class QueryServletTest {

    private final QueryServlet queryServlet = new QueryServlet(
            new ProductRepositoryImpl(),
            new ProductsHtmlMapperImpl()
    );

    @BeforeEach
    void createTableIfNotExists() {
        createTable();
    }

    @Test
    void shouldReturnMaxProduct() {
        var products = List.of(
                new Product("car", 100),
                new Product("table", 10)
        );
        fillTable(products);

        assertEquals("Product with max price: car 100", query("max"));
    }

    @Test
    void shouldReturnMinProduct() {
        var products = List.of(
                new Product("car", 100),
                new Product("table", 10)
        );
        fillTable(products);

        assertEquals("Product with min price: table 10", query("min"));
    }

    @Test
    void shouldReturnProductsCount() {
        var products = List.of(
                new Product("car", 100),
                new Product("table", 10)
        );
        fillTable(products);

        assertEquals("Number of products: 2", query("count"));
    }

    @Test
    void shouldReturnProductsSum() {
        var products = List.of(
                new Product("car", 100),
                new Product("table", 10)
        );
        fillTable(products);

        assertEquals("Summary price: 110", query("sum"));
    }

    @Test
    void shouldReturnZeroSum() {
        assertEquals("Summary price: 0", query("sum"));
    }

    @Test
    void shouldReturnZeroCount() {
        assertEquals("Number of products: 0", query("count"));
    }

    @Test
    void shouldReturnNothingOnMaxRequest() {
        assertEquals("Product with max price:", query("max"));
    }

    @Test
    void shouldReturnNothingOnMinRequest() {
        assertEquals("Product with min price:", query("min"));
    }

    @Test
    void shouldReturnUnknownCommand() {
        assertEquals("Unknown command: truncate", query("truncate"));
    }

    @SneakyThrows
    private String query(String command) {
        var request = requestWithCommand(command);
        var response = Mockito.mock(HttpServletResponse.class);

        var stringWriter = new StringWriter();
        var writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        queryServlet.doGet(request, response);

        return htmlBody(stringWriter.toString());
    }

    private HttpServletRequest requestWithCommand(String command) {
        var request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getParameter("command")).thenReturn(command);
        return request;
    }

    @AfterEach
    void clean() {
        cleanTable();
    }

}