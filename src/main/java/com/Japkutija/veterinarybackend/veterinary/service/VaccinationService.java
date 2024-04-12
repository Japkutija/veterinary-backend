package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.VaccinationDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Vaccination;

import java.util.List;
import java.util.UUID;

public interface VaccinationService {

    Vaccination createVaccination(VaccinationDTO vaccinationDTO);

    Vaccination saveVaccination(Vaccination vaccination);

    Vaccination updateVaccination(VaccinationDTO vaccinationDTO, UUID vaccinationUUID);

    Vaccination getVaccinationByUuid(UUID vaccinationUUID);

    List<Vaccination> getAllVaccinations();

    List<Vaccination> getVaccinationsByPetUuid(UUID petUUID);

    void deleteVaccination(UUID vaccinationUUID);
    
}
