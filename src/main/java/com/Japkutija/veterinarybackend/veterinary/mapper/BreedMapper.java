package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.BreedDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Breed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BreedMapper {

    @Mapping(source = "speciesUuid", target = "species.uuid")
    Breed toBreed(BreedDTO breedDTO);

    @Mapping(source = "species.uuid", target = "speciesUuid")
    BreedDTO toBreedDto(Breed breed);

    List<Breed> toBreedList(List<BreedDTO> breedDTOs);

    List<BreedDTO> toBreedDTOList(List<Breed> breeds);

    Breed updateBreedFromDto(BreedDTO breedDTO, @MappingTarget Breed breed);


}
