package com.code_doctor.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class OCRService {

    public String extractTextFromImage(BufferedImage image) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata"); // Path to Tesseract OCR trained data
        try {
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            throw new RuntimeException("Error processing image for OCR", e);
        }
    }
}
