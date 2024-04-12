package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.OwnerDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;
import com.Japkutija.veterinarybackend.veterinary.service.PetService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PetMapper.class})
public interface OwnerMapper {

    @Mapping(source = "petUuids", target = "pets")
    Owner toOwner(OwnerDTO ownerDTO);


    @Mapping(source = "pets", target = "petUuids")
    OwnerDTO toOwnerDto(Owner owner);

    // Additional helper method to convert Pet to UUID
    default UUID petToUuid(Pet pet) {
        if (pet == null) {
            return null;
        }
        return pet.getUuid();
    }

    // Additional helper method to convert UUID to Pet
    default Pet uuidToPet(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        Pet pet = new Pet();
        pet.setUuid(uuid);
        return pet;
    }

    List<Owner> toOwnerList(List<OwnerDTO> ownerDTOs);

    List<OwnerDTO> toOwnerDTOList(List<Owner> owners);

    Owner updateOwnerFromDto(OwnerDTO ownerDTO, @MappingTarget Owner owner);

}

