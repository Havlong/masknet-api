package ru.havlong.dnnback.services.impls;

import java.util.Objects;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.havlong.dnnback.models.NetResponseDto;
import ru.havlong.dnnback.models.ParamsDto;
import ru.havlong.dnnback.models.RequestDto;
import ru.havlong.dnnback.models.ResponseDto;
import ru.havlong.dnnback.repositories.NetworkRepository;
import ru.havlong.dnnback.services.NetworkService;
import ru.havlong.dnnback.utility.Messages;
import ru.havlong.dnnback.utility.Status;

@Service
public class NetworkServiceImpl implements NetworkService {

    @Autowired
    @Qualifier("networkTemplate")
    private NetworkRepository networkRepository;

    private Logger logger = LoggerFactory.getLogger(NetworkService.class);

    @Override
    @Nullable
    public ParamsDto askParams() {
        ResponseEntity<ParamsDto> params = null;
        try {
            params = networkRepository.getParams();
        } catch (Exception e) {
            logger.warn("Neural Network is unavailable!");
        }

        if (Objects.isNull(params) || params.getStatusCode().isError())
            return null;
        return params.getBody();
    }

    @Override
    @NonNull
    public ResponseDto sendRequest(RequestDto requestModel) {
        ResponseEntity<NetResponseDto> feedForward = null;
        try {
            feedForward = networkRepository.feedForward(requestModel);
        } catch (Exception e) {
            logger.warn("Neural Network is unavailable!");
        }
        if (Objects.isNull(feedForward)) {
            return new ResponseDto(null, Status.BACKEND_ERROR, Messages.DNN_UNAVAILABLE);
        }
        feedForward = Objects.requireNonNull(feedForward);
        if (feedForward.getStatusCode().is2xxSuccessful()) {
            NetResponseDto responseBody = Objects.requireNonNull(feedForward.getBody());
            return new ResponseDto(responseBody.probabilities(), Status.OK, null);
        }
        if (feedForward.getStatusCode().value() == 400) {
            return new ResponseDto(null, Status.BAD_PARAMS, Messages.BAD_PARAMS_DESC);
        }
        return new ResponseDto(null, Status.BACKEND_ERROR, Messages.DNN_UNAVAILABLE);
    }

    @Override
    @NonNull
    public Status retrainNetwork() {
        ResponseEntity<Void> trainingStatus = null;
        try {
            trainingStatus = networkRepository.askForRetrain();
        } catch (Exception e) {
            logger.warn("Neural Network is unavailable!");
        }
        if (Objects.nonNull(trainingStatus) && trainingStatus.getStatusCode().is2xxSuccessful())
            return Status.OK;
        return Status.BACKEND_ERROR;
    }

}
