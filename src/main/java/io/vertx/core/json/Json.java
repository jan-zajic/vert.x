/*
 * Copyright (c) 2011-2014 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.core.json;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.Context;
import io.vertx.core.ServiceHelper;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.spi.MapperFactory;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Json {

  public static ObjectMapper mapper;
  public static ObjectMapper prettyMapper;
  
  static {
	  MapperFactory factory = ServiceHelper.loadFactory(MapperFactory.class);
	  Json.mapper = factory.createMapper();
	  Json.prettyMapper = factory.createPrettyMapper();
  }
  
  private static ObjectMapper getMapper() {
	  Context context = VertxImpl.context();
	  if(context != null && context.getMapper() != null) {
		  return context.getMapper();
	  } else {
		  return Json.mapper;
	  }
  }
  
  private static ObjectMapper getPrettyMapper() {
	  Context context = VertxImpl.context();
	  if(context != null && context.getPrettyMapper() != null) {
		  return context.getPrettyMapper();
	  } else {
		  return Json.prettyMapper;
	  }
  }
  
  public static String encode(Object obj) throws EncodeException {
    return Json.encode(getMapper(), obj);
  }
  
  public static String encodePrettily(Object obj) throws EncodeException {
    return Json.encode(getPrettyMapper(), obj);
  }

  public static String encode(ObjectMapper mapper, Object obj) throws EncodeException {
    try {
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new EncodeException("Failed to encode as JSON: " + e.getMessage());
    }
  }
  
  public static <T> T decodeValue(String str, Class<T> clazz) throws DecodeException {
    return decodeValue(getMapper(), str, clazz);
  }
  
  public static <T> T decodeValue(ObjectMapper mapper, String str, Class<T> clazz) throws DecodeException {
    try {
      return mapper.readValue(str, clazz);
    }
    catch (Exception e) {
      throw new DecodeException("Failed to decode:" + e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  static Object checkAndCopy(Object val, boolean copy) {
    if (val == null) {
      // OK
    } else if (val instanceof Number && !(val instanceof BigDecimal)) {
      // OK
    } else if (val instanceof Boolean) {
      // OK
    } else if (val instanceof String) {
      // OK
    } else if (val instanceof Character) {
      // OK
    } else if (val instanceof CharSequence) {
      val = val.toString();
    } else if (val instanceof JsonObject) {
      if (copy) {
        val = ((JsonObject) val).copy();
      }
    } else if (val instanceof JsonArray) {
      if (copy) {
        val = ((JsonArray) val).copy();
      }
    } else if (val instanceof Map) {
      if (copy) {
        val = (new JsonObject((Map)val)).copy();
      } else {
        val = new JsonObject((Map)val);
      }
    } else if (val instanceof List) {
      if (copy) {
        val = (new JsonArray((List)val)).copy();
      } else {
        val = new JsonArray((List)val);
      }
    } else if (val instanceof byte[]) {
      val = Base64.getEncoder().encodeToString((byte[])val);
    } else if (val instanceof Instant) {
      val = ISO_INSTANT.format((Instant) val);
    } else {
      throw new IllegalStateException("Illegal type in JsonObject: " + val.getClass());
    }
    return val;
  }

  static <T> Stream<T> asStream(Iterator<T> sourceIterator) {
    Iterable<T> iterable = () -> sourceIterator;
    return StreamSupport.stream(iterable.spliterator(), false);
  }


}
