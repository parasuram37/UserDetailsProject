package com.task.www.service;

import com.task.www.entity.User;
import com.task.www.exception.CustomException;
import com.task.www.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        validateUserInput(user);
        user.setPanNum(user.getPanNum().toUpperCase());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public List<User> getUsers(Optional<String> mobNum, Optional<UUID> userId, Optional<UUID> managerId) {
        if (mobNum.isPresent()) {
            return userRepository.findByMobNum(mobNum.get()).map(List::of).orElse(List.of());
        } else if (userId.isPresent()) {
            return userRepository.findById(userId.get()).map(List::of).orElse(List.of());
        } else if (managerId.isPresent()) {
            return userRepository.findByManagerId(managerId.get());
        } else {
            return userRepository.findAll();
        }
    }

    public void deleteUser(Optional<UUID> userId, Optional<String> mobNum) {
        if (userId.isPresent()) {
            userRepository.deleteById(userId.get());
        } else if (mobNum.isPresent()) {
            userRepository.findByMobNum(mobNum.get()).ifPresent(userRepository::delete);
        } else {
            throw new CustomException("Either userId or mobNum must be provided");
        }
    }

    public String updateUser(List<UUID> userIds, User updateData) {
        for (UUID userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("User not found with id: " + userId));

            if (updateData.getFullName() != null) {
                user.setFullName(updateData.getFullName());
            }
            if (updateData.getMobNum() != null) {
                user.setMobNum(updateData.getMobNum());
            }
            if (updateData.getPanNum() != null) {
                user.setPanNum(updateData.getPanNum().toUpperCase());
            }
            if (updateData.getManagerId() != null) {
                user.setManagerId(updateData.getManagerId());
            }
            user.setUpdatedAt(LocalDateTime.now());

            userRepository.save(user);
        }
        return "Users updated successfully";
    }

    private void validateUserInput(User user) {
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            throw new CustomException("Full name is required");
        }
        if (!user.getMobNum().matches("^\\+?91?[0]?[6789]\\d{9}$")) {
            throw new CustomException("Invalid mobile number format");
        }
        if (!user.getPanNum().matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
            throw new CustomException("Invalid PAN number format");
        }
    }
}