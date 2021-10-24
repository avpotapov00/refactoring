package ru.akirakozov.sd.refactoring.repository;

import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    void createTable();

    void save(Product product);

    Optional<Product> findMaxByPrice();

    Optional<Product> findMinByPrice();

    long sumByPrice();

    int countAll();

    List<Product> findAll();

}
