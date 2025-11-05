package by.gomel.freedev.ucframework.ucdao.interfaces;

import by.march8.entities.admin.UserInformation;
import by.march8.entities.admin.UserRole;

import java.util.ArrayList;

/**
 * Интерфейс описывает работу с данными в части администрирования приложения
 * Created by Andy on 10.09.2014.
 */

public interface IAdministrationDao {

    /**
     * Метод возвращает инвормацию о учетной записи
     */
    UserInformation getUserInformationThread(String userLogin);

    /**
     * Метод возвращает коллекцию доступных пользователю функций
     */
    ArrayList<UserRole> getUserRolesByEmployeeId(int employeeId);

    /**
     * Проверяет, есть ли у выбранного сотрудника учетная запись
     */
    UserInformation hasHaveAccount(int employeeId);
}
