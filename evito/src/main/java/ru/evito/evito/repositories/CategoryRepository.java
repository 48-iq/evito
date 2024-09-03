package ru.evito.evito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.evito.evito.models.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("select c from Category c where c.title in :titles")
    List<Category> findAllCategoriesByTitles(List<String> titles);
    boolean existsByTitle(String title);
    void deleteByTitle(String title);
    Optional<Category> findByTitle(String title);
    @Modifying
    @Query(nativeQuery = true,
            value = "delete from products_categories where category_id = ?1")
    void deleteCategoryRelationsById(Integer id);
}
