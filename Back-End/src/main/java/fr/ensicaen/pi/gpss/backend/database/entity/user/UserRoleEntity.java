package fr.ensicaen.pi.gpss.backend.database.entity.user;

import fr.ensicaen.pi.gpss.backend.database.enumerate.Role;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Objects;

@Entity(name = "UserRole")
@Table(name="`UserRole`", schema = "`User`")
@Getter
@Setter
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="`idRole`")
    private Integer idRole;
    @Column(name="`RoleType`")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private Role roleType;
    @OneToOne
    @JoinColumn(name = "`idUserInformation`")
    private UserInformationEntity userInformation;

    public UserRoleEntity(Role role, UserInformationEntity userInformation){
        roleType = role;
        this.userInformation = userInformation;
    }

    public UserRoleEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleEntity that = (UserRoleEntity) o;
        return Objects.equals(idRole, that.idRole) &&
                roleType == that.roleType &&
                Objects.equals(userInformation, that.userInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRole, roleType, userInformation);
    }
}
