package ru.akirakozov.sd.refactoring.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.datasource.DataSource;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.util.BaseTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductRepositoryImplTest extends BaseTest {

    private final ProductRepositoryImpl repository;

    public ProductRepositoryImplTest() {
        super(new DataSource("jdbc:sqlite:test.db"));
        repository = new ProductRepositoryImpl(dataSource);
    }

    @BeforeEach
    void createTableIfNotExists() {
        createTable();
    }

    @Test
    void shouldReturnAllProducts() {
        var products = List.of(
                new Product("car", 100),
                new Product("table", 10)
        );

        fillTable(products);
        var actual = repository.findAll();

        assertEquals(products, actual);
    }

    @Test
    void shouldReturnAllProductsEmpty() {
        assertEquals(List.of(), repository.findAll());
    }

    @Test
    void shouldSaveProduct() {
        var product = new Product("car", 200);

        repository.save(product);

        assertEquals(List.of(product), getAllProducts());
    }

    @Test
    void shouldReturnMaxProduct() {
        var products = List.of(
                new Product("car", 100),
                new Product("table", 10)
        );
        fillTable(products);

        assertEquals(products.get(0), repository.findMaxByPrice().orElseThrow());
    }

    @Test
    void shouldReturnMinProduct() {
        var products = List.of(
                new Product("car", 100),
                new Product("table", 10)
        );
        fillTable(products);

        assertEquals(products.get(1), repository.findMinByPrice().orElseThrow());
    }

    @Test
    void shouldReturnCountOfProduct() {
        fillTable(List.of(
                new Product("car", 100),
                new Product("table", 10)
        ));

        assertEquals(2, repository.countAll());
    }

    @Test
    void shouldReturnSumOfProduct() {
        fillTable(List.of(
                new Product("car", 100),
                new Product("table", 10)
        ));

        assertEquals(110, repository.sumByPrice());
    }

    @AfterEach
    void clean() {
        cleanTable();
    }

}