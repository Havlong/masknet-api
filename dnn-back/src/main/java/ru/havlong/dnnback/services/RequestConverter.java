package ru.havlong.dnnback.services;

import ru.havlong.dnnback.documents.Request;
import ru.havlong.dnnback.models.RequestDto;

public interface RequestConverter {
    public Request toDocument(RequestDto model);
    public RequestDto toModel(Request document);
}
