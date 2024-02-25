package fr.ensicaen.pi.gpss.backend.database.entity.account;

import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "BankerAccount")
@Table(name = "`BankerAccount`", schema = "`Account`")
@Getter
@Setter
public class BankerAccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idBankerAccount`")
    private Integer idBankerAccount;
    @OneToOne
    @JoinColumn(name = "`idUser`")
    private UserInformationEntity userInformation;

    public BankerAccountEntity(UserInformationEntity userInformation) {
        this.userInformation = userInformation;
    }

    public BankerAccountEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankerAccountEntity that = (BankerAccountEntity) o;
        return Objects.equals(idBankerAccount, that.idBankerAccount) &&
                Objects.equals(userInformation, that.userInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBankerAccount, userInformation);
    }
}
