package dept.sbit.client.dao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddClientDto {
    private String code;
    private String name;
    private String fullname;

    // main
    private String clientType;
    private Boolean isClient;
    private String phone;
    private String UNN;
    private String OKPO;
    private String licence;
    private Boolean isRezident;
    private String discount;
    private String codeCountry;
    private String typeOfOrganization;
    private String manager;
    private String tradeMarkupAdult;
    private String tradeMarkupChildren;

    // contract
    private String contractNumber;
    private String director;
    private String chiefAccountant;
    private String purchasingManager;
}
