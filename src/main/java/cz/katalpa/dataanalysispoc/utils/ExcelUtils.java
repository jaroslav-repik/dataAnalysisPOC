package cz.katalpa.dataanalysispoc.utils;

import cz.katalpa.dataanalysispoc.model.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jaroslav.repik
 */
public class ExcelUtils {

    public static final String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String cellValue = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BLANK:
                break;
            case Cell.CELL_TYPE_ERROR:
                cellValue = String.valueOf(cell.getErrorCellValue());
                break;

            // CELL_TYPE_FORMULA will never occur
            case Cell.CELL_TYPE_FORMULA:
                break;
        }
        return cellValue;
    }

    public static Column getColumnDetail(Sheet sheet, int columnIndex) {
        Column column = new Column();
        Set values = new HashSet();
        long recordsTotal = 0;
        for (int i = sheet.getFirstRowNum() + 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(columnIndex);
            String cellValue = getCellValue(cell);
            if (cellValue != null) {
                values.add(cellValue);
                recordsTotal ++;
            }
        }

        column.setRecordsTotal(recordsTotal);
        column.setRecordsUnique(values.size());

        return column;
    }
}
