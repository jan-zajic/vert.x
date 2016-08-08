package io.vertx.core.json.impl;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import io.vertx.core.json.JsonObject;

public class JsonObjectDeSerializer extends JsonDeserializer<JsonObject> {
  @Override
  @SuppressWarnings("unchecked")
  public JsonObject deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
	  return new JsonObject(p.readValueAs(Map.class));
  }
}
