package dept.sbit.client.dao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class CurrentAccountDTO {
    String accountName;
    String accountNumber;
    Integer currencyType;
    Integer bankId;
    Integer clientId;
}
