package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.AppointmentDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "petUuid", target = "pet.uuid")
    @Mapping(source = "ownerUuid", target = "owner.uuid")
    @Mapping(source = "billUuid", target = "bill.uuid")
    Appointment toAppointment(AppointmentDTO appointmentDTO);

    @Mapping(source = "pet.uuid", target = "petUuid")
    @Mapping(source = "owner.uuid", target = "ownerUuid")
    @Mapping(source = "bill.uuid", target = "billUuid")
    AppointmentDTO toAppointmentDto(Appointment appointment);

    List<Appointment> toAppointmentList(List<AppointmentDTO> appointmentDTOs);

    List<AppointmentDTO> toAppointmentDTOList(List<Appointment> appointments);

    Appointment updateAppointmentFromDto(AppointmentDTO appointmentDTO, @MappingTarget Appointment appointment);


}
