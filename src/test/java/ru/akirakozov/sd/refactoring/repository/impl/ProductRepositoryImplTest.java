package ru.akirakozov.sd.refactoring.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.util.TestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.akirakozov.sd.refactoring.util.TestUtils.cleanTable;
import static ru.akirakozov.sd.refactoring.util.TestUtils.fillTable;

class ProductRepositoryImplTest {

    private final ProductRepositoryImpl repository = new ProductRepositoryImpl();

    @BeforeEach
    void createTableIfNotExists() {
        TestUtils.createTable();
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

        assertEquals(List.of(product), TestUtils.getAllProducts());
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
        fillTable( List.of(
                new Product("car", 100),
                new Product("table", 10)
        ));

        assertEquals(2, repository.countAll());
    }

    @Test
    void shouldReturnSumOfProduct() {
        fillTable( List.of(
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