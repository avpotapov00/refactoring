package ru.akirakozov.sd.refactoring.repository.impl;

import lombok.SneakyThrows;
import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductMapper {

    @SneakyThrows
    public Optional<Product> map(ResultSet resultSet) {
        if (resultSet.next()) {
            return Optional.of(mapOne(resultSet));
        }
        return Optional.empty();
    }

    @SneakyThrows
    public List<Product> mapAll(ResultSet resultSet) {
        var result = new ArrayList<Product>();
        while (resultSet.next()) {
            result.add(mapOne(resultSet));
        }
        return result;
    }

    @SneakyThrows
    private Product mapOne(ResultSet resultSet) {
        var name = resultSet.getString("NAME");
        var price = resultSet.getInt("PRICE");
        return new Product(name, price);
    }


}
