package fr.xebia.extras.it.mappers;

import fr.xebia.extras.beans.PersonOut;
import fr.xebia.extras.beans.PersonIn;
import fr.xebia.extras.mapper.Mapper;

/**
 *
 */
@Mapper(ignoreMissingProperties = true)
public interface BeanMapper {

    PersonOut convertFrom(PersonIn in);

}