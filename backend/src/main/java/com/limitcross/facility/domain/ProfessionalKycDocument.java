package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.KycDocType;
import com.limitcross.facility.domain.enumeration.VerificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ProfessionalKycDocument.
 */
@Entity
@Table(name = "professional_kyc_document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalKycDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false)
    private KycDocType docType;

    @Size(max = 255)
    @Column(name = "doc_number_enc", length = 255)
    private String docNumberEnc;

    @NotNull
    @Size(max = 255)
    @Column(name = "file_url", length = 255, nullable = false)
    private String fileUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VerificationStatus status;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Size(max = 255)
    @Column(name = "reject_reason", length = 255)
    private String rejectReason;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reviewer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessionalKycDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KycDocType getDocType() {
        return this.docType;
    }

    public ProfessionalKycDocument docType(KycDocType docType) {
        this.setDocType(docType);
        return this;
    }

    public void setDocType(KycDocType docType) {
        this.docType = docType;
    }

    public String getDocNumberEnc() {
        return this.docNumberEnc;
    }

    public ProfessionalKycDocument docNumberEnc(String docNumberEnc) {
        this.setDocNumberEnc(docNumberEnc);
        return this;
    }

    public void setDocNumberEnc(String docNumberEnc) {
        this.docNumberEnc = docNumberEnc;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public ProfessionalKycDocument fileUrl(String fileUrl) {
        this.setFileUrl(fileUrl);
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public VerificationStatus getStatus() {
        return this.status;
    }

    public ProfessionalKycDocument status(VerificationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
    }

    public Instant getReviewedAt() {
        return this.reviewedAt;
    }

    public ProfessionalKycDocument reviewedAt(Instant reviewedAt) {
        this.setReviewedAt(reviewedAt);
        return this;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getRejectReason() {
        return this.rejectReason;
    }

    public ProfessionalKycDocument rejectReason(String rejectReason) {
        this.setRejectReason(rejectReason);
        return this;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ProfessionalKycDocument createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getReviewer() {
        return this.reviewer;
    }

    public void setReviewer(User user) {
        this.reviewer = user;
    }

    public ProfessionalKycDocument reviewer(User user) {
        this.setReviewer(user);
        return this;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public ProfessionalKycDocument professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalKycDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalKycDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalKycDocument{" +
            "id=" + getId() +
            ", docType='" + getDocType() + "'" +
            ", docNumberEnc='" + getDocNumberEnc() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", status='" + getStatus() + "'" +
            ", reviewedAt='" + getReviewedAt() + "'" +
            ", rejectReason='" + getRejectReason() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
