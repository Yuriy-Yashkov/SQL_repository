package dept.production.zsh.zplata;

import lombok.Getter;
import lombok.Setter;

import java.util.Vector;

/**
 *
 * @author lidashka
 */
@Setter
@Getter
public class ItemT4 {
    private String vedName;
    private String dept;
    private String brig;
    private String period;
    private Vector workSmen;
    private Vector workClock;
    private Vector workDay;
    private Vector dataPeople;
    private Long date;

    public ItemT4() {
    }

    public ItemT4(String vedName, String dept, String brig, String period, Vector workSmen, Vector workClock, Vector workDay, Vector dataPeople, Long date) {
        this.vedName = vedName;
        this.dept = dept;
        this.brig = brig;
        this.period = period;
        this.workSmen = workSmen;
        this.workClock = workClock;
        this.workDay = workDay;
        this.dataPeople = dataPeople;
        this.date = date;
    }


}
