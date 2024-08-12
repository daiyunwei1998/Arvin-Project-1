package com.stylish.stylish.repository;

import com.stylish.stylish.model.ColorORM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<ColorORM, Integer> {
}
