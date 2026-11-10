package com.priya.careerpilotbackend.service;

import com.priya.careerpilotbackend.entity.Resume;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {

    String uploadResume(String email, MultipartFile file);

    List<Resume> getResumesByUser(String email);

    Resume getResumeById(Long resumeId, String email);

    Resume saveAnalysis(
            Long resumeId,
            String email,
            Integer resumeScore,
            String aiAnalysis
    );
}