package fr.ensicaen.pi.gpss.backend.database.entity.user;

import fr.ensicaen.pi.gpss.backend.database.entity.account.AccountManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Status;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "UserInformation")
@Table(name="`UserInformation`", schema = "`User`")
@Getter
@Setter
public class UserInformationEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "`idUser`")
    private Integer idUser;
    @Column(name = "`FirstName`")
    private String firstName;
    @Column(name = "`LastName`")
    private String lastName;
    @Column(name="`Password`")
    private String password;
    @Column(name="`Email`")
    private String email;
    @Column(name ="`Gender`")
    private int gender;
    @Column(name ="`RequestStatus`")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private Status requestStatus;
    @Column(name = "`PhoneNumber`")
    private String phoneNumber;
    @OneToOne(mappedBy = "userInformation", cascade = CascadeType.ALL)
    private UserRoleEntity role;
    @OneToMany(mappedBy = "usersInformation")
    private List<AccountManagerEntity> accountsManager = new ArrayList<>();

    public UserInformationEntity(
            String firstName,
            String lastName,
            String password,
            String email,
            int gender,
            Status requestStatus,
            String phoneNumber,
            UserRoleEntity role,
            List<AccountManagerEntity> accountsManager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.requestStatus = requestStatus;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.accountsManager = accountsManager;
    }

    public UserInformationEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInformationEntity that = (UserInformationEntity) o;
        return gender == that.gender &&
                Objects.equals(idUser, that.idUser) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(password, that.password) &&
                Objects.equals(email, that.email) &&
                requestStatus == that.requestStatus &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(role, that.role) &&
                Objects.equals(accountsManager, that.accountsManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idUser, firstName, lastName, password, email, gender, requestStatus, phoneNumber, role, accountsManager
        );
    }
}