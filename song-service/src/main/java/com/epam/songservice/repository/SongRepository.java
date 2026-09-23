package com.epam.songservice.repository;

import com.epam.songservice.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SongRepository extends JpaRepository<Song, Integer> {
    @Modifying
    @Query(value = """
            INSERT INTO songs (id, name, artist, album, duration, year)
            VALUES (:id, :name, :artist, :album, :duration, :year)
            ON CONFLICT (id) DO NOTHING
            """, nativeQuery = true)
    int insert(@Param("id") Integer id, @Param("name") String name,
               @Param("artist") String artist, @Param("album") String album,
               @Param("duration") String duration, @Param("year") String year);
}
