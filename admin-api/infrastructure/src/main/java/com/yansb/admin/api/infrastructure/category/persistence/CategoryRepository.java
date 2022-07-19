package com.yansb.admin.api.infrastructure.category.persistence;

import com.yansb.admin.api.infrastructure.category.persistence.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, String> {

}
