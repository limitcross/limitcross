package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.BookingSubscription;
import com.limitcross.facility.domain.CustomerAddress;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ServicePackage;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.BookingSubscriptionDTO;
import com.limitcross.facility.service.dto.CustomerAddressDTO;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ServicePackageDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingSubscription} and its DTO {@link BookingSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingSubscriptionMapper extends EntityMapper<BookingSubscriptionDTO, BookingSubscription> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userLogin")
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    @Mapping(target = "servicePackage", source = "servicePackage", qualifiedByName = "servicePackageName")
    @Mapping(target = "address", source = "address", qualifiedByName = "customerAddressLine1")
    @Mapping(target = "preferredProfessional", source = "preferredProfessional", qualifiedByName = "professionalDisplayName")
    BookingSubscriptionDTO toDto(BookingSubscription s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);

    @Named("servicePackageName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServicePackageDTO toDtoServicePackageName(ServicePackage servicePackage);

    @Named("customerAddressLine1")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "line1", source = "line1")
    CustomerAddressDTO toDtoCustomerAddressLine1(CustomerAddress customerAddress);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);
}
