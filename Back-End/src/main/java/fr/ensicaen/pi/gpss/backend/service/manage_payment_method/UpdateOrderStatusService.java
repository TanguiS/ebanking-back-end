package fr.ensicaen.pi.gpss.backend.service.manage_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestPaymentMethodStatusDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method.RequestPaymentMethodStatusMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.request_payment_method.RequestPaymentMethodStatusRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UpdateOrderStatusService {
    private final RequestPaymentMethodStatusMapper requestPaymentMethodStatusMapper;
    private final RequestPaymentMethodStatusRepository requestPaymentMethodStatusRepository;

    public UpdateOrderStatusService(
            RequestPaymentMethodStatusMapper requestPaymentMethodStatusMapper,
            RequestPaymentMethodStatusRepository requestPaymentMethodStatusRepository
    ) {
        this.requestPaymentMethodStatusMapper = requestPaymentMethodStatusMapper;
        this.requestPaymentMethodStatusRepository = requestPaymentMethodStatusRepository;
    }

    @Transactional
    public RequestPaymentMethodStatusDTO onRequestPaymentMethod(
            @Valid @NotNull RequestPaymentMethodStatusDTO requestPaymentMethodStatus
    ) {
        return requestPaymentMethodStatusMapper.toDTO(
                requestPaymentMethodStatusRepository.save(
                    requestPaymentMethodStatusMapper.toEntity(
                            requestPaymentMethodStatusMapper.toUpdateOnRequest(
                                    requestPaymentMethodStatus
                            )
                    )
                )
        );
    }

    @Transactional
    public RequestPaymentMethodStatusDTO onReceivedPaymentMethodByBank(
            @Valid @NotNull RequestPaymentMethodStatusDTO requestPaymentMethodStatus
    ) {
        return requestPaymentMethodStatusMapper.toDTO(
                requestPaymentMethodStatusRepository.save(
                        requestPaymentMethodStatusMapper.toEntity(
                                requestPaymentMethodStatusMapper.toUpdateOnReceivedByBank(
                                        requestPaymentMethodStatus
                                )
                        )
                )
        );
    }

    @Transactional
    public RequestPaymentMethodStatusDTO onReceivedPaymentMethodByUser(
            @Valid @NotNull RequestPaymentMethodStatusDTO requestPaymentMethodStatus
    ) {
        return requestPaymentMethodStatusMapper.toDTO(
                requestPaymentMethodStatusRepository.save(
                    requestPaymentMethodStatusMapper.toEntity(
                            requestPaymentMethodStatusMapper.toUpdateOnReceivedByUser(
                                    requestPaymentMethodStatus
                            )
                    )
                )
        );
    }
}
