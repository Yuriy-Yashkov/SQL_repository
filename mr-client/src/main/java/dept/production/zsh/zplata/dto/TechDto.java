package dept.production.zsh.zplata.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TechDto {
    private String techName;
    private String employeeFio;
    private BigDecimal workTime;
}
