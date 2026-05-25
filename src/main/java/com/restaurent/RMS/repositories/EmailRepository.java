package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface  EmailRepository  extends JpaRepository <Email,Long> {
    boolean existsBySentEmail(String sentEmail);

    Optional<Email> findBySentEmail(String sentEmail);

    Optional<Email> findByHostName(String hostName);

    Optional<Email> findFirstByOrderByIdAsc();
}
