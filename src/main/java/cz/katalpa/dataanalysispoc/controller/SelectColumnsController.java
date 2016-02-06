package cz.katalpa.dataanalysispoc.controller;

import cz.katalpa.dataanalysispoc.model.Column;
import cz.katalpa.dataanalysispoc.model.Table;
import cz.katalpa.dataanalysispoc.model.Template;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/selectColumns")
public class SelectColumnsController {


	@RequestMapping(method=RequestMethod.POST)
	public String selectColumns(HttpServletRequest request) throws IOException {

		request.getParameterMap();
        return "poc_select_columns";

	}
	
}
