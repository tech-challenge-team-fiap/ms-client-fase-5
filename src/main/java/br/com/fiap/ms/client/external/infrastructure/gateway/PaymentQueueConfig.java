package br.com.fiap.ms.client.external.infrastructure.gateway;

import br.com.fiap.ms.client.application.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class PaymentQueueConfig {

    private final StreamBridge streamBridge;

    @Autowired
    public PaymentQueueConfig(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendPaymentMessage(PaymentDto paymentDto) {
        streamBridge.send("queuePayments-out-0", MessageBuilder.withPayload(paymentDto).build());
    }
}
