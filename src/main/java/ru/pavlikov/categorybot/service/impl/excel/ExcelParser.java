package ru.pavlikov.categorybot.service.impl.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ru.pavlikov.categorybot.exception.DocParsingException;
import ru.pavlikov.categorybot.model.entities.Category;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.pavlikov.categorybot.constants.Constants.TRANS;

/**
 * @author pavlikov
 */
@Component
public class ExcelParser {



    /**
     * Принимает скачанный с апдейта Excel файл, находит в нем дерево категорий и возвращает его списком
     * @param file скачанный с апдейта Excel файл
     * @param chatId id юзера
     * @return {@link List<Category>} с найденными в файле категориями
     * @author pavlikov
     */
    public List<Category> parse(File file, long chatId) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
            return findNewCategoriesInSheet(workbook.getSheetAt(0), chatId);
        } catch (IOException e) {
            throw new DocParsingException();
        }
    }


    private List<Category> findNewCategoriesInSheet(  XSSFSheet sheet, long chatId) {
        List<Category> newCategories = new ArrayList<>();
        Map<Integer, Category> categoriesLevels = new HashMap<>();
        AtomicInteger maxLevel = new AtomicInteger(0);

        sheet.rowIterator().forEachRemaining(row -> {
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getStringCellValue().equals(TRANS)) {
                    break;
                }
            }

            if (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String categoryName = cell.getStringCellValue();
                int categoryLevel = cell.getColumnIndex();
                maxLevel.set(categoryLevel);
                Category newCategory = new Category(categoriesLevels.get(categoryLevel - 1),
                        categoryName, chatId);
                categoriesLevels.put(categoryLevel, newCategory);
                newCategories.add(newCategory);
            }
        });

        return newCategories;
    }
}
