package ru.pavlikov.categorybot.service.impl.excel;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;
import ru.pavlikov.categorybot.exception.DocumentDownloadException;
import ru.pavlikov.categorybot.model.Command;
import ru.pavlikov.categorybot.model.entities.Category;
import ru.pavlikov.categorybot.service.impl.CategoryService;


import java.io.FileOutputStream;
import java.io.IOException;

import static ru.pavlikov.categorybot.constants.Constants.TRANS;

/**
 * @author pavlikov
 */
@Component
@RequiredArgsConstructor
public class ExcelBuilder {


    private final CategoryService categoryService;
    private int rowNum;

    private XSSFCellStyle descriptorCellStyle;

    /**
     * Собирает дерево категорий в файл Excel
     * @author pavlikov
     */
    public void build(Command command)  {
        Category root=categoryService.findRootCategory(command);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            changeWorkBookFont(workbook);
            createDescriptorCellStyle(workbook);

            XSSFSheet sheet = workbook.createSheet("Категории");

            fillSheet(sheet,1,root);

            workbook.write(new FileOutputStream(command.getFile()));
        }catch (IOException e){
            throw new DocumentDownloadException();

        }
    }

    public void fillSheet(XSSFSheet sheet, int level, Category parent){
        XSSFRow row = sheet.createRow(rowNum++);
        XSSFCell cell = row.createCell(level - 1);
        cell.setCellStyle(descriptorCellStyle);
        cell.setCellValue(TRANS);
        cell = row.createCell(level);
        cell.setCellValue(parent.getName());
        categoryService.findCategoriesByParen(parent).forEach(category -> {
            fillSheet(sheet,level+1,category);
        });
    }

    private void changeWorkBookFont(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createCellStyle().getFont();
        font.setFontHeightInPoints((short) 14);
        workbook.createCellStyle().setFont(font);
    }

    private void createDescriptorCellStyle(XSSFWorkbook workbook) {
        descriptorCellStyle = workbook.createCellStyle();
        descriptorCellStyle.setAlignment(HorizontalAlignment.RIGHT);
    }
}
