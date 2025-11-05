package dept.sbit.client.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeClientDto {
    private String code;
    private String name;
    private String fullname;
    // main
    private String clientType;
    private boolean isClient;
    private String phone;
    private String UNN;
    private String OKPO;
    private String licence;
    private boolean isRezident;
    private String discount;
    private String codeCountry;
    private String typeOfOrganization;
    private String manager;
    private String adult;
    private String children;
    // contract
    private String mainContract;
    private String director;
    private String chiefAccountant;
    private String purchasingManager;
    // address
    private String addressPost;
    private String addressUr;
    // accountNumber
    private String currentAccountSelected;
}
