package com.resume.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resume.application.model.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByEmailSentFalse();
    List<Resume> findByWhatsappSentFalse();
}