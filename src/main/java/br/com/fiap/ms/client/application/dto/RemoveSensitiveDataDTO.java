package br.com.fiap.ms.client.application.dto;

import br.com.fiap.ms.client.domain.enums.SensitiveData;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemoveSensitiveDataDTO {
    private List<SensitiveData> sensitiveDataList;
}
