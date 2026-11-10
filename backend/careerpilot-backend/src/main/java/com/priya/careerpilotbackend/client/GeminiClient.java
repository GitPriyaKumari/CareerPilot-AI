package com.priya.careerpilotbackend.client;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.priya.careerpilotbackend.dto.resume.ResumeAnalysisResponse;
import org.springframework.stereotype.Component;

@Component
public class GeminiClient {

    private final Client client;
    private final ObjectMapper objectMapper;

    public GeminiClient(ObjectMapper objectMapper) {
        this.client = new Client();
        this.objectMapper = objectMapper;
    }

    public ResumeAnalysisResponse analyzeResume(String resumeText) {

        String prompt = """
                Analyze the following resume and return ONLY valid JSON.

                The JSON must have exactly these fields:

                {
                  "resumeScore": 0,
                  "skills": [],
                  "education": [],
                  "experience": [],
                  "projects": [],
                  "certifications": [],
                  "strengths": [],
                  "improvements": []
                }

                Rules:
                - resumeScore must be a number from 0 to 100.
                - skills must contain technical and professional skills found in the resume.
                - education must contain the education details found in the resume.
                - experience must contain work or internship experience.
                - projects must contain projects mentioned in the resume.
                - certifications must contain certifications mentioned in the resume.
                - strengths must contain useful strengths based on the resume.
                - improvements must contain specific suggestions for improving the resume.
                - Do not invent information that is not present in the resume.
                - Return ONLY JSON.
                - Do not use markdown.
                - Do not put the JSON inside ```.

                Resume:
                %s
                """.formatted(resumeText);

        GenerateContentResponse response = client.models.generateContent(
                "gemini-3.8-flash",
                prompt,
                null
        );

        try {

            String jsonResponse = response.text();

            return objectMapper.readValue(
                    jsonResponse,
                    ResumeAnalysisResponse.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse Gemini response",
                    e
            );
        }
    }
}