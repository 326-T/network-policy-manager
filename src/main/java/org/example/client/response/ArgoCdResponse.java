package org.example.client.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArgoCdResponse {

  private Integer code;
  private String details;
  private String error;
  private String message;
}
