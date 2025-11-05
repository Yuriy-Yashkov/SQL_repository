package dept.sbit.client.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractDto {
    private Integer clientId;
    private String nameContract;
    private String numberContract;
    private Date beginDate;
    private Date endDate;
}
