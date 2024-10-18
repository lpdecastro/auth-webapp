package com.lpdecastro.authwebapp.util;

import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LoginModelMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public UserEntity convertDtoToEntity(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }

    public UserDto convertEntityToDto(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }
}
