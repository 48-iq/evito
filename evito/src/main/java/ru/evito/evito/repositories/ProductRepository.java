package ru.evito.evito.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.evito.evito.models.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select p from Product p where p.title like :title")
    Page<Product> findAllProductsByTitle(String title, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.title IN :categories GROUP BY p.id HAVING COUNT(c) = :size")
    Page<Product> findAllProductsByCategory(List<String> categories, Integer size, Pageable pageable);

    @Query("select p from Product p join p.user u where u.username = :username")
    Page<Product> findAllProductsByUser(String username, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.title IN :categories GROUP BY p.id HAVING COUNT(c) = :size and p.title like :title")
    Page<Product> findAllProductsByCategoryAndTitle(String title, List<String> categories, Integer size, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from products_categories where product_id = ?1")
    void deleteProductRelationsById(Integer id);
}
