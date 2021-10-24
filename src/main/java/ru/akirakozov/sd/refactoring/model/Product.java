package ru.akirakozov.sd.refactoring.model;

import lombok.NonNull;

public record Product(@NonNull String name,
                      @NonNull long price) {
}
