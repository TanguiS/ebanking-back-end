package fr.ensicaen.pi.gpss.backend.database.mapper;


public interface DTOEntityMapper<D, E> {
    D toDTO(E entity);

    E toEntity(D dto);
}
