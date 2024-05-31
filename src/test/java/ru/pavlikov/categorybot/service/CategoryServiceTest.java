package ru.pavlikov.categorybot.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pavlikov.categorybot.exception.CategoryException;
import ru.pavlikov.categorybot.exception.RootCategoryException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.model.entities.Category;
import ru.pavlikov.categorybot.repository.CategoryRepository;
import ru.pavlikov.categorybot.service.impl.CategoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.pavlikov.categorybot.constants.Constants.DESC;
import static ru.pavlikov.categorybot.constants.Constants.TRANS;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void saveRootCategoryWithExceptionTest() {

        Command command = new Command();
        command.setChatId(1L);
        command.setParentCategory("<root>");
        when(categoryRepository.existsByChatIdAndParentIsNull(1L)).thenReturn(true);


        assertThrows(RootCategoryException.class, () -> categoryService.saveRootCategory(command));
    }

    @Test
    void saveRootCategoryTest() {

        Command command = new Command();
        command.setChatId(1L);
        command.setParentCategory("<root>");
        when(categoryRepository.existsByChatIdAndParentIsNull(1L)).thenReturn(false);


        categoryService.saveRootCategory(command);


        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void saveChildrenCategoryWithExceptionTest() {

        Command command = new Command();
        command.setChatId(1L);
        command.setParentCategory("<parent>");
        command.setChildrenCategory("<children>");
        when(categoryRepository.findCategoryByNameAndChatId("parent", 1L)).thenReturn(Optional.empty());


        assertThrows(CategoryException.class, () -> categoryService.saveChildrenCategory(command));
    }

    @Test
    void saveChildrenCategory_shouldThrowCategoryException_whenChildrenCategoryAlreadyExists() {

        Command command = new Command();
        command.setChatId(1L);
        command.setParentCategory("<parent>");
        command.setChildrenCategory("<children>");
        when(categoryRepository.findCategoryByNameAndChatId("parent", 1L)).thenReturn(Optional.of(new Category()));
        when(categoryRepository.findCategoryByNameAndChatId("children", 1L)).thenReturn(Optional.of(new Category()));


        assertThrows(CategoryException.class, () -> categoryService.saveChildrenCategory(command));
    }

    @Test
    void saveChildrenCategoryTest() {

        Command command = new Command();
        command.setChatId(1L);
        command.setParentCategory("<parent>");
        command.setChildrenCategory("<children>");
        Category parent = new Category();
        parent.setId(1L);
        when(categoryRepository.findCategoryByNameAndChatId("parent", 1L)).thenReturn(Optional.of(parent));
        when(categoryRepository.findCategoryByNameAndChatId("children", 1L)).thenReturn(Optional.empty());


        categoryService.saveChildrenCategory(command);


        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void saveAllWithExceptionTest() {

        Command command = new Command();
        command.setChatId(1L);
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(categoryRepository.existsByChatIdAndParentIsNull(1L)).thenReturn(true);


        assertThrows(CategoryException.class, () -> categoryService.saveAll(command, categories));
    }

    @Test
    void saveAllTest() {

        Command command = new Command();
        command.setChatId(1L);
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(categoryRepository.existsByChatIdAndParentIsNull(1L)).thenReturn(false);


        categoryService.saveAll(command, categories);


        verify(categoryRepository, times(1)).saveAll(categories);
    }

    @Test
    void deleteAllWithExceptionTest() {

        Command command = new Command();
        command.setChatId(1L);
        when(categoryService.truthRootCategory(command.getChatId())).thenReturn(false);


        assertThrows(CategoryException.class, () -> categoryService.deleteAll(command));
    }

    @Test
    void deleteAllTest() {
        // Arrange
        Command command = new Command();
        command.setChatId(1L);
        Category parent=new Category(null, "Category 1", 1L);
        Category children=new Category(parent, "Category 2", 1L);
        when(categoryService.truthRootCategory(command.getChatId())).thenReturn(true);
        List<Category> categories = new ArrayList<>();
        categories.add(parent);
        categories.add(children);
        when(categoryService.findAllByChatId(command)).thenReturn(categories);


        categoryService.deleteAll(command);


       verify(categoryRepository, times(1)).deleteAll(categories);
    }

    @Test
    void removeByNameAndChatIdWithExceptionTest() {
        Command command = new Command();
        command.setParentCategory("<NonExistentCategory>");
        command.setChatId(1L);

        when(categoryRepository.findCategoryByNameAndChatId("NonExistentCategory", 1L)).thenReturn(Optional.empty());

        assertThrows(CategoryException.class, () -> categoryService.removeByNameAndChatId(command));
    }
    @Test
    void removeByNameAndChatIdTest() {
        Command command = new Command();
        command.setParentCategory("<Category>");
        command.setChatId(1L);

        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.findCategoryByNameAndChatId("Category", 1L)).thenReturn(Optional.of(category));

        categoryService.removeByNameAndChatId(command);

        verify(categoryRepository, times(1)).deleteAll(any());
        verify(categoryRepository, times(1)).deleteCategoryByNameAndChatId("Category", 1L);
    }



    @Test
    void treeToStringWithExceptionTest() {
        Command command = new Command();
        command.setChatId(1L);

        when(categoryRepository.findCategoryByChatIdAndParentIsNull(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryException.class, () -> categoryService.treeToString(command));
    }

    @Test
    void testTreeToStringTest() {
        Category parent=new Category(null, "Category 1", 1L);
        Category children1=new Category(parent, "Category 2", 1L);
        Category children2=new Category(parent, "Category 3", 1L);;


        Command command = new Command();
        command.setChatId(1L);
        when(categoryRepository.findCategoryByChatIdAndParentIsNull(command.getChatId()))
                .thenReturn(Optional.of(parent));
        when(categoryRepository.hasChildren(parent)).thenReturn(true);
        when(categoryRepository.findCategoriesByParentId(parent.getId()))
                .thenReturn(List.of( children1,  children2));


        String treeString = categoryService.treeToString(command);


        assertEquals(DESC+"Category 1" + TRANS + "\n" +
                DESC+DESC + "Category 2\n" +
                DESC+DESC + "Category 3\n", treeString);
    }
}
