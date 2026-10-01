package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.KycDocType;
import com.limitcross.facility.domain.enumeration.VerificationStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalKycDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalKycDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private KycDocType docType;

    @Size(max = 255)
    private String docNumberEnc;

    @NotNull
    @Size(max = 255)
    private String fileUrl;

    @NotNull
    private VerificationStatus status;

    private Instant reviewedAt;

    @Size(max = 255)
    private String rejectReason;

    private Instant createdAt;

    private UserDTO reviewer;

    @NotNull
    private ProfessionalDTO professional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KycDocType getDocType() {
        return docType;
    }

    public void setDocType(KycDocType docType) {
        this.docType = docType;
    }

    public String getDocNumberEnc() {
        return docNumberEnc;
    }

    public void setDocNumberEnc(String docNumberEnc) {
        this.docNumberEnc = docNumberEnc;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public VerificationStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getReviewer() {
        return reviewer;
    }

    public void setReviewer(UserDTO reviewer) {
        this.reviewer = reviewer;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalKycDocumentDTO)) {
            return false;
        }

        ProfessionalKycDocumentDTO professionalKycDocumentDTO = (ProfessionalKycDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalKycDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalKycDocumentDTO{" +
            "id=" + getId() +
            ", docType='" + getDocType() + "'" +
            ", docNumberEnc='" + getDocNumberEnc() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", status='" + getStatus() + "'" +
            ", reviewedAt='" + getReviewedAt() + "'" +
            ", rejectReason='" + getRejectReason() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", reviewer=" + getReviewer() +
            ", professional=" + getProfessional() +
            "}";
    }
}
