package com.priya.careerpilotbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.priya.careerpilotbackend.ai.ResumeAnalyzerService;
import com.priya.careerpilotbackend.dto.resume.ResumeAnalysisResponse;
import com.priya.careerpilotbackend.entity.Resume;
import com.priya.careerpilotbackend.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeAnalyzerService resumeAnalyzerService;
    private final ObjectMapper objectMapper;

    @PostMapping("/upload")
    public String uploadResume(
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        return resumeService.uploadResume(
                userDetails.getUsername(),
                file
        );
    }

    @GetMapping("/analyze/{resumeId}")
    public ResumeAnalysisResponse analyzeResume(
            @PathVariable Long resumeId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        String email = userDetails.getUsername();

        Resume resume = resumeService.getResumeById(
                resumeId,
                email
        );

        ResumeAnalysisResponse analysis =
                resumeAnalyzerService.analyzeResume(
                        resume.getExtractedText()
                );

        try {

            String analysisJson =
                    objectMapper.writeValueAsString(analysis);

            resumeService.saveAnalysis(
                    resumeId,
                    email,
                    analysis.getResumeScore(),
                    analysisJson
            );

        } catch (JsonProcessingException e) {

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to save resume analysis."
            );
        }

        return analysis;
    }
}