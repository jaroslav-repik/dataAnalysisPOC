package cz.katalpa.dataanalysispoc.controller;

import cz.katalpa.dataanalysispoc.model.Column;
import cz.katalpa.dataanalysispoc.model.Table;
import cz.katalpa.dataanalysispoc.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/createReport")
public class ReportController {


	@RequestMapping(method=RequestMethod.POST)
	public String createReport(Model model, HttpServletRequest request) throws IOException {

        long start = System.currentTimeMillis();

        Map<String, String[]> requestParams = request.getParameterMap();

        List<String> selectedTables = new ArrayList<String>();
        List<String> selectedColumns = new ArrayList<String>();

        for (String paramKey : requestParams.keySet()) {
            String paramValue = requestParams.get(paramKey)[0];
            if (paramKey.startsWith("DATA_") && ("on".equals(paramValue) || "true".equals(paramValue))) {
                String table = paramKey.substring("DATA_".length(),paramKey.indexOf("|"));
                //String column = paramKey.substring(paramKey.indexOf("|") + 1);
                if (!selectedTables.contains(table)) {
                    selectedTables.add(table);
                }
                selectedColumns.add(paramKey);
            }
        }

        Workbook wb = (Workbook)request.getSession(false).getAttribute("workbook");

        List<Table> tables = new ArrayList<Table>();
        for (String selectedTable : selectedTables) {
            Table table = new Table();
            table.setName(selectedTable);
            List<Column> columns = new ArrayList<Column>();
            Sheet sheet = wb.getSheet(selectedTable);
            int firstRowNum = wb.getSheet(selectedTable).getFirstRowNum();
            Row row = wb.getSheet(selectedTable).getRow(firstRowNum);
            if (row != null) {
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    try {
                        Cell cell = row.getCell(j);
                        String cellValue = ExcelUtils.getCellValue(cell);
                        if (cellValue == null) {
                            continue;
                        }

                        if (selectedColumns.contains("DATA_"+selectedTable+"|"+cellValue)) {
                            //column was selected
                            Column column = ExcelUtils.getColumnDetail(sheet, j);
                            column.setName(cellValue);
                            columns.add(column);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!columns.isEmpty()) {
                table.setColumns(columns);
                tables.add(table);
            }
        }
        model.addAttribute("tables", tables);

        long timePassed = System.currentTimeMillis() - start;
        model.addAttribute("timePassed", new Long(timePassed));

        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory =  Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        model.addAttribute("totalMemory", new Long(totalMemory));
        model.addAttribute("freeMemory", new Long(freeMemory));
        model.addAttribute("usedMemory", new Long(usedMemory));

        return "poc_report";

	}
	
}
