package com.github.regiadi.springboot_playground.user.dto;

import com.github.regiadi.springboot_playground.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	public User toUser(CreateUserRequestDTO dto) {
		if (dto == null) {
			return null;
		}
		// The password from the DTO is raw and will be encoded by the service
		return new User(dto.getUsername(), dto.getEmail(), dto.getPassword());
	}

	public UserResponseDTO toUserResponseDTO(User user) {
		if (user == null) {
			return null;
		}
		UserResponseDTO dto = new UserResponseDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setCreatedAt(user.getCreatedAt());
		dto.setUpdatedAt(user.getUpdatedAt());
		return dto;
	}
}