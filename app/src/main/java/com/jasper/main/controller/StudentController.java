package com.jasper.main.controller;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StudentController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/get")
    public ResponseEntity<byte[]> getStudentDetailsInPDF() throws JRException, SQLException, FileNotFoundException {
        System.out.println("request received");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Content-Disposition", "inline; filename=" + "example.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
       // File file = ResourceUtils.getFile("classpath:employess.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport("/absolute-path/employees.jrxml");
       // JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("/absolute-path/employees.jasper");
        Map<String , Object> parameters = new HashMap<>();
        parameters.put("id", 1);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        System.out.println("Jasper print: "+ jasperPrint);
        byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
