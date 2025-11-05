package dept.sbit.client.dao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BankDto {
    String bankName;
    String address;
    String MFO;
    String id;
    String swift;
    Boolean resident;
    String kornaim;
    String korunn;
    String korschet;
}
