package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.TreatmentDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Treatment;

import java.util.List;
import java.util.UUID;

public interface TreatmentService {

    Treatment createTreatment(TreatmentDTO treatmentDTO);

    Treatment saveTreatment(Treatment treatment);

    Treatment updateTreatment(TreatmentDTO treatmentDTO, UUID treatmentUUID);

    Treatment getTreatmentByUuid(UUID treatmentUUID);

    List<Treatment> getAllTreatments();

    List<Treatment> getTreatmentsByPetUuid(UUID petUUID);

    void deleteTreatment(UUID treatmentUUID);

}
