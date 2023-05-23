package com.toyota.terminalservice.dao;

import com.toyota.terminalservice.domain.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TerminalRepository extends JpaRepository<Terminal,Long> {
    List<Terminal> findAllByIsActiveIsTrue();
}
