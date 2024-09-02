package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.BreedDTO;
import com.Japkutija.veterinarybackend.veterinary.model.dto.UserDto;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Breed;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "uuid", target = "uuid")
    User toUser(UserDto userDto);

    @Mapping(source = "user.uuid", target = "uuid")
    UserDto toUserDto(User user);

    List<User> toUserList(List<UserDto> userDtos);

    List<UserDto> toUserDtoList(List<User> users);

    User updateUserFromDto(UserDto userDto, @MappingTarget User user);
}
