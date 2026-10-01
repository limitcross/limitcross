package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.SearchKeyword} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchKeywordDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 80)
    private String keyword;

    private Integer weight;

    @NotNull
    private FacilityServiceDTO service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public FacilityServiceDTO getService() {
        return service;
    }

    public void setService(FacilityServiceDTO service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchKeywordDTO)) {
            return false;
        }

        SearchKeywordDTO searchKeywordDTO = (SearchKeywordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, searchKeywordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchKeywordDTO{" +
            "id=" + getId() +
            ", keyword='" + getKeyword() + "'" +
            ", weight=" + getWeight() +
            ", service=" + getService() +
            "}";
    }
}
