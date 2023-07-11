package com.toyota.verificationauthorizationservice.dao;

import com.toyota.verificationauthorizationservice.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token,String> {
    @Query("select t from Token t inner join User u on t.user.id=u.id " +
            "where u.id=?1 and (t.revoked=false or t.expirationDate>CURRENT_TIMESTAMP)")
    List<Token> findAllValidTokensByUser(Long userId);
}
