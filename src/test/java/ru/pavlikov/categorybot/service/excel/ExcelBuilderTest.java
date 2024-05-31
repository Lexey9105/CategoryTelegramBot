package ru.pavlikov.categorybot.service.excel;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.model.entities.Category;
import ru.pavlikov.categorybot.service.impl.CategoryService;
import ru.pavlikov.categorybot.service.impl.excel.ExcelBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcelBuilderTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ExcelBuilder excelBuilder;






    @Test
    void testBuildWithCategories() {
        File file= new File("src/test/fileForTest/Categories.xlsx");
        Command command= new Command();
        command.setFile(file);


        Category category1 = new Category();
        category1.setName("Category 1");

        when(categoryService.findRootCategory(command)).thenReturn(category1);
        excelBuilder.build(command);
        verify(categoryService, times(1)).findRootCategory(command);
    }


}
