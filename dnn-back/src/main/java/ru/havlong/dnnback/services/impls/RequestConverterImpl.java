package ru.havlong.dnnback.services.impls;

import org.springframework.stereotype.Service;

import ru.havlong.dnnback.documents.Request;
import ru.havlong.dnnback.models.RequestDto;
import ru.havlong.dnnback.services.RequestConverter;
import ru.havlong.dnnback.services.SnowflakeProvider;

@Service
public class RequestConverterImpl implements RequestConverter {

    private SnowflakeProvider snowflakeProvider;

    public RequestConverterImpl(SnowflakeProvider snowflakeProvider) {
        this.snowflakeProvider = snowflakeProvider;
    }

    @Override
    public Request toDocument(RequestDto model) {
        return new Request(snowflakeProvider.generateSnowflake(), null, model.embeddedParams(),
                model.numericalParams());
    }

    @Override
    public RequestDto toModel(Request document) {
        return new RequestDto(document.getEmbeddedParams(), document.getNumericalParams());
    }
}
