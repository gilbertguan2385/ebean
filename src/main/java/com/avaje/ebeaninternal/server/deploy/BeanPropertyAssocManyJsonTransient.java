package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebeaninternal.server.text.json.ReadJson;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Helper used to read transient many properties using Jackson ObjectMapper.
 */
public class BeanPropertyAssocManyJsonTransient {

  /**
   * Use Jackson ObjectMapper to read the transient 'many' property.
   */
  public void jsonReadUsingObjectMapper(BeanPropertyAssocMany<?> many, ReadJson readJson, EntityBean parentBean) throws IOException {

    ObjectMapper mapper = readJson.getObjectMapper();

    ManyType manyType = many.getManyType();

    Object value;
    if (manyType.isMap()) {
      // read map using Jackson object mapper with unknown key type
      TypeFactory typeFactory = mapper.getTypeFactory();
      JavaType target = typeFactory.constructType(many.getTargetType());
      MapType jacksonType = typeFactory.constructMapType(LinkedHashMap.class, typeFactory.unknownType(), target);
      value = mapper.readValue(readJson.getParser(), jacksonType);

    } else {
      // read list or set using Jackson object mapper
      CollectionType jacksonType = mapper.getTypeFactory().constructCollectionType(manyType.getCollectionType(), many.getTargetType());
      value = mapper.readValue(readJson.getParser(), jacksonType);
    }

    many.setValue(parentBean, value);
  }
}
