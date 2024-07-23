package br.com.fiap.ms.client.application.usecases;

import br.com.fiap.ms.client.application.dto.ClientDto;
import br.com.fiap.ms.client.application.dto.OrderDto;
import br.com.fiap.ms.client.application.dto.PaymentDto;
import br.com.fiap.ms.client.application.dto.ProductDto;
import br.com.fiap.ms.client.domain.exception.InvalidProcessException;
import br.com.fiap.ms.client.domain.exception.client.ClientNotFoundException;
import br.com.fiap.ms.client.domain.exception.client.InvalidClientProcessException;
import br.com.fiap.ms.client.domain.interfaces.ClientUseCaseInterface;
import br.com.fiap.ms.client.domain.interfaces.abstracts.AbstractClientUseCase;
import br.com.fiap.ms.client.domain.model.Client;
import br.com.fiap.ms.client.external.infrastructure.gateway.ClientGatewayImpl;
import br.com.fiap.ms.client.external.infrastructure.gateway.ProductClient;
import br.com.fiap.ms.client.external.infrastructure.gateway.OrderClient;
import br.com.fiap.ms.client.external.infrastructure.gateway.PaymentQueueConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientUseCaseImpl extends AbstractClientUseCase implements ClientUseCaseInterface {

    private static final Logger logger = LoggerFactory.getLogger(ClientUseCaseImpl.class);
    private final ClientGatewayImpl clientGatewayImpl;
    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final PaymentQueueConfig paymentQueueConfig;

    @Autowired
    public ClientUseCaseImpl(ClientGatewayImpl clientGatewayImpl, ProductClient productClient,
                             OrderClient orderClient, PaymentQueueConfig paymentQueueConfig) {
        super(logger);
        this.clientGatewayImpl = clientGatewayImpl;
        this.productClient = productClient;
        this.orderClient = orderClient;
        this.paymentQueueConfig = paymentQueueConfig;
    }

    @Override
    public List<ClientDto> findAll() {
        return clientGatewayImpl.listFindAll();
    }

    @Override
    public ClientDto findByCpf(String cpf) throws InvalidProcessException {
        return clientGatewayImpl.findByCpf(cpf);
    }

    @Override
    public ClientDto findByDocument(String cpf) throws ClientNotFoundException {
        return clientGatewayImpl.findByDocument(cpf);
    }

    @Override
    public ClientDto edit(ClientDto clientDto) throws InvalidClientProcessException {
        validateEmail(clientDto.getEmail());
        validatePhoneNumber(clientDto.getPhone());

        return clientGatewayImpl.update(clientDto);
    }

    @Override
    public ClientDto register(ClientDto clientDto) throws InvalidClientProcessException {
        validateCpf(clientDto.getCpf());
        validateEmail(clientDto.getEmail());
        validatePhoneNumber(clientDto.getPhone());
        checkIfClientAlreadyExists(clientDto.getCpf());

        ProductDto product = productClient.getProductById(clientDto.getProductId());
        if (product == null) {
            throw new InvalidClientProcessException("Product not found");
        }

        OrderDto orderDto = new OrderDto(clientDto.getProductId(), clientDto.getQuantity());
        ResponseEntity<?> response = orderClient.updateStock(orderDto);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidClientProcessException("Failed to update stock");
        }

        PaymentDto paymentDto = new PaymentDto(clientDto.getCpf(), clientDto.getTotalAmount());
        paymentQueueConfig.sendPaymentMessage(paymentDto);

        return clientGatewayImpl.register(new Client(clientDto));
    }

    @Override
    public ClientDto remove(String cpf) throws InvalidClientProcessException {
        validateCpf(cpf);
        return clientGatewayImpl.delete(cpf);
    }

    private void checkIfClientAlreadyExists(String cpf) throws InvalidClientProcessException {
        try {
            clientGatewayImpl.findByCpf(cpf);
        } catch (ClientNotFoundException e) {
            return;
        }
        throw new InvalidClientProcessException("Client with CPF " + cpf + " already exists.");
    }
}
