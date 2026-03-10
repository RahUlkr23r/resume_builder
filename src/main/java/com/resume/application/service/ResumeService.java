package com.resume.application.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resume.application.model.Resume;
import com.resume.application.repository.ResumeRepository;

@Service
public class ResumeService {
    
    @Autowired
    private ResumeRepository resumeRepository;
    
    public Resume saveResume(Resume resume) {
        return resumeRepository.save(resume);
    }
    
    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }
    
    public Resume getResumeById(Long id) {
        return resumeRepository.findById(id).orElse(null);
    }
    
    public Resume updateResume(Long id, Resume resumeDetails) {
        Resume resume = getResumeById(id);
        if (resume != null) {
            resume.setFullName(resumeDetails.getFullName());
            resume.setEmail(resumeDetails.getEmail());
            resume.setPhone(resumeDetails.getPhone());
            resume.setAddress(resumeDetails.getAddress());
            resume.setSummary(resumeDetails.getSummary());
            resume.setSkills(resumeDetails.getSkills());
            resume.setExperience(resumeDetails.getExperience());
            resume.setEducation(resumeDetails.getEducation());
            resume.setProjects(resumeDetails.getProjects());
            resume.setCertifications(resumeDetails.getCertifications());
            return resumeRepository.save(resume);
        }
        return null;
    }
    
    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }
}