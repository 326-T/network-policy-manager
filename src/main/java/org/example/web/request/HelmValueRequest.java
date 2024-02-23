package org.example.web.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HelmValueRequest<T> {

  private String systemCode;
  private String namespace;
  private T body;
}
