package by.march8.entities.contractor;


import by.march8.api.BaseEntity;
import by.march8.api.DialogFrameSize;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;


@Entity
@Table(name = "s_email")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@DialogFrameSize(width = 300,height = 400)
public class ClientEmailItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
   // @TableHeader(name = "ID", sequence = 0)
    private int id ;

    @Column(name = "client_code")
   // @TableHeader(name = "Код клиента", sequence = 1)
    private int clientCode;
    @Column(name = "email")
    @TableHeader(name = "E-mail", sequence = 2)
    private String email ;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getClientCode() {
        return clientCode;
    }

    public void setClientCode(final int clientId) {
        this.clientCode = clientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return email;
    }
}
