
package com.dhij.app.com.dhij.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dhij.app.com.dhij.app.model.Helper;
import com.dhij.app.com.dhij.app.model.User;

@Repository
public interface HelperRepository extends JpaRepository<Helper, Long> {
    // ❌ Do NOT put findByUsername here.
    // Keep this repository ONLY for Helper entity methods.
	//Optional<User> findByUsername(String username);
}
