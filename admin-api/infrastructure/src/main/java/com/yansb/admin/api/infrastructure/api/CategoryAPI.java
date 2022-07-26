package com.yansb.admin.api.infrastructure.api;

import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.infrastructure.category.models.CreateCategoryApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryAPI {

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(summary = "Create a new category")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Category created"),
      @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
      @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred")

  })
  ResponseEntity<?> createCategory(@RequestBody CreateCategoryApiInput input);

  @GetMapping
  @Operation(summary = "List all categories paginated")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categories found"),
      @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
      @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred")
  })
  Pagination<?> listCategories(
      @RequestParam(name = "search", required = false, defaultValue = "") final String search,
      @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
      @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
      @RequestParam(name = "sort", required = false, defaultValue = "name") final int sort,
      @RequestParam(name = "dir", required = false, defaultValue = "asc") final int dir

  );
}
