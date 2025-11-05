package dept.sbit.client.dao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddressDto {
    String fullAddress;
    String zipcode;
    String city;
    String state;
    String raion;
    String street;
    String number;
    Integer id;
    Integer clientId;
}
