package au.com.library.shared.mapper;

/**
 * A simple mapper interface for converting between Entity and DTO objects. This will be used as a base interface for specific mappers in the application, allowing for consistent mapping methods across different entities and DTOs.
 * This should only be used for simple mappings where the field names and types in the Entity and DTO are the same.
 * <p>The implementation of this interface at compile time, will use a framework such as MapStruct to generate the mapping code based on the method signatures defined in this interface and the structure of the source and target classes.</p>
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