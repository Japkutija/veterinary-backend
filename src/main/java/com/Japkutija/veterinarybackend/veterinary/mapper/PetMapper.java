package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.PetDTO;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.PetWithSpeciesAndBreedDto;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(source = "owner.uuid", target = "ownerUuid")
    @Mapping(source = "breed.breedName", target = "breedName")
    @Mapping(source = "species.speciesName", target = "speciesName")
    @Mapping(source = "owner", target = "ownerName", qualifiedByName = "mapOwnerName")
    PetWithSpeciesAndBreedDto toPetWithSpeciesAndBreedDto(Pet pet);

    @Named("mapOwnerName")
    default String mapOwnerName(Owner owner) {
        return owner.getFirstName() + " " + owner.getLastName();
    }
    PetWithSpeciesAndBreedDto fromPetDto(PetDTO petDTO);

    //@Mapping(source = "speciesName", target = "")
    PetDTO fromPetWithSpeciesAndBreedDto(PetWithSpeciesAndBreedDto petWithSpeciesAndBreedDto);

    @Mapping(source = "owner.uuid", target = "ownerUuid")
    @Mapping(source = "breed.uuid", target = "breedUuid")
    @Mapping(source = "species.uuid", target = "speciesUuid")
    PetDTO toPetDTO(Pet pet);

    @Mapping(source = "ownerUuid", target = "owner.uuid")
    @Mapping(source = "breedUuid", target = "breed.uuid")
    @Mapping(source = "speciesUuid", target = "species.uuid")
    Pet toPet(PetDTO petDTO);

    List<PetDTO> toPetDTOList(List<Pet> pets);

    List<Pet> toPetList(List<PetDTO> petDTOs);

    @Mapping(source = "speciesName", target = "species.speciesName")
    @Mapping(source = "breedName", target = "breed.breedName")
    Pet updatePetFromDto(PetDTO petDTO, @MappingTarget Pet pet);


}

