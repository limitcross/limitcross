package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.BannerPlacement;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.Banner} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BannerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 120)
    private String title;

    @NotNull
    @Size(max = 255)
    private String imageUrl;

    @Size(max = 255)
    private String deepLink;

    @NotNull
    private BannerPlacement placement;

    private Integer sortOrder;

    @NotNull
    private Instant startsAt;

    private Instant endsAt;

    @NotNull
    private Boolean active;

    private CityDTO city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public BannerPlacement getPlacement() {
        return placement;
    }

    public void setPlacement(BannerPlacement placement) {
        this.placement = placement;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BannerDTO)) {
            return false;
        }

        BannerDTO bannerDTO = (BannerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bannerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BannerDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", deepLink='" + getDeepLink() + "'" +
            ", placement='" + getPlacement() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", startsAt='" + getStartsAt() + "'" +
            ", endsAt='" + getEndsAt() + "'" +
            ", active='" + getActive() + "'" +
            ", city=" + getCity() +
            "}";
    }
}
