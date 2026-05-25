package com.restaurent.RMS.services;
import com.restaurent.RMS.entities.User;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.UserMapper;
import com.restaurent.RMS.repositories.UserRepository;
import com.restaurent.RMS.specification.UserSpecis;
import com.restaurent.RMS.utils.PasswordGenerator;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.restaurent.RMS.dtos.request.UserRequestDto;
import com.restaurent.RMS.dtos.response.UserResponseDto;
import com.restaurent.RMS.entities.Restaurant;
import com.restaurent.RMS.entities.Role;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.exceptionHandlers.AlreadyExistException;
import com.restaurent.RMS.exceptionHandlers.RequiredDataMissingException;
import com.restaurent.RMS.repositories.RestaurantRepository;
import com.restaurent.RMS.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    public final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService credentialEmailService;



    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findByIdWithRoleAndRestaurant(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User " + RestApiResponseStatusCodes.NOT_FOUND.getMessage()
                ));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto createUser(Long restaurantId, Long roleId, UserRequestDto dto) {


        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AlreadyExistException("Email Already Exists");
        }
        if (userRepository.existsByNic(dto.getNic())) {
            throw new AlreadyExistException("NIC Already Exists");
        }

        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new AlreadyExistException("PhoneNumber Already Exists");
        }


        dto.setRestaurantId(restaurantId);
        dto.setRoleId(roleId);

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not Found"));

        String autoPassword = PasswordGenerator.generatePassword(10);
        credentialEmailService.sendUserCredentialEmail(dto.getEmail(), autoPassword);

        User user = userMapper.toEntity(dto, role);
        user.setPassword(bCryptPasswordEncoder.encode(autoPassword));
        User savedUser = userRepository.save(user);
        UserResponseDto result = userMapper.toDto(savedUser);
        result.setRoleId(roleId);
        return result;
    }

    @Override
    public void User_deleteById(Long id) {
        User user=userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        userRepository.delete(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        validateUserFields(userRequestDto);
        validateRequiredFields(userRequestDto);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Role role = roleRepository.findById(userRequestDto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (userRepository.existsByEmailAndIdNot(userRequestDto.getEmail(), id))
            throw new AlreadyExistException("Email already exists");

        if (userRepository.existsByNicAndIdNot(userRequestDto.getNic(), id))
            throw new AlreadyExistException("NIC already exists");

        if (userRepository.existsByPhoneNumberAndIdNot(userRequestDto.getPhoneNumber(), id))
            throw new AlreadyExistException("Phone number already exists");

        userMapper.updateUserFromDto(userRequestDto, user);
        user.setRole(role);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    private void validateUserFields(UserRequestDto userRequestDto) {
        List<String> errors = new ArrayList<>();

        if (userRequestDto.getEmail() == null ||
                !userRequestDto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.add("Invalid email format");
        }

        if (userRequestDto.getNic() == null ||
                !userRequestDto.getNic().matches("^(\\d{9}[VvXx]|\\d{12})$")) {
            errors.add("Invalid NIC number");
        }

        if (userRequestDto.getPhoneNumber() == null ||
                !userRequestDto.getPhoneNumber().matches("^(0\\d{9}|\\+\\d{1,3}\\d{4,14})$")) {
            errors.add("Invalid phone number");
        }

        // If any format errors → throw all together
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
    }

    private void validateRequiredFields(UserRequestDto dto) {

        if (dto.getFirstName() == null || dto.getFirstName().isEmpty() ||
                dto.getLastName() == null || dto.getLastName().isEmpty() ||
                dto.getEmail() == null || dto.getEmail().isEmpty() ||
                dto.getNic() == null || dto.getNic().isEmpty() ||
                dto.getCity() == null || dto.getCity().isEmpty() ||
                dto.getAddress() == null || dto.getAddress().isEmpty() ||
                dto.getPhoneNumber() == null || dto.getPhoneNumber().isEmpty()) {

            throw new RequiredDataMissingException("Missing parameter: Required fields cannot be empty");
        }

        if (dto.getRoleId() == null) {
            throw new RequiredDataMissingException("roleId is required");
        }
    }

    @Override
    public Page<UserResponseDto> searchUsers(Long restaurantId, Long roleId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;

        if (roleId == null) {
            userPage = userRepository.findByRole_Restaurant_Id(restaurantId, pageable);
        } else {
            userPage = userRepository.findByRole_Restaurant_IdAndRole_Id(
                    restaurantId, roleId, pageable
            );
        }

        if (page < 0 || size < 0 || restaurantId < 0 || (roleId != null && roleId < 0)) {
            throw new ConstraintViolationException(ValidationMessages.CONSTRAINT_VIOLATION,null);
        }

        if (userPage.isEmpty()) {
            throw new ResourceNotFoundException("No users found for restaurantId " + restaurantId +
                    (roleId != null ? " and roleId " + roleId : ""));
        }
        return userPage.map(userMapper::toDto);
    }

    @Override
    public List<UserResponseDto> searchUser(String query) {
        List<User> users = userRepository.findAll(UserSpecis.search(query));
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

//
//    @Override
//    public List<UserResponseDto> getUsersByRoleName(Long restaurantId, String roleName) {
//
//        Role role = roleRepository
//                .findByRoleNameIgnoreCaseAndRestaurant_Id(roleName, restaurantId)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Role not found: " + roleName)
//                );
//
//        List<User> users = userRepository
//                .findByRole_IdAndRole_Restaurant_Id(role.getId(), restaurantId);
//
//        if (users.isEmpty()) {
//            throw new ResourceNotFoundException("No stewards found");
//        }
//
//        return users.stream()
//                .map(userMapper::toDto)
//                .toList();
//    }
//
//

}
