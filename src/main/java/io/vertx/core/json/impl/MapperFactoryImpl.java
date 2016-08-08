package io.vertx.core.json.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.MapperFactory;

public class MapperFactoryImpl implements MapperFactory {

	@Override
	public ObjectMapper createMapper() {
		ObjectMapper mapper = new ObjectMapper();
		configureCommonProperties(mapper);		
		return mapper;
	}

	@Override
	public ObjectMapper createPrettyMapper() {
		ObjectMapper prettyMapper = new ObjectMapper();
		configureCommonProperties(prettyMapper);
	    prettyMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		return prettyMapper;
	}

	protected void configureCommonProperties(ObjectMapper mapper) {
		// Non-standard JSON but we allow C style comments in our JSON
	    mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
	    // Vert.x module
	    SimpleModule module = new SimpleModule();
	    module.addSerializer(JsonObject.class, new JsonObjectSerializer());
	    module.addSerializer(JsonArray.class, new JsonArraySerializer());
	    module.addDeserializer(JsonObject.class, new JsonObjectDeSerializer());
	    module.addDeserializer(JsonArray.class, new JsonArrayDeSerializer());
	    mapper.registerModule(module);
	}
	
}
