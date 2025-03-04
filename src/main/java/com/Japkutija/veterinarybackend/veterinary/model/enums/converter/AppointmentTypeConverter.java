package com.Japkutija.veterinarybackend.veterinary.model.enums.converter;

import com.Japkutija.veterinarybackend.veterinary.model.enums.AppointmentType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AppointmentTypeConverter implements AttributeConverter<AppointmentType, Long> {

    @Override
    public Long convertToDatabaseColumn(AppointmentType attribute) {
        return attribute != null ? (long) attribute.getId() : null;
    }

    @Override
    public AppointmentType convertToEntityAttribute(Long dbData) {
        return dbData != null ? AppointmentType.fromId(dbData.intValue()) : null;
    }
}
