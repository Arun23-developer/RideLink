package com.ridelink.authservice.repository;

import com.ridelink.authservice.entity.User;
import com.ridelink.authservice.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    List<User> findByRole(Role role);

    List<User> findAllByOrderByCreatedAtDesc();
}
