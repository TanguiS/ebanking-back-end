package fr.ensicaen.pi.gpss.backend.payload.response;

import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.security.jwt.TokenFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class SingleResponseBuilder {
    private final Map<String, Object> requestResponseMap;
    private RequestID request;
    private TemplateDTO dataDTO;
    private String dataString;
    private List<TemplateDTO> dataDTOs;
    private final Map<String, Object> responseWrapperMap;
    private HttpStatus status;
    private JSONObject requestResponseJson;
    private HttpHeaders headers;

    public SingleResponseBuilder() {
        requestResponseMap = new HashMap<>();
        responseWrapperMap = new HashMap<>();
        dataDTOs = new ArrayList<>();
    }

    private void resetValues() {
        request = null;
        dataDTO = null;
        dataString = null;
        status = null;
        requestResponseJson = null;
        headers = null;
        requestResponseMap.clear();
        responseWrapperMap.clear();
        dataDTOs.clear();
    }

    public SingleResponseBuilder setData(@NotNull TemplateDTO data) {
        dataDTO = data;
        return this;
    }

    public SingleResponseBuilder setData(@NotEmpty List<TemplateDTO> data) {
        dataDTOs = new ArrayList<>(data);
        return this;
    }

    public SingleResponseBuilder setData(@Nullable String data) {
        dataString = data;
        return this;
    }

    public SingleResponseBuilder setBearerAccessToken(@Nullable String accessToken) {
        if (accessToken == null) {
            return this;
        }
        headers = new HttpHeaders();
        headers.set(TokenFormat.BEARER.key(), TokenFormat.BEARER.prefix() + accessToken);
        return this;
    }

    public SingleResponseBuilder setRequest(RequestID request) {
        this.request = request;
        return this;
    }

    public SingleResponseBuilder setLoginRequest() {
        return setRequest(RequestID.LOGIN);
    }

    public SingleResponseBuilder setRegisterRequest() {
        return setRequest(RequestID.REGISTER);
    }

    public SingleResponseBuilder setCollectedTransactionRequest() {
        return setRequest(RequestID.COLLECTED_TRANSACTION);
    }

    public SingleResponseBuilder setGenerateTransactionRequest() {
        return setRequest(RequestID.GENERATE_TRANSACTION);
    }

    public SingleResponseBuilder setSimulateTransactionCollectionRequest() {
        return setRequest(RequestID.SIMULATE_TRANSACTION_COLLECTION);
    }

    public SingleResponseBuilder setUnresolvedMappingRequest() {
        return setRequest(RequestID.UNRESOLVED_MAPPING);
    }

    public SingleResponseBuilder setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public SingleResponseBuilder setOkStatus() {
        return setStatus(HttpStatus.OK);
    }

    public SingleResponseBuilder setAcceptedStatus() {
        return setStatus(HttpStatus.ACCEPTED);
    }

    public SingleResponseBuilder setUnauthorizedStatus() {
        return setStatus(HttpStatus.UNAUTHORIZED);
    }

    private void fillRequestResponseMap() throws IllegalArgumentException {
        requestResponseMap.put("requestId", request.id());
        requestResponseMap.put("requestName", request.label());
        requestResponseMap.put("requestDescription", request.description());
        if (dataDTO != null) {
            requestResponseMap.put("data", dataDTO);
        } else if (!dataDTOs.isEmpty()) {
            requestResponseMap.put("data", dataDTOs);
        } else {
            requestResponseMap.put("data", dataString);
        }
    }

    private void fillResponseWrapperMap() throws IllegalArgumentException {
        responseWrapperMap.put("status", status.value());
        responseWrapperMap.put("message", status.getReasonPhrase());
        responseWrapperMap.put("timestamp", new Timestamp(new Date().getTime()));
        responseWrapperMap.put("interactionResponse", requestResponseJson);
    }

    public ResponseEntity<String> build() {
        if (request == null) {
            throw new IllegalArgumentException("RequestID aka request can not be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("RequestID aka request can not be null");
        }
        HttpStatus savedStatus = this.status;
        HttpHeaders savedHeaders = this.headers;
        fillRequestResponseMap();
        requestResponseJson = new JSONObject(requestResponseMap);
        fillResponseWrapperMap();
        String body = new JSONObject(responseWrapperMap).toString();
        resetValues();
        return ResponseEntity
                .status(savedStatus)
                .headers(savedHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(body.length())
                .body(body);
    }
}
