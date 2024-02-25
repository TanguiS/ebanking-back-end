package fr.ensicaen.pi.gpss.backend.database.entity.account;

import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "AccountManager")
@Table(name = "`AccountManager`", schema = "`Account`")
@Getter
@Setter
public class AccountManagerEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "`idAccountManager`")
    private Integer idAccountManager;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idClientAccount`")
    private ClientAccountEntity clientAccount;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idBankerAccount`")
    private BankerAccountEntity bankerAccount;
    @ManyToOne
    @JoinColumn(name = "`idUserInformation`")
    private UserInformationEntity usersInformation;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idATMAccount`")
    private ATMAccountEntity atmAccount;

    public AccountManagerEntity(
            ClientAccountEntity clientAccount,
            BankerAccountEntity bankerAccount,
            UserInformationEntity usersInformation,
            ATMAccountEntity atmAccount
    ) {
        this.clientAccount = clientAccount;
        this.bankerAccount = bankerAccount;
        this.usersInformation = usersInformation;
        this.atmAccount = atmAccount;
    }

    public AccountManagerEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountManagerEntity that = (AccountManagerEntity) o;
        return Objects.equals(idAccountManager, that.idAccountManager) &&
                Objects.equals(clientAccount, that.clientAccount) &&
                Objects.equals(bankerAccount, that.bankerAccount) &&
                Objects.equals(usersInformation, that.usersInformation) &&
                Objects.equals(atmAccount, that.atmAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAccountManager, clientAccount, bankerAccount, usersInformation, atmAccount);
    }
}
