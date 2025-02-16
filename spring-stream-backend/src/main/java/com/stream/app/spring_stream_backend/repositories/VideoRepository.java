package com.stream.app.spring_stream_backend.repositories;


import com.stream.app.spring_stream_backend.entities.video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<video,String> {
    Optional<video> findByTitle(String title);
}
