package ru.havlong.dnnback.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import ru.havlong.dnnback.documents.Request;

public interface RequestRepository extends MongoRepository<Request, Long> {

    public long countByFinishedAtNotNull();

    public long countByIdBetweenAndFinishedAtNotNull(long before, long after);

}
