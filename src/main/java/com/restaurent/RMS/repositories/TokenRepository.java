package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);

    Token findTopByUserIdAndTypeOrderByCreateAtDesc(long id, String otp);

    Optional<Token> findByToken(String tokenValue);

    @Query("""
    select t from Token t where t.user.id = :userId and t.revoked = false
""")
    List<Token> findAllActiveTokensByUser(Long userId);


}
