package com.jhrq.springbackend.service;

import com.jhrq.springbackend.dto.UserResponseDTO;
import com.jhrq.springbackend.model.User;
import com.jhrq.springbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> findById(Long id) {
        return userRepository.findById(id).map(this::toDTO);
    }

    public UserResponseDTO create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return toDTO(userRepository.save(user));
    }

    public Optional<UserResponseDTO> update(Long id, User updated) {
        return userRepository.findById(id).map(user -> {
            user.setName(updated.getName());
            user.setEmail(updated.getEmail());
            user.setPassword(passwordEncoder.encode(updated.getPassword()));
            return toDTO(userRepository.save(user));
        });
    }

    public boolean delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}