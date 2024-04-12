package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.VaccinationMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.VaccinationDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Vaccination;
import com.Japkutija.veterinarybackend.veterinary.repository.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaccinationServiceImpl implements com.Japkutija.veterinarybackend.veterinary.service.VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final VaccinationMapper vaccinationMapper;

    @Override
    @Transactional
    public Vaccination createVaccination(VaccinationDTO vaccinationDTO) {
        var vaccination = vaccinationMapper.toVaccination(vaccinationDTO);

        return saveVaccination(vaccination);
    }

    @Override
    @Transactional
    public Vaccination saveVaccination(Vaccination vaccination) {

        try {
            return vaccinationRepository.save(vaccination);
        } catch (Exception ex) {
            throw new EntitySavingException(Vaccination.class, ex);
        }
    }

    @Override
    @Transactional
    public Vaccination updateVaccination(VaccinationDTO vaccinationDTO, UUID vaccinationUUID) {
        var vaccination = getVaccinationByUuid(vaccinationUUID);

        var updatedVaccination = vaccinationMapper.updateVaccinationFromDto(vaccinationDTO, vaccination);

        return saveVaccination(updatedVaccination);
    }

    @Override
    @Transactional(readOnly = true)
    public Vaccination getVaccinationByUuid(UUID vaccinationUUID) {
        return vaccinationRepository.findByUuid(vaccinationUUID).orElseThrow(() -> new EntityNotFoundException(Vaccination.class, vaccinationUUID);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vaccination> getAllVaccinations() {
        var vaccinations = vaccinationRepository.findAll();

        if (vaccinations.isEmpty()) {
            return List.of();
        }

        return vaccinations;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vaccination> getVaccinationsByPetUuid(UUID petUUID) {
        var vaccinations = vaccinationRepository.findByPetUuid(petUUID);

        if (vaccinations.isEmpty()) {
            return List.of();
        }

        return vaccinations;
    }

    @Override
    @Transactional
    public void deleteVaccination(UUID vaccinationUUID) {

        var vaccination = getVaccinationByUuid(vaccinationUUID);

        vaccinationRepository.delete(vaccination);
    }
}
