package com.Japkutija.veterinarybackend.veterinary.model.enums.converter;

import com.Japkutija.veterinarybackend.veterinary.model.enums.MedicalRecordStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MedicalRecordStatusConverter implements AttributeConverter<MedicalRecordStatus, Long> {

    @Override
    public Long convertToDatabaseColumn(MedicalRecordStatus attribute) {
        return attribute != null ? (long) attribute.getId() : null;
    }

    @Override
    public MedicalRecordStatus convertToEntityAttribute(Long dbData) {
        return dbData != null ? MedicalRecordStatus.fromId(dbData.intValue()) : null;
    }
}
