package io.vertx.core.json.impl;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import io.vertx.core.json.JsonArray;

public class JsonArrayDeSerializer extends JsonDeserializer<JsonArray> {
  @Override
  public JsonArray deserialize(JsonParser p, DeserializationContext ctxt)
	  	throws IOException, JsonProcessingException {
    return new JsonArray(p.readValueAs(List.class));
  }
}
