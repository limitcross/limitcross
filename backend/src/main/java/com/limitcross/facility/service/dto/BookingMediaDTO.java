package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.MediaType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.BookingMedia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingMediaDTO implements Serializable {

    private Long id;

    @NotNull
    private MediaType mediaType;

    @NotNull
    @Size(max = 255)
    private String fileUrl;

    private Instant createdAt;

    private UserDTO uploadedBy;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UserDTO uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingMediaDTO)) {
            return false;
        }

        BookingMediaDTO bookingMediaDTO = (BookingMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingMediaDTO{" +
            "id=" + getId() +
            ", mediaType='" + getMediaType() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", uploadedBy=" + getUploadedBy() +
            ", booking=" + getBooking() +
            "}";
    }
}
