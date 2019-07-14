package com.waes.comparator.repository;

import com.waes.comparator.entity.Base64Entry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by volkangumus on 14.07.2019
 */
@Repository
public interface Base64DiffRepository extends CrudRepository<Base64Entry, Long> {
    @Override
    Optional<Base64Entry> findById(Long aLong);
}
