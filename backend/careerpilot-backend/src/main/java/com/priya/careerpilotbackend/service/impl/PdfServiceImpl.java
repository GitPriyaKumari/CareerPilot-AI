package com.priya.careerpilotbackend.service.impl;

import com.priya.careerpilotbackend.service.PdfService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class PdfServiceImpl implements PdfService {

    @Override
    public String extractText(String filePath) throws IOException {

        try (var document = Loader.loadPDF(Path.of(filePath).toFile())) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);
        }
    }
}