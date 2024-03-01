package com.compassuol.sp.challenge.msuser.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.compassuol.sp.challenge.msuser.model.Address;

@FeignClient(name = "msaddress", url = "http://localhost:8081")
public interface AddressFeign {

    @GetMapping("/v1/address-api/get-address-by-cep/{cep}")
    Address getAddressByCep(@PathVariable("cep") String cep);
}
