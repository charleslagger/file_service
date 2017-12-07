package com.vega.samo.util;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 *  Author : Ngo Duy Kien
 *          Email:kiennd@vega.com.vn
 * Nov 2, 2017
 */

public class JsonMapper {
  
  private static Logger logger = Logger.getLogger(JsonMapper.class);
  private static ObjectMapper mapper = new ObjectMapper();
  
  public static <O> String writeValueAsString(O o) {
    try {
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      return mapper.writeValueAsString(o);
    } catch (Exception e) {
      logger.error(e.getMessage(),  e);
      return e.getMessage();
    }
  }
  
  public static <T> T convertJsonToObject(String jsonStr, final Class<T> clazz) {
    try {
      return mapper.readValue(jsonStr, clazz);
    } catch (Exception e) {
      logger.error(e.getMessage(),  e);
      return null;
    }
  }
  
  public static <T> T convertJsonToObject(String jsonStr, final TypeReference<T> reference) {
    try {
      return mapper.readValue(jsonStr, reference);
    } catch (Exception e) {
      logger.error(e.getMessage(),  e);
      return null;
    }
  }
}
