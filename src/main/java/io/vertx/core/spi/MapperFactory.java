package io.vertx.core.spi;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface MapperFactory {

	ObjectMapper createMapper();
	ObjectMapper createPrettyMapper();
	
}
