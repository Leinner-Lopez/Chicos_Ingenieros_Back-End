package com.chicos_ingenieros.zenkai.Categories.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringCategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
