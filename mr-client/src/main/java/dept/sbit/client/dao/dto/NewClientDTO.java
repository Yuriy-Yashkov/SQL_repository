package dept.sbit.client.dao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class NewClientDTO {
    Integer code;
    String name;
    String fullName;
    String phoneNumber;
    String unn;
    String okpo;
    String licence;
    String discount;
    String codeCountry;
    Boolean isClient;
    Boolean isResident;
    Integer clientType;
    String typeOrganisation;

    String mainContract;
    String currentAccount;
    String postAddress;
    String urAddress;
    String director;
    String chiefAccount;
    String purchasingManager;
}
