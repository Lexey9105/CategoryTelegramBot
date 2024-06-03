package ru.pavlikov.categorybot.service.excel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pavlikov.categorybot.model.entities.Category;
import ru.pavlikov.categorybot.service.impl.excel.ExcelParser;

import java.io.File;
import java.io.IOException;;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ExcelParserTest {


    private ExcelParser parser;
    @BeforeEach
    void setUp() {
        parser = new ExcelParser();
    }





    @Test
    public void testParseExcelFileSuccess() throws IOException {

        File file = new File("src/test/fileForTest/Categories.xlsx");
        long chatId = 123456L;


        List<Category> categories = parser.parse(file, chatId);


        assertNotNull(categories);
        assertTrue(categories.size() > 0);
    }
}
