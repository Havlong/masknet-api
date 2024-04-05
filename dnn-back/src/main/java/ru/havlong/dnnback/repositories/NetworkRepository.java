package ru.havlong.dnnback.repositories;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import ru.havlong.dnnback.models.NetResponseDto;
import ru.havlong.dnnback.models.ParamsDto;
import ru.havlong.dnnback.models.RequestDto;

@HttpExchange(accept = { MediaType.APPLICATION_JSON_VALUE }, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface NetworkRepository {

    @GetExchange(url = "/params")
    ResponseEntity<ParamsDto> getParams();

    @GetExchange(url="/retrain")
    ResponseEntity<Void> askForRetrain();

    @PostExchange(url="/request")
    ResponseEntity<NetResponseDto> feedForward(@RequestBody RequestDto requestDto);

}
