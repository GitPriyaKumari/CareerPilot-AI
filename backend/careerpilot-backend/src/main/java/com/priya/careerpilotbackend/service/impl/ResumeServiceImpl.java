package com.priya.careerpilotbackend.service.impl;

import com.priya.careerpilotbackend.entity.Resume;
import com.priya.careerpilotbackend.entity.User;
import com.priya.careerpilotbackend.repository.ResumeRepository;
import com.priya.careerpilotbackend.repository.UserRepository;
import com.priya.careerpilotbackend.service.PdfService;
import com.priya.careerpilotbackend.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final PdfService pdfService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String uploadResume(String email, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Please select a PDF file.");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null ||
                !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new RuntimeException("Only PDF files are allowed.");
        }

        if (file.getSize() > 20L * 1024 * 1024) {
            throw new RuntimeException("File size cannot exceed 20 MB.");
        }

        try (InputStream input = file.getInputStream()) {

            byte[] signature = input.readNBytes(5);

            if (!new String(signature, StandardCharsets.US_ASCII)
                    .equals("%PDF-")) {
                throw new RuntimeException("Invalid PDF file.");
            }

        } catch (IOException e) {
            throw new RuntimeException("Unable to read uploaded file.", e);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Path savedFile = null;

        try {

            Path uploadPath = Paths.get(uploadDir)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(uploadPath);

            String storedFilename = UUID.randomUUID() + ".pdf";

            savedFile = uploadPath.resolve(storedFilename);

            Files.copy(file.getInputStream(), savedFile);

            String extractedText =
                    pdfService.extractText(savedFile.toString());

            if (extractedText == null || extractedText.isBlank()) {
                throw new RuntimeException(
                        "No readable text found in the PDF."
                );
            }

            Resume resume = Resume.builder()
                    .fileName(originalFilename)
                    .fileType("application/pdf")
                    .filePath(savedFile.toString())
                    .extractedText(extractedText)
                    .uploadedAt(LocalDateTime.now())
                    .user(user)
                    .build();

            resumeRepository.save(resume);

            return "Resume uploaded successfully.";

        } catch (IOException e) {

            if (savedFile != null) {
                try {
                    Files.deleteIfExists(savedFile);
                } catch (IOException ignored) {
                }
            }

            throw new RuntimeException("Resume upload failed.", e);
        }
    }

    @Override
    public List<Resume> getResumesByUser(String email) {

        return resumeRepository.findByUserEmail(email);
    }

    @Override
    public Resume getResumeById(Long resumeId, String email) {

        return resumeRepository.findByIdAndUserEmail(resumeId, email)
                .orElseThrow(() ->
                        new RuntimeException("Resume not found."));
    }

    @Override
    public Resume saveAnalysis(
            Long resumeId,
            String email,
            Integer resumeScore,
            String aiAnalysis
    ) {

        Resume resume = getResumeById(resumeId, email);

        resume.setResumeScore(resumeScore);
        resume.setAiAnalysis(aiAnalysis);

        return resumeRepository.save(resume);
    }
}