package com.kaiquesouzapereira.javaavaliacao.modules.documents.Controller;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Dto.ChangeStatusDto;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Service.*;
import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.DocumentNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documents")
@Tag(name = "Documents", description = "Endpoints for VEXYN file management system")
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "apiKeyAuth")
@SecurityRequirement(name = "apiKeySecret")
public class DocumentController {

    @Autowired
    private UploadDocumentService uploadDocument;
    @Autowired
    private FetchAllDocumentService fetchAllDocument;
    @Autowired
    private DeleteDocumentService deleteDocument;
    @Autowired
    private UpdateDocumentService updateDocument;
    @Autowired
    private ChangeStatusDocumentService changeStatusDocument;

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);

    private String getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : "Anonymous";
    }

    @Operation(summary = "Upload document", description = "Sends a new file to the server and saves the metadata in MySQL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document created successfully."),
            @ApiResponse(responseCode = "400", description = "Error processing file.")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Object> uploadFile(
            @Parameter(description = "Select the physical file.") @RequestPart("file") MultipartFile file) {
        String user = getCurrentUser();
        LOGGER.info("User [{}] starting file upload: {}", user, file.getOriginalFilename());
        try {
            var id = uploadDocument.upload(file);
            LOGGER.info("User [{}] - Document sent successfully. ID: {}", user, id);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (Exception e) {
            LOGGER.error("User [{}] - error uploading file. {}: {}", user, file.getOriginalFilename(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "List to documents", description = "Returns all registered documents. Requires USER permission.")
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document created successfully."),
            @ApiResponse(responseCode = "400", description = "Error listing documents")
    })
    public ResponseEntity<Object> getAllDocuments() {
        String user = getCurrentUser();
        LOGGER.info("User [{}] requesting a list of all documents", user);
        try {
            var result = fetchAllDocument.fetchAllDocument();
            LOGGER.info("User [{}] - Listing completed successfully. Total: {}", user, result.size());
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            LOGGER.error("User [{}] - Error listing documents: {}", user, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete Document", description = "Removes the document record via ID. Requires MANAGER permission.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")@ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document successfully deleted."),
            @ApiResponse(responseCode = "400", description = "Document not found.")
    })
    public ResponseEntity<Object> deleteDocument(@PathVariable String id) {
        String user = getCurrentUser();
        LOGGER.info("User [{}] attempting to delete document ID: {}", user, id);
        try {
            deleteDocument.delete(id);
            LOGGER.info("User [{}] - Document ID: {} successfully deleted", user, id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }  catch (DocumentNotFound e) {
            LOGGER.error("User [{}] - Error deleting document with id {}! Not found.", user, id);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("User [{}] - Unexpected error while deleting ID. {}: {}", user, id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update File", description = "Replaces the file of an existing document.")
    @PutMapping(value = "/{id}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document successfully modified."),
            @ApiResponse(responseCode = "400", description = "Document not found.")
    })
    public ResponseEntity<Object> updateDocument(
            @PathVariable String id,
            @Parameter(description = "New file for replacement") @RequestPart("file") MultipartFile file) {
        String user = getCurrentUser();
        LOGGER.info("User [{}] requested an update to document ID: {}", user, id);
        try {
            updateDocument.updateDocument(id, file);
            LOGGER.info("User [{}] - Document ID file: {} updated successfully.", user, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOGGER.error("User [{}] - Error updating document ID.{}: {}", user, id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update status", description = "Updates the status of the document (e.g., Pending to Approved).")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document successfully modified."),
            @ApiResponse(responseCode = "400", description = "Document not found")
    })
    public ResponseEntity<Object> changeStatus(
            @PathVariable String id,
            @RequestBody ChangeStatusDto changeStatusDto) {
        String user = getCurrentUser();
        LOGGER.info("User [{}] changing document ID status: {} to {}", user, id, changeStatusDto.getStatus());
        try {
            changeStatusDocument.changeStatus(id, changeStatusDto.getStatus());
            LOGGER.info("User [{}] - Document ID Status: {} successfully modified", user, id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOGGER.error("User [{}] - Error changing document ID status. {}: {}", user, id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}