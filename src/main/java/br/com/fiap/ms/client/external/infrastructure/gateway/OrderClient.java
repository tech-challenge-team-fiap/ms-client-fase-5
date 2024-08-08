package br.com.fiap.ms.client.external.infrastructure.gateway;

import br.com.fiap.ms.client.application.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-order", url = "http://ms-order")
public interface OrderClient {
    @PostMapping("/orders/update-stock")
    ResponseEntity<?> updateStock(@RequestBody OrderDto orderDto);
}
