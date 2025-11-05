package by.march8.entities.readonly;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.utils.DateUtils;
import by.march8.api.MarchDataSourceEnum;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@NamedQueries({
        @NamedQuery(name = "ContractEntity.findByContractorId",
                query = "SELECT contract FROM ContractEntity contract  " +
                        "WHERE contract.contractor.id = :contractor "+
                        "order by date")
})

/**
 * @author Andy 23.10.2015.
 */
@Entity
@Table(name = "s_dogovor")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private int id;

    @Column(name = "NAIM")
    private String name;

    @Column(name = "NOMER")
    private String number;

    @Column(name = "DATA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "DATA_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KLIENT_ID")
    private ContractorEntity contractor;

    @Override
    public String toString() {
        return Objects.nonNull(this.name)
                ? name.trim() + " № " + number.trim() + " от " + DateUtils.getNormalDateFormat(date) + "г." : "";
    }
}
