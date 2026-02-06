package au.com.library.shared.mapper;

/**
 * A simple mapper interface for converting between Entity and DTO objects.
 *
 * @param <E> the type of the Entity
 * @param <D> the type of the DTO
 */
public interface SimpleMapper<E, D> {

    /**
     * Converts an Entity to a DTO.
     *
     * @param entity the Entity to convert
     * @return the converted DTO
     */
    D toDTO(E entity);
    /**
     * Converts a DTO to an Entity.
     *
     * @param dto the DTO to convert
     * @return the converted Entity
     */
    E toEntity(D dto);
}