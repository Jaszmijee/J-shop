package com.example.jshop.repository;

import com.example.jshop.domain.warehouse.Warehouse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface WarehouseRepository extends CrudRepository<Warehouse, Long> {

    @Override
    List<Warehouse> findAll();

    @Override
    Warehouse save(Warehouse warehouse);

    void deleteByProduct_ProductID(Long productId);

    @Query(value = "SELECT * from Warehouse w " +
            "JOIN products p on w.products_product_id = p.product_id " +
            "JOIN categories c ON p.categories_categoryID = c.categoryID " +
            "where " +
            "c.category like %:NAME% " +
            "ORDER BY p.price ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<Warehouse> findWarehouseByProduct_Category_Name(@Param("NAME") String nameFragment, Integer limit);

    @Query(value = "SELECT * from Warehouse " +
            "where products_product_id = :productId", nativeQuery = true)
    Optional<Warehouse> findWarehouseByProductId(@Param("productId") Long productId);

    @Query(value = "SELECT * from Warehouse w " +
            "JOIN products p on w.products_product_id = p.product_id " +
            "JOIN categories c ON p.categories_categoryID = c.categoryID " +
            "where (:CATEGORY_NAME IS NULL OR c.category LIKE %:CATEGORY_NAME%) " +
            "AND (:PRODUCT_NAME IS NULL OR p.product_name LIKE %:PRODUCT_NAME%) " +
            "AND (:PRICE IS NULL OR p.price <=:PRICE) " +
            "ORDER BY p.price ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<Warehouse> findWarehouseByProduct_CategoryOrProduct_ProductNameOAndProduct_Price(@Param("CATEGORY_NAME") String categoryName, @Param("PRODUCT_NAME") String productName,
                                                                                          @Param("PRICE") BigDecimal price, Integer limit);
}






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


/*    @Override
    public List<Passenger> findOrderedBySeatNumberLimitedTo(int limit) {
        return entityManager.createQuery("SELECT p FROM Passenger p ORDER BY p.seatNumber",
                Passenger.class).setMaxResults(limit).getResultList();*/


