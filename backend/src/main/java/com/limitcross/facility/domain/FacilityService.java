package com.limitcross.facility.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "facility_service")
public class FacilityService implements Serializable {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 10)
    private String emoji;

    @Column(nullable = false, length = 40)
    private String category;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "price_range", nullable = false, length = 60)
    private String priceRange;

    @Column(name = "starting_price", nullable = false)
    private Integer startingPrice;

    @Column(nullable = false)
    private Double rating;

    @Column(name = "reviews_count", nullable = false)
    private Integer reviewsCount;

    @Column(nullable = false, length = 40)
    private String duration;

    @Column(nullable = false, length = 1000)
    private String highlights;

    @Column(name = "is_popular", nullable = false)
    private Boolean popular;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }
    public Integer getStartingPrice() { return startingPrice; }
    public void setStartingPrice(Integer startingPrice) { this.startingPrice = startingPrice; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Integer getReviewsCount() { return reviewsCount; }
    public void setReviewsCount(Integer reviewsCount) { this.reviewsCount = reviewsCount; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getHighlights() { return highlights; }
    public void setHighlights(String highlights) { this.highlights = highlights; }
    public Boolean getPopular() { return popular; }
    public void setPopular(Boolean popular) { this.popular = popular; }
}