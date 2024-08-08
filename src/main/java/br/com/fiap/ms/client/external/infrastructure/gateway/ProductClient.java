package br.com.fiap.ms.client.external.infrastructure.gateway;

import br.com.fiap.ms.client.application.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-product", url = "http://ms-product")
public interface ProductClient {
    @GetMapping("/products/{id}")
    ProductDto getProductById(@PathVariable("id") String productId);
}

