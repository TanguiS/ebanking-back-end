package fr.ensicaen.pi.gpss.backend.database.repository.user;

import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformationEntity, Integer> {

    Optional<UserInformationEntity> findByEmailIsLikeIgnoreCase(
            @Valid @NotBlank @Email @Param("email") String email
    );

    @Query(
            "SELECT user FROM UserInformation user " +
            "LEFT JOIN FETCH user.role " +
            "LEFT JOIN FETCH user.accountsManager " +
            "WHERE lower(user.email) = lower(:email)"
    )
    UserInformationEntity findByEmailIsLikeIgnoreCaseWithAllProperties(
            @Valid @NotBlank @Email @Param("email") String email
    );

    int countAllByEmailIsLikeIgnoreCase(
            @Valid @NotBlank @Email @Param("email") String email
    );
}
