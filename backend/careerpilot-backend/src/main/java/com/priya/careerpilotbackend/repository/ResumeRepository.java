package com.priya.careerpilotbackend.repository;

import com.priya.careerpilotbackend.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUserEmail(String email);

    Optional<Resume> findByIdAndUserEmail(Long resumeId, String email);
}