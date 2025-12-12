package au.com.library.shared.util;

import org.modelmapper.ModelMapper;

/**
 * Handles the mapping of one object to another, provided the attributes of the destination
 * type exactly matches that of the source.
 */
public final class Mapper {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private Mapper(){
    }

    /**
     * Map the data of the source object to the specified destination type.
     * @param source The source object.
     * @param destinationType The destination class literal type of the destination.
     * @return An instance of the destination type populated with the data of the source.
     * @param <S>
     * @param <D>
     */
    public static <S, D> D map(S source, Class<D> destinationType) {
        return MODEL_MAPPER.map(source, destinationType);
    }
}
