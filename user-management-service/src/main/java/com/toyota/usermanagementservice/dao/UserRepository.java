package com.toyota.usermanagementservice.dao;

import com.toyota.usermanagementservice.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing users table
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("select u from User u where UPPER(u.firstname) like concat('%',UPPER(?1),'%')" +
            "and Upper(u.lastname) like concat('%',Upper(?2),'%') and Upper(u.email) like " +
            "concat('%',UPPER(?3),'%') and upper(u.username) like concat('%',UPPER(?4),'%') ")
    Page<User> getUsersFiltered(String firstname, String lastName, String email, String username, Pageable pageable);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
