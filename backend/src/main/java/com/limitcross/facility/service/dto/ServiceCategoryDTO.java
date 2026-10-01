package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ServiceCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String code;

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String icon;

    private Integer sortOrder;

    @NotNull
    private Boolean active;

    private ServiceCategoryDTO parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ServiceCategoryDTO getParent() {
        return parent;
    }

    public void setParent(ServiceCategoryDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceCategoryDTO)) {
            return false;
        }

        ServiceCategoryDTO serviceCategoryDTO = (ServiceCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceCategoryDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", icon='" + getIcon() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", active='" + getActive() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
