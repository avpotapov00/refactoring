package ru.akirakozov.sd.refactoring.servlet;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.datasource.DataSource;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.repository.impl.ProductRepositoryImpl;
import ru.akirakozov.sd.refactoring.util.BaseTest;
import ru.akirakozov.sd.refactoring.view.impl.ProductsHtmlMapperImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetProductsServletTest extends BaseTest {

    private final GetProductsServlet getProductsServlet;

    public GetProductsServletTest() {
        super(new DataSource("jdbc:sqlite:test.db"));
        getProductsServlet = new GetProductsServlet(
                new ProductRepositoryImpl(dataSource),
                new ProductsHtmlMapperImpl());
    }

    @BeforeEach
    void createTableIfNotExists() {
        createTable();
    }

    @SneakyThrows
    @Test
    void shouldReturnProducts() {
        var products = List.of(
                new Product("car", 100),
                new Product("table", 10)
        );
        fillTable(products);

        var request = Mockito.mock(HttpServletRequest.class);
        var response = Mockito.mock(HttpServletResponse.class);

        var stringWriter = new StringWriter();
        var writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        getProductsServlet.doGet(request, response);

        assertProductsInHtml(stringWriter.toString(), products);
    }

    @Test
    @SneakyThrows
    void shouldReturnEmptyList() {
        var request = Mockito.mock(HttpServletRequest.class);
        var response = Mockito.mock(HttpServletResponse.class);

        var stringWriter = new StringWriter();
        var writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        getProductsServlet.doGet(request, response);
        assertEquals("", Jsoup.parse(stringWriter.toString()).body().text().trim());
    }

    @AfterEach
    void clean() {
        cleanTable();
    }


}