package by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces;

import by.march8.entities.readonly.AddressEntity;
import by.march8.entities.readonly.ContractEntity;
import by.march8.entities.readonly.ContractorEntity;

import java.util.List;

/**
 * Сервис управления контрагентами
 *
 * @author Andy 11.11.2015.
 */
public interface ContractorDao {

    /**
     * Возвращает контрагента по его коду (не ID)
     *
     * @param contractorCode идентификатор контрагента
     * @return контрагент
     */
    ContractorEntity getContractorByCode(int contractorCode);

    /**
     * Возвращает список договоров по ID контрагента
     *
     * @param contractorId идентификатор контрагента
     * @return список договоров по контрагенту
     */
    List<ContractEntity> getContractsByContractorId(int contractorId);

    /**
     * Возвращает список адресов, закрепленных за контрагентом  по его ID
     *
     * @param contractorId идентификатор контрагента
     * @return список договоров по контрагенту
     */
    List<AddressEntity> getAddressByContractorId(int contractorId);

    /**
     * Возвращает юридический адрес контрагента по его ID
     *
     * @param contractorId идентификатор контрагента
     * @return юридический адрес
     */
    AddressEntity getLegalAddressByContractorId(int contractorId);


}
