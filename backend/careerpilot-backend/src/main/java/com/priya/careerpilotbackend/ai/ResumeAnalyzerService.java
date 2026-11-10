package com.priya.careerpilotbackend.ai;

import com.priya.careerpilotbackend.client.GeminiClient;
import com.priya.careerpilotbackend.dto.resume.ResumeAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeAnalyzerService {

    private final GeminiClient geminiClient;

    public ResumeAnalysisResponse analyzeResume(String resumeText) {

        return geminiClient.analyzeResume(resumeText);
    }
}