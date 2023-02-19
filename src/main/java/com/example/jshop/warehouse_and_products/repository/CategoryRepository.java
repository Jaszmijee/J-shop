package com.example.jshop.warehouse_and_products.repository;

import com.example.jshop.warehouse_and_products.domain.category.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends CrudRepository<Category, Long> {

    @Override
    List<Category> findAll();

     /* @Query(value = "SELECT * FROM CATEGORIES" +
            "WHERE Category_name = :NAME", nativeQuery = true)
    Category showAllCategoriesAndProducts(@Param("NAME") String name);*/
/*
    @Query(value = "SELECT * FROM COMPANIES " +
            "WHERE COMPANY_NAME LIKE %:NAME%", nativeQuery = true)
    List<Company> retrieveNameContaining(@Param("NAME") String nameFragment);

    @NamedQuery(
        name = "Employee.retrieveLastName",
        query = "FROM Employee WHERE lastname = :LASTNAME"
)
*/

   /* @Query(value = "SELECT C.* FROM COPIES C " +
            "JOIN BOOKS B ON B.BOOK_ID = C.BOOK_ID " +
            "WHERE B.BOOK_TITLE = :TITLE AND C.COPY_STATUS = 'AVAILABLE'", nativeQuery = true)
                List<Copy> findCopiesAvailableByTitle(@Param("TITLE") String title);*/

  /* SELECT I1.ID AS "TASK_ID", I1.SUMMARY, I2.NAME as "TASK_NAME"
    FROM ISSUES I1 JOIN ISSUESLISTS I2 ON I1.ISSUESLIST_ID = I2.ID;*/

/*  @NamedNativeQuery(
        name = "Company.retrieveNameStartingWith",
        query = "SELECT * FROM " +
                "COMPANIES WHERE " +
                "SUBSTRING(COMPANY_NAME, 1,3) = :COMPANY_SHORT_NAME",
        resultClass = Company.class

  @Query(value = "SELECT * FROM COMPANIES " +
            "WHERE COMPANY_NAME LIKE %:NAME%", nativeQuery = true)
    List<Company> retrieveNameContaining(@Param("NAME") String nameFragment);
 */


    Category findByCategoryID(Long categoryId);

    Category findByNameEqualsIgnoreCase(String name);

    @Override
    Category save(Category category);

    void deleteByNameEqualsIgnoreCase(String name);
}
