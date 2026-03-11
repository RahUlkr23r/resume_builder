package com.resume.application.controller;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resume.application.model.Resume;
import com.resume.application.service.EmailService;
import com.resume.application.service.PdfService;
import com.resume.application.service.ResumeService;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://gleaming-rabanadas-5d28cf.netlify.app/"
})
public class ResumeController {
    
    @Autowired
    private ResumeService resumeService;
    
    @Autowired
    private PdfService pdfService;
    
    @Autowired
    private EmailService emailService;
    
    private static final LocalTime DEPLOYMENT_TIME = LocalTime.now();
    
    @GetMapping("/check-time")
    public ResponseEntity<Map<String, Object>> checkTime() {
        Map<String, Object> response = new HashMap<>();
        LocalTime currentTime = LocalTime.now();
        boolean isExpired = currentTime.isAfter(DEPLOYMENT_TIME.plusMinutes(20));
        
        response.put("expired", isExpired);
        response.put("message", isExpired ? "Resume submission time has expired." : "Form is active");
        response.put("remainingMinutes", isExpired ? 0 : 
            java.time.Duration.between(currentTime, DEPLOYMENT_TIME.plusMinutes(20)).toMinutes());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/save")
    public ResponseEntity<?> saveResume(@RequestBody Resume resume) {
        if (LocalTime.now().isAfter(DEPLOYMENT_TIME.plusMinutes(20))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Resume submission time has expired.");
        }
        
        resume.setCreatedAt(LocalDateTime.now());
        Resume savedResume = resumeService.saveResume(resume);
        return ResponseEntity.ok(savedResume);
    }
    
    @PostMapping("/download-pdf/{id}")
    public ResponseEntity<Map<String, String>> downloadPdf(@PathVariable Long id) {
        Resume resume = resumeService.getResumeById(id);
        String password = resume.getFullName().replace(" ", "") + "-" + resume.getDob();
        byte[] pdfBytes = pdfService.generatePasswordProtectedPdf(resume, password);
        
        Map<String, String> response = new HashMap<>();
        response.put("password", password);
        response.put("pdf", java.util.Base64.getEncoder().encodeToString(pdfBytes));
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/send-email/{id}")
    public ResponseEntity<?> sendEmail(@PathVariable Long id) {
        Resume resume = resumeService.getResumeById(id);
        String password = resume.getFullName().replace(" ", "") + "-" + resume.getDob();
        
        emailService.sendResumeWithPassword(resume, password);
        resume.setEmailSent(true);
        resumeService.saveResume(resume);
        
        return ResponseEntity.ok("Email sent successfully");
    }
    
    @PostMapping("/send-whatsapp/{id}")
    public ResponseEntity<?> sendWhatsApp(@PathVariable Long id) {
        Resume resume = resumeService.getResumeById(id);
        
        if (resume.isWhatsappSent()) {
            return ResponseEntity.badRequest().body("WhatsApp message already sent");
        }
        
        // Integrate with WhatsApp Business API or Twilio here
        String password = resume.getFullName().replace(" ", "") + "-" + resume.getDob();
        // Send WhatsApp message logic
        
        resume.setWhatsappSent(true);
        resumeService.saveResume(resume);
        
        return ResponseEntity.ok("WhatsApp message sent successfully");
    }
}