package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.service.PdfService;
import com.pdfcrowd.Pdfcrowd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    /**
     * 接收前端传递的 gameID，调用 service 生成 PDF 并返回给前端
     * @param gameID
     * @return 生成的 PDF 文件
     */
    @GetMapping("/generate")
    public ResponseEntity<InputStreamResource> generateGamePdf(@RequestParam Long gameID) {
        try {
            // 调用 PdfService 生成 PDF 文件
            File pdfFile = pdfService.generatePdfFromGameID(gameID);

            // 准备响应头，设置文件下载的相关信息
            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());

            // 返回 PDF 文件流作为响应体
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfFile.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(fileInputStream));
        } catch (Pdfcrowd.Error | IOException e) {
            // 处理异常，返回 500 错误
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}

