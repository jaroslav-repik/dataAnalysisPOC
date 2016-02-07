package cz.katalpa.dataanalysispoc.controller;

import cz.katalpa.dataanalysispoc.model.Column;
import cz.katalpa.dataanalysispoc.model.Table;
import cz.katalpa.dataanalysispoc.model.Template;
import cz.katalpa.dataanalysispoc.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/upload")
public class UploadController {


	@RequestMapping(method=RequestMethod.GET)
	public void fileUploadForm() {
	}

	@RequestMapping(method=RequestMethod.POST)
	public String processUpload(@RequestParam MultipartFile file, Model model, HttpServletRequest request) throws IOException {
		//model.addAttribute("message", "File '" + file.getOriginalFilename() + "' uploaded successfully");

        long start = System.currentTimeMillis();

		try {
			Workbook wb = WorkbookFactory.create(file.getInputStream());
            //Workbook wb = new XSSFWorkbook(file.getInputStream());

            //formula evaluator
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            Template template = new Template();
            template.setName(file.getOriginalFilename());

			List<Table> tables = new ArrayList<Table>();
            template.setTables(tables);
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                Table table = new Table();
                table.setName(wb.getSheetName(i));
				tables.add(table);
                int firstRowNum = wb.getSheetAt(i).getFirstRowNum();
                Row row = wb.getSheetAt(i).getRow(firstRowNum);
                List<Column> columns = new ArrayList<Column>();
                table.setColumns(columns);
                if (row != null) {
                    for (int j = row.getFirstCellNum(); j< row.getLastCellNum(); j++) {
                        try {
                            Cell cell = row.getCell(j);
                            String cellValue = ExcelUtils.getCellValue(cell);
                            if (cellValue == null) {
                                continue;
                            }
                            Column column = new Column();
                            column.setName(cellValue);
                            columns.add(column);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
			}
            model.addAttribute("tables", tables);
            request.getSession(false).setAttribute("workbook", wb);
            request.getSession(false).setAttribute("template", template);

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message","File format not supported.");
		}

        long timePassed = System.currentTimeMillis() - start;
        model.addAttribute("timePassed", new Long(timePassed));

        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory =  Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        model.addAttribute("totalMemory", new Long(totalMemory));
        model.addAttribute("freeMemory", new Long(freeMemory));
        model.addAttribute("usedMemory", new Long(usedMemory));

        return "poc_select_columns";

	}
	
}
