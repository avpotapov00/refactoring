package ru.akirakozov.sd.refactoring.repository.impl;

import lombok.SneakyThrows;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl extends BaseRepository implements ProductRepository {

    private final ProductMapper productMapper = new ProductMapper();

    @Override
    @SneakyThrows
    public void save(Product product) {
        save("insert into PRODUCT (NAME, PRICE) VALUES (?, ?)", preparedStatement -> {
            preparedStatement.setString(1, product.name());
            preparedStatement.setLong(2, product.price());
        });
    }

    @Override
    public Optional<Product> findMaxByPrice() {
        return query("select * from PRODUCT order by PRICE desc limit 1", productMapper::map);
    }

    @Override
    public Optional<Product> findMinByPrice() {
        return query("select * from PRODUCT order by PRICE limit 1", productMapper::map);
    }

    @Override
    public long sumByPrice() {
        return query("select sum(PRICE) as SUM from PRODUCT", resultSet -> {
            resultSet.next();
            return resultSet.getLong("SUM");
        });
    }

    @Override
    public int countAll() {
        return query("select count(*) as COUNT from PRODUCT", resultSet -> {
            resultSet.next();
            return resultSet.getInt("COUNT");
        });
    }

    @Override
    public List<Product> findAll() {
        return query("SELECT * FROM PRODUCT", productMapper::mapAll);
    }
}
