package org.example.web.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateHelmChartRequest {

  private String systemCode;
  private String namespace;
}
