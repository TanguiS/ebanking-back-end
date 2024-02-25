package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.payload.request.UserRegistrationForm;
import fr.ensicaen.pi.gpss.backend.tools.security.PasswordEncoderBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@DependsOn("isAllowed")
public class UserFormComponent {
    @Value("${be.ebanking.app.password.master}")
    private String masterPassword;
    @Value("${be.ebanking.app.password.collector}")
    private String collectorPassword;
    @Value("${be.ebanking.app.password.simulator}")
    private String simulatorPassword;
    @Value("${be.ebanking.app.password.banker}")
    private String bankerPassword;
    @Value("${be.ebanking.app.password.accountant}")
    private String accountantPassword;
    @Value("${be.ebanking.app.password.client.babylon}")
    private String babylonPassword;
    @Value("${be.ebanking.app.password.client.babylonJunior}")
    private String babylonJuniorPassword;
    private final PasswordEncoder passwordEncoder;

    public UserFormComponent(PasswordEncoderBean passwordEncoderBean) {
        passwordEncoder = passwordEncoderBean.getPasswordEncoder();
    }

    @Bean
    public UserRegistrationForm getMasterForm() {
        return new UserRegistrationForm(
                "Master", "Unlimited", "master@ensibank.pro.fr",
                passwordEncoder.encode(masterPassword),
                0, "0700000000"
        );
    }

    @Bean
    public UserRegistrationForm getTransactionCollectorForm() {
        return new UserRegistrationForm(
                "FrontEnd", "Collect", "front.end.collect@ensibank.pro.fr",
                passwordEncoder.encode(collectorPassword),
                0, "0700000004"
        );
    }

    @Bean
    public UserRegistrationForm getSimulatorForm() {
        return new UserRegistrationForm(
                "Henry", "Simulate", "henry.simulate@ensibank.pro.fr",
                passwordEncoder.encode(simulatorPassword),
                0, "0700000003"
        );
    }

    @Bean
    public UserRegistrationForm getBankerForm() {
        return new UserRegistrationForm(
                "Alexandrie", "King", "alexandrie.king@ensibank.pro.fr",
                passwordEncoder.encode(bankerPassword),
                0, "0700000001"
        );
    }

    @Bean
    public UserRegistrationForm getAccountantForm() {
        return new UserRegistrationForm(
                "Caesar", "Emperor", "ceasar.emperor@ensibank.pro.fr",
                passwordEncoder.encode(accountantPassword),
                0, "0700000002"
        );
    }

    @Bean
    public UserRegistrationForm getBabylonClientForm() {
        return new UserRegistrationForm(
                "Babylon", "Snow", "babylon@gmail.com",
                passwordEncoder.encode(babylonPassword),
                0, "+33698746458"
        );
    }

    @Bean
    public UserRegistrationForm getBabylonJuniorClientForm() {
        return new UserRegistrationForm(
                "BabylonJunior", "Snow", "babylon.junior@gmail.com",
                passwordEncoder.encode(babylonJuniorPassword),
                0, "+33798458958"
        );
    }
}
