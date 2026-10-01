package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CityTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalAvailabilityTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalKycDocumentTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalSkillTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTierTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTimeOffTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalWalletTestSamples.*;
import static com.limitcross.facility.domain.ServiceZoneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProfessionalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Professional.class);
        Professional professional1 = getProfessionalSample1();
        Professional professional2 = new Professional();
        assertThat(professional1).isNotEqualTo(professional2);

        professional2.setId(professional1.getId());
        assertThat(professional1).isEqualTo(professional2);

        professional2 = getProfessionalSample2();
        assertThat(professional1).isNotEqualTo(professional2);
    }

    @Test
    void kycDocumentTest() {
        Professional professional = getProfessionalRandomSampleGenerator();
        ProfessionalKycDocument professionalKycDocumentBack = getProfessionalKycDocumentRandomSampleGenerator();

        professional.addKycDocument(professionalKycDocumentBack);
        assertThat(professional.getKycDocuments()).containsOnly(professionalKycDocumentBack);
        assertThat(professionalKycDocumentBack.getProfessional()).isEqualTo(professional);

        professional.removeKycDocument(professionalKycDocumentBack);
        assertThat(professional.getKycDocuments()).doesNotContain(professionalKycDocumentBack);
        assertThat(professionalKycDocumentBack.getProfessional()).isNull();

        professional.kycDocuments(new HashSet<>(Set.of(professionalKycDocumentBack)));
        assertThat(professional.getKycDocuments()).containsOnly(professionalKycDocumentBack);
        assertThat(professionalKycDocumentBack.getProfessional()).isEqualTo(professional);

        professional.setKycDocuments(new HashSet<>());
        assertThat(professional.getKycDocuments()).doesNotContain(professionalKycDocumentBack);
        assertThat(professionalKycDocumentBack.getProfessional()).isNull();
    }

    @Test
    void skillTest() {
        Professional professional = getProfessionalRandomSampleGenerator();
        ProfessionalSkill professionalSkillBack = getProfessionalSkillRandomSampleGenerator();

        professional.addSkill(professionalSkillBack);
        assertThat(professional.getSkills()).containsOnly(professionalSkillBack);
        assertThat(professionalSkillBack.getProfessional()).isEqualTo(professional);

        professional.removeSkill(professionalSkillBack);
        assertThat(professional.getSkills()).doesNotContain(professionalSkillBack);
        assertThat(professionalSkillBack.getProfessional()).isNull();

        professional.skills(new HashSet<>(Set.of(professionalSkillBack)));
        assertThat(professional.getSkills()).containsOnly(professionalSkillBack);
        assertThat(professionalSkillBack.getProfessional()).isEqualTo(professional);

        professional.setSkills(new HashSet<>());
        assertThat(professional.getSkills()).doesNotContain(professionalSkillBack);
        assertThat(professionalSkillBack.getProfessional()).isNull();
    }

    @Test
    void availabilityTest() {
        Professional professional = getProfessionalRandomSampleGenerator();
        ProfessionalAvailability professionalAvailabilityBack = getProfessionalAvailabilityRandomSampleGenerator();

        professional.addAvailability(professionalAvailabilityBack);
        assertThat(professional.getAvailabilities()).containsOnly(professionalAvailabilityBack);
        assertThat(professionalAvailabilityBack.getProfessional()).isEqualTo(professional);

        professional.removeAvailability(professionalAvailabilityBack);
        assertThat(professional.getAvailabilities()).doesNotContain(professionalAvailabilityBack);
        assertThat(professionalAvailabilityBack.getProfessional()).isNull();

        professional.availabilities(new HashSet<>(Set.of(professionalAvailabilityBack)));
        assertThat(professional.getAvailabilities()).containsOnly(professionalAvailabilityBack);
        assertThat(professionalAvailabilityBack.getProfessional()).isEqualTo(professional);

        professional.setAvailabilities(new HashSet<>());
        assertThat(professional.getAvailabilities()).doesNotContain(professionalAvailabilityBack);
        assertThat(professionalAvailabilityBack.getProfessional()).isNull();
    }

    @Test
    void timeOffTest() {
        Professional professional = getProfessionalRandomSampleGenerator();
        ProfessionalTimeOff professionalTimeOffBack = getProfessionalTimeOffRandomSampleGenerator();

        professional.addTimeOff(professionalTimeOffBack);
        assertThat(professional.getTimeOffs()).containsOnly(professionalTimeOffBack);
        assertThat(professionalTimeOffBack.getProfessional()).isEqualTo(professional);

        professional.removeTimeOff(professionalTimeOffBack);
        assertThat(professional.getTimeOffs()).doesNotContain(professionalTimeOffBack);
        assertThat(professionalTimeOffBack.getProfessional()).isNull();

        professional.timeOffs(new HashSet<>(Set.of(professionalTimeOffBack)));
        assertThat(professional.getTimeOffs()).containsOnly(professionalTimeOffBack);
        assertThat(professionalTimeOffBack.getProfessional()).isEqualTo(professional);

        professional.setTimeOffs(new HashSet<>());
        assertThat(professional.getTimeOffs()).doesNotContain(professionalTimeOffBack);
        assertThat(professionalTimeOffBack.getProfessional()).isNull();
    }

    @Test
    void homeCityTest() {
        Professional professional = getProfessionalRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        professional.setHomeCity(cityBack);
        assertThat(professional.getHomeCity()).isEqualTo(cityBack);

        professional.homeCity(null);
        assertThat(professional.getHomeCity()).isNull();
    }

    @Test
    void tierTest() {
        Professional professional = getProfessionalRandomSampleGenerator();
        ProfessionalTier professionalTierBack = getProfessionalTierRandomSampleGenerator();

        professional.setTier(professionalTierBack);
        assertThat(professional.getTier()).isEqualTo(professionalTierBack);

        professional.tier(null);
        assertThat(professional.getTier()).isNull();
    }

    @Test
    void zoneTest() {
        Professional professional = getProfessionalRandomSampleGenerator();
        ServiceZone serviceZoneBack = getServiceZoneRandomSampleGenerator();

        professional.addZone(serviceZoneBack);
        assertThat(professional.getZones()).containsOnly(serviceZoneBack);

        professional.removeZone(serviceZoneBack);
        assertThat(professional.getZones()).doesNotContain(serviceZoneBack);

        professional.zones(new HashSet<>(Set.of(serviceZoneBack)));
        assertThat(professional.getZones()).containsOnly(serviceZoneBack);

        professional.setZones(new HashSet<>());
        assertThat(professional.getZones()).doesNotContain(serviceZoneBack);
    }

    @Test
    void professionalWalletTest() {
        Professional professional = getProfessionalRandomSampleGenerator();
        ProfessionalWallet professionalWalletBack = getProfessionalWalletRandomSampleGenerator();

        professional.setProfessionalWallet(professionalWalletBack);
        assertThat(professional.getProfessionalWallet()).isEqualTo(professionalWalletBack);
        assertThat(professionalWalletBack.getProfessional()).isEqualTo(professional);

        professional.professionalWallet(null);
        assertThat(professional.getProfessionalWallet()).isNull();
        assertThat(professionalWalletBack.getProfessional()).isNull();
    }
}
