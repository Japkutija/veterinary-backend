package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.TreatmentMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.TreatmentDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Treatment;
import com.Japkutija.veterinarybackend.veterinary.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TreatmentServiceImpl implements com.Japkutija.veterinarybackend.veterinary.service.TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final TreatmentMapper treatmentMapper;

    @Override
    @Transactional
    public Treatment createTreatment(TreatmentDTO treatmentDTO) {

        var treatment = treatmentMapper.toTreatment(treatmentDTO);

        return saveTreatment(treatment);
    }

    @Override
    @Transactional
    public Treatment saveTreatment(Treatment treatment) {
        try {
            return treatmentRepository.save(treatment);
        } catch (Exception ex) {
            log.error("Error while saving treatment: {}", ex.getMessage());
            throw new EntitySavingException(Treatment.class, ex);
        }
    }

    @Override
    @Transactional
    public Treatment updateTreatment(TreatmentDTO treatmentDTO, UUID treatmentUUID) {
        var treatment = getTreatmentByUuid(treatmentUUID);

        var updatedTreatment = treatmentMapper.updateTreatmentFromDto(treatmentDTO, treatment);

        return saveTreatment(updatedTreatment);
    }

    @Override
    @Transactional(readOnly = true)
    public Treatment getTreatmentByUuid(UUID treatmentUUID) {

        return treatmentRepository.findByUuid(treatmentUUID)
                .orElseThrow(() -> new EntityNotFoundException(Treatment.class, treatmentUUID));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Treatment> getAllTreatments() {
        var treatments = treatmentRepository.findAll();

        if (treatments.isEmpty())
            return List.of();

        return treatments;
    }

    @Override
    public List<Treatment> getTreatmentsByPetUuid(UUID petUUID) {
        var treatments = treatmentRepository.findAll();

        if (treatments.isEmpty())
            return List.of();

        return treatments;
    }

    @Override
    @Transactional
    public void deleteTreatment(UUID treatmentUUID) {

        var treatment = getTreatmentByUuid(treatmentUUID);

        treatmentRepository.delete(treatment);

    }
}
