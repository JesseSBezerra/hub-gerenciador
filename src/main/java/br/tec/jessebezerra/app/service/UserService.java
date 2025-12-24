package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.UserRequestDTO;
import br.tec.jessebezerra.app.dto.UserResponseDTO;
import br.tec.jessebezerra.app.model.entity.User;
import br.tec.jessebezerra.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO save(UserRequestDTO dto) {
        User user = new User();
        user.setNome(dto.getNome());
        user.setFuncao(dto.getFuncao());
        
        User savedUser = userRepository.save(user);
        return toResponseDTO(savedUser);
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(this::toResponseDTO);
    }

    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setNome(dto.getNome());
                    existingUser.setFuncao(dto.getFuncao());
                    User updatedUser = userRepository.save(existingUser);
                    return toResponseDTO(updatedUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setFuncao(user.getFuncao());
        return dto;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
