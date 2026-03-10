package com.resume.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.resume.application.model.Resume;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;


@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private PdfService pdfService;
    
    public void sendResumeWithPassword(Resume resume, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo(resume.getEmail());
            helper.setSubject("Your Resume - Password Protected");
            
            String emailBody = String.format(
                "Dear %s,\n\n" +
                "Please find attached your password-protected resume.\n" +
                "Password: %s\n\n" +
                "Use this password to open your resume PDF.\n\n" +
                "Regards,\nResume Builder Team",
                resume.getFullName(), password
            );
            
            helper.setText(emailBody);
            
            byte[] pdfBytes = pdfService.generatePasswordProtectedPdf(resume, password);
            ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
            helper.addAttachment("Resume.pdf", dataSource);
            
            mailSender.send(message);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}