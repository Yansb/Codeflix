package com.yansb.admin.api.infrastructure.genre;

import com.yansb.admin.api.MySQLGatewayTest;
import com.yansb.admin.api.infrastructure.category.CategoryMySQLGateway;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

  @Autowired
  private CategoryMySQLGateway categoryMySQLGateway;

  @Autowired
  private GenreMySQLGateway genreMySQLGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test

  public void testDependenciesInjected() {
    Assertions.assertNotNull(categoryMySQLGateway);
    Assertions.assertNotNull(genreRepository);
    Assertions.assertNotNull(genreMySQLGateway);
  }

}
