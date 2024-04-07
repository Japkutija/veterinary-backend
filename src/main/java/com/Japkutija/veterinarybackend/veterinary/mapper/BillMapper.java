package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.BillDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillMapper {

    @Mapping(source = "ownerUuid", target = "owner.uuid")
    Bill toBill(BillDTO billDTO);

    @Mapping(source = "owner.uuid", target = "ownerUuid")
    BillDTO toBillDto(Bill bill);

    List<Bill> toBillList(List<BillDTO> billDTOs);

    List<BillDTO> toBillDTOList(List<Bill> bills);

    Bill updateBillFromDto(BillDTO billDTO, @MappingTarget Bill bill);


}
