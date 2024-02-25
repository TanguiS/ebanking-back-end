package fr.ensicaen.pi.gpss.backend.database.repository.account;

import fr.ensicaen.pi.gpss.backend.database.entity.account.BankerAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankerAccountRepository extends JpaRepository<BankerAccountEntity, Integer> {
    @Query(
            "SELECT manager.bankerAccount.userInformation FROM AccountManager manager " +
            "WHERE manager.usersInformation = :banker"
    )
    List<UserInformationEntity> findAllClientByBankerUser(
            @Valid @NotNull @Param("banker") UserInformationEntity banker
    );
}
