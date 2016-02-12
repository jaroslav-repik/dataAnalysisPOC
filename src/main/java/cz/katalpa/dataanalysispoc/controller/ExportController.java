package cz.katalpa.dataanalysispoc.controller;

import cz.katalpa.dataanalysispoc.model.Column;
import cz.katalpa.dataanalysispoc.model.Table;
import cz.katalpa.dataanalysispoc.utils.ExcelUtils;
import cz.katalpa.dataanalysispoc.utils.OracleUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/export")
public class ExportController {



	@RequestMapping(method=RequestMethod.POST)
	public String exportData(Model model, HttpServletRequest request) throws IOException {

        long start = System.currentTimeMillis();

        List<String> dbMessages = new ArrayList<String>();
        List<String> selectedTables = (List<String>) request.getSession(false).getAttribute("selectedTables");
        List<String> selectedColumns = (List<String>) request.getSession(false).getAttribute("selectedColumns");

        //create DB connection
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }

        List<String> createdTemporaryTables = new ArrayList<String>();

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "poc", "poc");

            Workbook wb = (Workbook) request.getSession(false).getAttribute("workbook");

            for (String selectedTable : selectedTables) {
                Sheet sheet = wb.getSheet(selectedTable);
                int firstRowNum = wb.getSheet(selectedTable).getFirstRowNum();
                int lastRowNum = wb.getSheet(selectedTable).getLastRowNum();

                List<Integer> selectedColumnsIndexes = new ArrayList<Integer>();
                List<String> selectedColumnsNames = new ArrayList<String>();
                for (int i = firstRowNum; i < lastRowNum; i++) {
                    Row row = sheet.getRow(i);

                    if (i == firstRowNum) {
                        for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String cellValue = ExcelUtils.getCellValue(cell);
                            if (cellValue == null) {
                                continue;
                            }

                            if (selectedColumns.contains("DATA_" + selectedTable + "|" + cellValue)) {
                                selectedColumnsNames.add(OracleUtils.normalizeOracleName(cellValue));
                                selectedColumnsIndexes.add(j);
                            }
                        }

                        // create temporary table
                        StringBuilder tempTableQuery = new StringBuilder("CREATE GLOBAL TEMPORARY TABLE ");
                        tempTableQuery.append(OracleUtils.normalizeOracleName(selectedTable));
                        tempTableQuery.append("(");
                        String prefix = "";
                        for (String columnName : selectedColumnsNames) {
                            tempTableQuery.append(prefix);
                            prefix = ",";
                            tempTableQuery.append(columnName);
                            tempTableQuery.append(" varchar2(4000)");
                        }
                        tempTableQuery.append(") ON COMMIT PRESERVE ROWS");
                        dbMessages.add(tempTableQuery.toString());
                        stmt = connection.prepareStatement(tempTableQuery.toString());
                        createdTemporaryTables.add(selectedTable);
                        try {
                            stmt.executeUpdate();
                            stmt.close();
                        } catch (Exception e) {
                            dbMessages.add("Error creating temporary table with query " + tempTableQuery + "." + e.getMessage());
                            System.out.println("Error creating temporary table with query " + tempTableQuery + "." + e.getMessage());
                        }
                    } else {
                        if (selectedColumnsIndexes.isEmpty()) {
                            continue;
                        }
                        // insert individual rows
                        StringBuilder insertQuery = new StringBuilder("insert into ").append(OracleUtils.normalizeOracleName(selectedTable)).append(" values (");
                        String prefix = "";
                        List<String> cellValues = new ArrayList<String>();
                        for (int j : selectedColumnsIndexes) {
                            Cell cell = row.getCell(j);
                            String cellValue = ExcelUtils.getCellValue(cell);
                            cellValues.add(cellValue);

                            insertQuery.append(prefix);
                            prefix = ",";
                            insertQuery.append("?");
                        }
                        insertQuery.append(")");
                        //System.out.println(insertQuery.toString() + " " +i);
                        stmt = connection.prepareStatement(insertQuery.toString());
                        int k = 1;
                        for (String cellValue : cellValues) {
                            stmt.setString(k++, cellValue);
                        }
                        try {
                            stmt.executeUpdate();
                        } catch (Exception e0) {
                            System.out.println("Error inserting row " + insertQuery.toString() + " " +i + ": "+ e0.getMessage());
                        }
                        stmt.close();
                    }
                }
                connection.commit();

                if (createdTemporaryTables.contains(selectedTable)) {
                    //table test query
                    String query = "select count(*) as total from " + OracleUtils.normalizeOracleName(selectedTable);
                    stmt = connection.prepareStatement(query);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        int totalRows = rs.getInt("total");
                        dbMessages.add(query + ": " + totalRows);
                    }
                    rs.close();
                }


            }
        } catch (SQLException e) {
            dbMessages.add(e.getMessage());
            e.printStackTrace();

        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    //ignore
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }

        //drop tables
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "poc", "poc");
            for (String selectedTable : selectedTables) {
                if (createdTemporaryTables.contains(selectedTable)) {
                    String query = "drop table " + OracleUtils.normalizeOracleName(selectedTable);
                    dbMessages.add(query);
                    stmt = connection.prepareStatement(query);
                    try {
                        stmt.executeUpdate();
                    } catch (Exception e0) {
                        dbMessages.add(e0.getMessage());
                        e0.printStackTrace();
                    } finally {
                        stmt.close();
                    }
                }
            }

        } catch (SQLException e) {
            dbMessages.add(e.getMessage());
            e.printStackTrace();

        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    //ignore
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }


        model.addAttribute("dbMessages", dbMessages);

        long timePassed = System.currentTimeMillis() - start;
        model.addAttribute("timePassed", new Long(timePassed));

        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory =  Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        model.addAttribute("totalMemory", new Long(totalMemory));
        model.addAttribute("freeMemory", new Long(freeMemory));
        model.addAttribute("usedMemory", new Long(usedMemory));

        return "poc_db_result";

	}
	
}
