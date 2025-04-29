package ru.otus.mapper;

public interface Mapper<Entity, Rq, Rs> {
    Entity toEntity(Rq request);

    Rs toResponse(Entity entity);
}
