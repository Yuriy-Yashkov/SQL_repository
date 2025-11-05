package by.march8.ecs.application.modules.warehouse.external.shipping.manager;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.march8.entities.warehouse.Certificate;

import java.sql.SQLException;
import java.util.List;


/**
 * Менеджер сертификатов продукции. Синглтон. Вызывать через getInstance() ;
 *
 * @author Andy 17.05.2016.
 */
public class CertificateService {

    private static final String SIGN_CERTIFICATE = "Сертификат";
    private static final String SIGN_LICENSE = "Удостоверение ГГР";
    private static CertificateService instance = null;
    private static List<Certificate> list = null;

    private CertificateService() {

    }

    /**
     * Возвращает менеджер сертификатов приложения
     *
     * @return менеджер сертификатов
     */
    public static CertificateService getInstance() {
        if (instance == null) {
            instance = new CertificateService();
        }
        return instance;
    }

    /**
     * ВОзвращает список доступных сертификатов
     *
     * @return список сертификатов
     */
    public List<Certificate> getCertificateList() {
        //System.out.println("Получение сертификатов на продукцию....");
        if (list == null) {
            DaoFactory<Certificate> factory = DaoFactory.getInstance();
            IGenericDao<Certificate> dao = factory.getGenericDao();
            try {
                list = dao.getAllEntity(Certificate.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Возвращает сертификат согласно шифра артикула и типа сертификата
     *
     * @param articleNumber шифр артикула
     * @param type          тип сертификата
     * @return сертификат
     */
    public Certificate getCertificateByArticle(String articleNumber, int type) {
        List<Certificate> list = getCertificateList();
        for (Certificate certificate : list) {
            String article = certificate.getArticleCode();
            if (article != null) {
                if (type == certificate.getType()) {
                    //System.out.println(article.trim()+" *** "+articleNumber.trim());
                    if (article.trim().equals(articleNumber.trim())) {
                        return certificate;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Метод отбора сертификата отбрасыванием последней цифры шифра до первого вхождения
     *
     * @param articleNumber шифр артикула
     * @param type          тип сертификата
     * @return сертификат
     */
    public Certificate getCertificateByArticleRecursively(String articleNumber, int type) {
        List<Certificate> list = getCertificateList();
        for (int i = articleNumber.length(); i > 2; i--) {
            String tmpArticle = articleNumber.substring(0, i);
            for (Certificate certificate : list) {
                String article = certificate.getArticleCode();
                if (article != null) {
                    if (type == certificate.getType() && certificate.getTypeName().equals(SIGN_CERTIFICATE)) {
                        if (article.trim().equals(tmpArticle)) {
                            // System.out.println(article.trim() + " *** " + tmpArticle + " *** " + articleNumber);
                            return certificate;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Метод отбора удостоверения отбрасыванием последней цифры шифра до первого вхождения
     *
     * @param articleNumber шифр артикула
     * @param type          тип сертификата
     * @return сертификат
     */
    public Certificate getLicenseByArticleRecursively(String articleNumber, int type, int ageGroup) {
        List<Certificate> list = getCertificateList();
        for (int i = articleNumber.length(); i > 2; i--) {
            String tmpArticle = articleNumber.substring(0, i);
            for (Certificate certificate : list) {
                String article = certificate.getArticleCode();
                if (article != null) {
                    if (type == certificate.getType() && certificate.getTypeName().equals(SIGN_LICENSE) && certificate.getGroup() == ageGroup) {
                        if (article.trim().equals(tmpArticle)) {
                            // System.out.println(article.trim() + " *** " + tmpArticle + " *** " + articleNumber);
                            return certificate;
                        }
                    }
                }
            }
        }
        return null;
    }


}
