package dept.sbit.client.dao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClientDTO {
    private String code;
    private String name;
    private String address;
    private String accountNumber;
    private String phone;
    private String UNN;
}
