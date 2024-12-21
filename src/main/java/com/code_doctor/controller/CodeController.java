package com.code_doctor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.code_doctor.service.AIAnalysisService;
import com.code_doctor.service.OCRService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/api/code")
public class CodeController {

    @Autowired
    private OCRService ocrService;

    @Autowired
    private AIAnalysisService aiAnalysisService;

    @PostMapping("/analyzeImg")
    public String analyzeCodeImg(@RequestParam("image") MultipartFile imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile.getInputStream());
        String code = ocrService.extractTextFromImage(image);
        return aiAnalysisService.analyzeCode(code);
    }

    @GetMapping("/analyzeTxt")
    public String analyzeCode(@RequestParam("code") String code) {
        return aiAnalysisService.analyzeCodeWithOllama(code);
    }
}
