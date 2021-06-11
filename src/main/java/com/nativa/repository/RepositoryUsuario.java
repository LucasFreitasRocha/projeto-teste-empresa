package com.nativa.repository;

import com.nativa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryUsuario extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
}
