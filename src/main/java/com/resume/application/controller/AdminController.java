package com.resume.application.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resume.application.model.Resume;
import com.resume.application.service.ResumeService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "https://resume-frontend-cybercrime.netlify.app")
public class AdminController {
    
    @Autowired
    private ResumeService resumeService;
    
    @GetMapping("/resumes")
    public List<Resume> getAllResumes() {
        return resumeService.getAllResumes();
    }
    
    @GetMapping("/resume/{id}")
    public Resume getResume(@PathVariable Long id) {
        return resumeService.getResumeById(id);
    }
    
    @PutMapping("/resume/{id}")
    public ResponseEntity<Resume> updateResume(@PathVariable Long id, @RequestBody Resume resume) {
        Resume updatedResume = resumeService.updateResume(id, resume);
        return ResponseEntity.ok(updatedResume);
    }
    
    @DeleteMapping("/resume/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.ok().build();
    }
}