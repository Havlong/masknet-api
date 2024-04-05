package ru.havlong.dnnback.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import ru.havlong.dnnback.models.ParamsDto;
import ru.havlong.dnnback.models.RequestDto;
import ru.havlong.dnnback.models.ResponseDto;
import ru.havlong.dnnback.utility.Status;

public interface NetworkService {

    @Nullable
    public ParamsDto askParams();

    @NonNull
    public ResponseDto sendRequest(RequestDto requestModel);

    @NonNull
    public Status retrainNetwork();
}
