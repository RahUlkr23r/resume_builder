package com.resume.application.service;


import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.resume.application.model.Resume;

@Service
public class PdfService {
    
    public byte[] generatePasswordProtectedPdf(Resume resume, String password) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            // Set up writer with encryption
            WriterProperties writerProps = new WriterProperties();
            
            // Set encryption with user password and owner password
            // 128-bit AES encryption
            writerProps.setStandardEncryption(
                password.getBytes(), // User password
                password.getBytes(), // Owner password
                EncryptionConstants.ALLOW_PRINTING, // Permissions
                EncryptionConstants.ENCRYPTION_AES_128 // Encryption algorithm
            );
            
            PdfWriter writer = new PdfWriter(baos, writerProps);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            // Set document margins
            document.setMargins(50, 50, 50, 50);
            
            // Add resume content
            PdfFont font = PdfFontFactory.createFont();
            PdfFont boldFont = PdfFontFactory.createFont();
            
            // Header
            Paragraph header = new Paragraph(resume.getFullName())
                .setFont(boldFont)
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
            document.add(header);
            
            // Contact Info
            Paragraph contact = new Paragraph()
                .add(new Text(resume.getEmail()).setFont(font))
                .add(new Text(" | ").setFont(font))
                .add(new Text(resume.getPhone()).setFont(font))
                .add(new Text("\n").setFont(font))
                .add(new Text(resume.getAddress()).setFont(font))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
            document.add(contact);
            
            // Summary Section
            if (resume.getSummary() != null && !resume.getSummary().isEmpty()) {
                document.add(new Paragraph("PROFESSIONAL SUMMARY")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setMarginTop(10)
                    .setMarginBottom(5));
                document.add(new Paragraph(resume.getSummary())
                    .setFont(font)
                    .setMarginBottom(10));
            }
            
            // Skills Section
            if (resume.getSkills() != null && !resume.getSkills().isEmpty()) {
                document.add(new Paragraph("SKILLS")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setMarginTop(10)
                    .setMarginBottom(5));
                document.add(new Paragraph(resume.getSkills())
                    .setFont(font)
                    .setMarginBottom(10));
            }
            
            // Experience Section
            if (resume.getExperience() != null && !resume.getExperience().isEmpty()) {
                document.add(new Paragraph("WORK EXPERIENCE")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setMarginTop(10)
                    .setMarginBottom(5));
                document.add(new Paragraph(resume.getExperience())
                    .setFont(font)
                    .setMarginBottom(10));
            }
            
            // Education Section
            if (resume.getEducation() != null && !resume.getEducation().isEmpty()) {
                document.add(new Paragraph("EDUCATION")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setMarginTop(10)
                    .setMarginBottom(5));
                document.add(new Paragraph(resume.getEducation())
                    .setFont(font)
                    .setMarginBottom(10));
            }
            
            // Projects Section
            if (resume.getProjects() != null && !resume.getProjects().isEmpty()) {
                document.add(new Paragraph("PROJECTS")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setMarginTop(10)
                    .setMarginBottom(5));
                document.add(new Paragraph(resume.getProjects())
                    .setFont(font)
                    .setMarginBottom(10));
            }
            
            // Certifications Section
            if (resume.getCertifications() != null && !resume.getCertifications().isEmpty()) {
                document.add(new Paragraph("CERTIFICATIONS")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setMarginTop(10)
                    .setMarginBottom(5));
                document.add(new Paragraph(resume.getCertifications())
                    .setFont(font)
                    .setMarginBottom(10));
            }
            
            // Footer with password info
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("This PDF is password protected")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20));
            
            document.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return baos.toByteArray();
    }
}