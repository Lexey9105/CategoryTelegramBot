package ru.pavlikov.categorybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pavlikov.categorybot.model.entities.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findCategoryByNameAndChatId(String name, Long chatId);

    List<Category> findCategoryByChatId(long chatId);

    boolean existsByChatIdAndParentIsNull(Long chatId);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.parent = :category")
    boolean hasChildren(@Param("category") Category category);

    Optional<Category> findCategoryByChatIdAndParentIsNull(Long chatId);
    List<Category> findCategoriesByParentId(Long parentId);

    void deleteCategoryByNameAndChatId(String name, long chatId);

}
