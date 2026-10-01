package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.IncentiveRule;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalIncentiveAward;
import com.limitcross.facility.domain.WalletTransaction;
import com.limitcross.facility.service.dto.IncentiveRuleDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ProfessionalIncentiveAwardDTO;
import com.limitcross.facility.service.dto.WalletTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalIncentiveAward} and its DTO {@link ProfessionalIncentiveAwardDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalIncentiveAwardMapper extends EntityMapper<ProfessionalIncentiveAwardDTO, ProfessionalIncentiveAward> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    @Mapping(target = "rule", source = "rule", qualifiedByName = "incentiveRuleName")
    @Mapping(target = "walletTransaction", source = "walletTransaction", qualifiedByName = "walletTransactionId")
    ProfessionalIncentiveAwardDTO toDto(ProfessionalIncentiveAward s);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);

    @Named("incentiveRuleName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    IncentiveRuleDTO toDtoIncentiveRuleName(IncentiveRule incentiveRule);

    @Named("walletTransactionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WalletTransactionDTO toDtoWalletTransactionId(WalletTransaction walletTransaction);
}
