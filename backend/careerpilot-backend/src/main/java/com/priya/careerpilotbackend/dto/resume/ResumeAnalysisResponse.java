package com.priya.careerpilotbackend.dto.resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysisResponse {

    private int resumeScore;

    private List<String> skills;

    private List<String> education;

    private List<String> experience;

    private List<String> projects;

    private List<String> certifications;

    private List<String> strengths;

    private List<String> improvements;
}