package org.zerock.memberboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.memberboard.entity.MovieImage;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {
}
