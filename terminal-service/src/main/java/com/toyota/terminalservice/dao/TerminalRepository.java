package com.toyota.terminalservice.dao;

import com.toyota.terminalservice.domain.Terminal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Terminal
 */
@Repository
public interface TerminalRepository extends JpaRepository<Terminal,Long> {
    @Query("Select t from Terminal t where (Upper(t.depName) like concat('%',upper(?1) ,'%') or t.depName is null)" +
            " and t.isActive=(?2)")
    Page<Terminal> getTerminalsFiltered(String name, boolean isActive, Pageable pageable);

    Optional<Terminal> findByDepCode(String depCode);
}
