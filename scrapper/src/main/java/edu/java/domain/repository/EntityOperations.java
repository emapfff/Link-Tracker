package edu.java.domain.repository;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface EntityOperations<T> {
    void add(@NotNull T dto);

    int remove(@NotNull Integer id);

    List<T> findAll();
}
