package org.example.persistence.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Repository;

@Repository
public class YamlRepository {

  private final ObjectMapper mapper;

  public YamlRepository() {
    this.mapper = new ObjectMapper(new YAMLFactory());
    this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  public <T> void save(String filename, T data) throws IOException {
    mapper.writeValue(new File(filename), data);
  }

  public <T> T load(String filename, Class<T> clazz) throws IOException {
    return mapper.readValue(new File(filename), clazz);
  }
}
