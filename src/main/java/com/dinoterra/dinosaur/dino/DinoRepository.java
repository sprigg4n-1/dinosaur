package com.dinoterra.dinosaur.dino;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DinoRepository extends JpaRepository<Dino, Long> {
    
}
