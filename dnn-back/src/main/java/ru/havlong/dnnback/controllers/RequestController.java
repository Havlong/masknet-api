package ru.havlong.dnnback.controllers;

import java.time.Instant;
import java.util.Objects;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import ru.havlong.dnnback.documents.Request;
import ru.havlong.dnnback.models.ParamsDto;
import ru.havlong.dnnback.models.RequestDto;
import ru.havlong.dnnback.models.ResponseDto;
import ru.havlong.dnnback.repositories.RequestRepository;
import ru.havlong.dnnback.services.NetworkService;
import ru.havlong.dnnback.services.RequestConverter;
import ru.havlong.dnnback.utility.Messages;
import ru.havlong.dnnback.utility.Status;

@AllArgsConstructor
@RestController
@RequestMapping("/net")
public class RequestController {

    RequestConverter requestConverter;
    RequestRepository requestRepository;
    NetworkService networkService;

    @PostMapping(path = "/request", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseDto askNetwork(@RequestBody RequestDto requestModel) {
        Request request = requestConverter.toDocument(requestModel);
        requestRepository.save(request);

        ResponseDto netResponse = networkService.sendRequest(requestModel);
        if (netResponse.status().equals(Status.OK)) {
            request.setFinishedAt(Instant.now());
            requestRepository.save(request);
        }

        return netResponse;
    }

    @GetMapping(path = "/params", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ParamsDto askParams() {
        ParamsDto networkParams = networkService.askParams();
        if (Objects.isNull(networkParams)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(502), Messages.DNN_UNAVAILABLE);
        }
        return networkParams;
    }

    @GetMapping(path = "/retrain", produces = { MediaType.APPLICATION_JSON_VALUE })
    public void retrainNetwork() {
        Status response = networkService.retrainNetwork();
        switch (response) {
            case BACKEND_ERROR:
                throw new ResponseStatusException(HttpStatusCode.valueOf(502), Messages.DNN_UNAVAILABLE);
            default:
                break;
        }
    }

    @GetMapping(path = "/successful", produces = { MediaType.TEXT_PLAIN_VALUE })
    public String countSuccessfulRequests() {
        return Long.toString(requestRepository.countByFinishedAtNotNull());
    }

}
