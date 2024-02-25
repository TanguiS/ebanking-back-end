package fr.ensicaen.pi.gpss.backend.database.repository.user;

import fr.ensicaen.pi.gpss.backend.database.entity.user.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {
}
