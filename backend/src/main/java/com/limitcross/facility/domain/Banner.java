package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.BannerPlacement;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Banner.
 */
@Entity
@Table(name = "banner")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 120)
    @Column(name = "title", length = 120, nullable = false)
    private String title;

    @NotNull
    @Size(max = 255)
    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;

    @Size(max = 255)
    @Column(name = "deep_link", length = 255)
    private String deepLink;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "placement", nullable = false)
    private BannerPlacement placement;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @NotNull
    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @Column(name = "ends_at")
    private Instant endsAt;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Banner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Banner title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Banner imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDeepLink() {
        return this.deepLink;
    }

    public Banner deepLink(String deepLink) {
        this.setDeepLink(deepLink);
        return this;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public BannerPlacement getPlacement() {
        return this.placement;
    }

    public Banner placement(BannerPlacement placement) {
        this.setPlacement(placement);
        return this;
    }

    public void setPlacement(BannerPlacement placement) {
        this.placement = placement;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public Banner sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Instant getStartsAt() {
        return this.startsAt;
    }

    public Banner startsAt(Instant startsAt) {
        this.setStartsAt(startsAt);
        return this;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return this.endsAt;
    }

    public Banner endsAt(Instant endsAt) {
        this.setEndsAt(endsAt);
        return this;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Banner active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Banner city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Banner)) {
            return false;
        }
        return getId() != null && getId().equals(((Banner) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Banner{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", deepLink='" + getDeepLink() + "'" +
            ", placement='" + getPlacement() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", startsAt='" + getStartsAt() + "'" +
            ", endsAt='" + getEndsAt() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
