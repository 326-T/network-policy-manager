package org.example.web.mapper;

import org.example.persistence.entity.NetworkPolicyValue;
import org.example.web.request.NetworkPolicyValueRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
    nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS)
public interface NetworkPolicyValueMapper {

  NetworkPolicyValue mapToEntity(NetworkPolicyValueRequest request);
}
