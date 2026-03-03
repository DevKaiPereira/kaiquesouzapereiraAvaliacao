package com.kaiquesouzapereira.javaavaliacao.modules.documents.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Controller.DocumentController;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DocumentStatsService {

    private static final Logger auditLogger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentRepository documentRepository;

    @Scheduled(cron = "0 0 0 * * *")
    //@Scheduled(initialDelay = 5000, fixedRate = 86400000)
    public void generateDailyReport() {
        try {

            long totalFiles = documentRepository.countTotalDocuments();
            long totalBytes = documentRepository.sumTotalBytes();

            double totalMB = totalBytes / (1024.0 * 1024.0);

            auditLogger.info("[RELATÓRIO DIÁRIO] Total de arquivos: {} | Espaço total: {} bytes ({} MB)",
                    totalFiles, totalBytes, String.format("%.2f", totalMB));

        } catch (Exception e) {
            auditLogger.error("Error generating daily report: {}", e);
        }
    }
}