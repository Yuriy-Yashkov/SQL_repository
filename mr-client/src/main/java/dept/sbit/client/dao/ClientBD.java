package dept.sbit.client.dao;

import by.gomel.freedev.ucframework.ucdao.jdbc.AbstractMSSQLServerJDBC;
import dept.sbit.client.dao.dto.AddressDto;
import dept.sbit.client.dao.dto.BankDto;
import dept.sbit.client.dao.dto.ChangeClientDto;
import dept.sbit.client.dao.dto.ClientDTO;
import dept.sbit.client.dao.dto.ContractDto;
import dept.sbit.client.dao.dto.CurrentAccountDTO;
import dept.sbit.client.dao.dto.NewClientDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClientBD extends AbstractMSSQLServerJDBC {

    private static final Logger log = Logger.getLogger(ClientBD.class.getName());

    public List<ClientDTO> getClients() {
        String sql =
                "select KOD, client.NAIM, TELEFON, UNN,\n" +
                        "       (select addres.NAIM\n" +
                        "        from dbo.s_adres addres\n" +
                        "        where client.POSTADRES = addres.ITEM_ID), accountNumber.NOMER\n" +
                        "from dbo.s_klient client\n" +
                        "    left join dbo.s_rschet accountNumber on client.R_SCHET = accountNumber.ITEM_ID\n" +
                        "order by KOD";
        List<ClientDTO> clients = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClientDTO client = ClientDTO.builder()
                        .code(rs.getString(1))
                        .name(rs.getString(2))
                        .phone(rs.getString(3))
                        .UNN(rs.getString(4))
                        .address(rs.getString(5))
                        .accountNumber(rs.getString(6))
                        .build();

                clients.add(client);
            }
        } catch (Exception e) {
            log.severe("Exception getClients " + e.getMessage());
        }
        return clients;
    }

    public String[] getAddressByClientCode(String text) {
        if (text.isEmpty()) {
            return new String[]{};
        }
        String sql = "select a.NAIM\n" +
                "from dbo.s_adres a\n" +
                "    join dbo.s_klient on a.KLIENT_ID = s_klient.ITEM_ID\n" +
                "where KOD = ?";
        List<String> addresses = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(text));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                addresses.add(rs.getString(1).trim());
            }
        } catch (Exception e) {
            log.severe("Exception getAddressByClientCode " + e.getMessage());
        }
        return addresses.toArray(new String[0]);
    }

    public String[] getCurrentAccountByClientCode(String text) {
        if (text.isEmpty()) {
            return new String[]{};
        }
        String sql = "select bank.NAIM, raschet.NOMER\n" +
                "from dbo.s_klient\n" +
                "    join dbo.s_rschet raschet on s_klient.ITEM_ID = raschet.KLIENT_ID\n" +
                "    join dbo.s_bank bank on raschet.BANK_ID = bank.ITEM_ID\n" +
                "where s_klient.KOD = ?";
        List<String> accounts = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(text));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                accounts.add(rs.getString(1).trim() + " Р/с: " + rs.getString(2).trim());
            }
        } catch (Exception e) {
            log.severe("Exception getCurrentAccountByClientCode " + e.getMessage());
        }
        return accounts.toArray(new String[0]);
    }

    public String[] getMainContract(String text) {
        if (text.isEmpty()) {
            return new String[]{};
        }
        String sql = "select d.NAIM, d.NOMER, d.DATA\n" +
                "from dbo.s_klient\n" +
                "    join dbo.s_dogovor d on s_klient.ITEM_ID = d.KLIENT_ID\n" +
                "where KOD = ?\n" +
                "order by d.DATA DESC";
        List<String> contracts = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(text));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                contracts.add(rs.getString(1).trim() +
                        " №" + rs.getString(2).trim() +
                        " от " + rs.getString(3).trim());
            }
        } catch (Exception e) {
            log.severe("Exception getMainContract " + e.getMessage());
        }
        return contracts.toArray(new String[0]);
    }

    public ChangeClientDto getClientByKOD(int code) {
        String sql =
                "select client.KOD, client.NAIM, client.FULLNAIM, client.VID, client.POKUP, client.TELEFON,\n" +
                        "       client.UNN, client.OKPO, client.LISENCE, client.REZIDENT, client.SKIDKA,\n" +
                        "       client.KT, client.KVO, client.USERVRKV," +
                        "       (select addres.NAIM\n" +
                        "        from dbo.s_adres addres\n" +
                        "        where client.POSTADRES = addres.ITEM_ID) adressPost,\n" +
                        "       (select addres.NAIM\n" +
                        "        from dbo.s_adres addres\n" +
                        "        where client.URADRES = addres.ITEM_ID) adressUr,\n" +
                        "    d.NAIM,\n" +
                        "    d.NOMER,\n" +
                        "    d.DATA,\n" +
                        "    client.DIRECTOR,\n" +
                        "    client.GLBUH,\n" +
                        "    client.MANAGER,\n" +
                        "    bank.NAIM,\n" +
                        "    accountNumber.NOMER\n" +
                        "from dbo.s_klient client\n" +
                        "    left join dbo.s_adres adress on client.ITEM_ID = adress.KLIENT_ID\n" +
                        "    left join dbo.s_rschet accountNumber on client.R_SCHET = accountNumber.ITEM_ID\n" +
                        "    left join dbo.s_bank bank on accountNumber.BANK_ID = bank.ITEM_ID\n" +
                        "    left join dbo.s_dogovor d on client.DOGOVOR = d.ITEM_ID\n" +
                        "where KOD = ?\n" +
                        "order by KOD";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return ChangeClientDto.builder()
                        .code(rs.getString(1).trim())
                        .name(rs.getString(2).trim())
                        .fullname(rs.getString(3).trim())

                        .clientType(rs.getString(4).trim())
                        .isClient(rs.getBoolean(5))
                        .phone(rs.getString(6).trim())
                        .UNN(rs.getString(7).trim())
                        .OKPO(rs.getString(8).trim())
                        .licence(rs.getString(9).trim())
                        .isRezident(rs.getBoolean(10))
                        .discount(rs.getString(11).trim())
                        .codeCountry(rs.getString(12).trim())
                        .typeOfOrganization(rs.getString(13).trim())
                        .manager(rs.getString(14) != null
                                ? rs.getString(14).trim()
                                : "")
                        .adult("")
                        .children("")

                        .mainContract((rs.getString(17) != null && rs.getString(18) != null && rs.getString(19) != null) ?
                                rs.getString(17).trim() + " №" +
                                        rs.getString(18).trim() + " от " +
                                        rs.getString(19).trim() : "")
                        .director(rs.getString(20) != null ? rs.getString(20).trim() : "")
                        .chiefAccountant(rs.getString(21) != null ? rs.getString(21).trim() : "")
                        .purchasingManager("")

                        .addressPost(rs.getString(15) != null ? rs.getString(15).trim() : "")
                        .addressUr(rs.getString(16) != null ? rs.getString(16).trim() : "")


                        .currentAccountSelected((rs.getString(22) != null && rs.getString(23) != null)
                                ? rs.getString(22).trim() + " Р/с: " + rs.getString(23).trim()
                                : "")
                        .build();
            }
        } catch (Exception e) {
            log.severe("Exception getClientByKOD " + e.getMessage());
        }
        return new ChangeClientDto();
    }

    public void addClient(NewClientDTO clientDTO) {
        String sqlIsCodeExist =
                "SELECT CASE WHEN EXISTS (SELECT 1 FROM dbo.s_klient WHERE KOD = ?) THEN 'true' ELSE 'false' END AS result";

        String sqlInsert = "DECLARE @maxId INT; SELECT @maxId = MAX(ITEM_ID) FROM dbo.s_klient; " +
                "INSERT INTO dbo.s_klient(ITEM_ID, KOD, VID, NAIM, FULLNAIM, TELEFON, UNN, OKPO, REZIDENT, LISENCE, POKUP, SKIDKA, KT, KVO) " +
                "VALUES (@maxId + 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement psCode = conn.prepareStatement(sqlIsCodeExist);
             PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
            psCode.setInt(1, clientDTO.getCode());
            ResultSet rs = psCode.executeQuery();
            if (rs.next() && !rs.getBoolean(1)) {
                psInsert.setInt(1, clientDTO.getCode());
                psInsert.setInt(2, clientDTO.getClientType());
                psInsert.setString(3, clientDTO.getName());
                psInsert.setString(4, clientDTO.getFullName());
                psInsert.setString(5, clientDTO.getPhoneNumber());
                psInsert.setString(6, clientDTO.getUnn());
                psInsert.setString(7, clientDTO.getOkpo());
                psInsert.setBoolean(8, clientDTO.getIsResident());
                psInsert.setString(9, clientDTO.getLicence());
                psInsert.setBoolean(10, clientDTO.getIsClient());
                psInsert.setString(11, clientDTO.getDiscount());
                psInsert.setString(12, clientDTO.getCodeCountry());
                psInsert.setString(13, clientDTO.getTypeOrganisation());
                psInsert.executeUpdate();
            }

        } catch (Exception e) {
            log.severe("Exception addClient " + e.getMessage());
        }
    }

    public void changeClient(NewClientDTO clientDTO) {

        String sqlInsert =
                "DECLARE @ITEM_ID INT; SELECT @ITEM_ID = ITEM_ID FROM dbo.s_klient where KOD = ?; " +
                        "DECLARE @postAddressId INT; SELECT @postAddressId = ITEM_ID FROM dbo.s_adres where NAIM = ? and KLIENT_ID = @ITEM_ID; " +
                        "DECLARE @urAddressId INT; SELECT @urAddressId = ITEM_ID FROM dbo.s_adres where NAIM = ? and KLIENT_ID = @ITEM_ID; " +
                        "DECLARE @contract INT; SELECT @contract = ITEM_ID FROM dbo.s_dogovor where NOMER = ? and KLIENT_ID = @ITEM_ID; " +
                        "DECLARE @currentAccount INT; SELECT @currentAccount = ITEM_ID FROM dbo.s_rschet where NOMER = ? and KLIENT_ID = @ITEM_ID; " +

                        "UPDATE dbo.s_klient " +
                        "SET VID = ?, " +
                        "    NAIM = ?, " +
                        "    FULLNAIM = ?, " +
                        "    TELEFON = ?, " +
                        "    UNN = ?, " +
                        "    OKPO = ?, " +
                        "    REZIDENT = ?, " +
                        "    LISENCE = ?, " +
                        "    POKUP = ?, " +
                        "    SKIDKA = ?, " +
                        "    KT = ?, " +
                        "    KVO = ?, " +
                        "    R_SCHET = @currentAccount, " +
                        "    DOGOVOR = @contract, " +
                        "    POSTADRES = @postAddressId, " +
                        "    URADRES = @urAddressId, " +
                        "    DIRECTOR = ?, " +
                        "    GLBUH = ?, " +
                        "    MANAGER = ? " +
                        "WHERE ITEM_ID = @ITEM_ID";
        try (Connection conn = getConnection();
             PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
            psInsert.setInt(1, clientDTO.getCode());
            psInsert.setString(2, clientDTO.getPostAddress());
            psInsert.setString(3, clientDTO.getUrAddress());
            psInsert.setString(4, clientDTO.getMainContract());
            psInsert.setString(5, clientDTO.getCurrentAccount());
            psInsert.setInt(6, clientDTO.getClientType());
            psInsert.setString(7, clientDTO.getName());
            psInsert.setString(8, clientDTO.getFullName());
            psInsert.setString(9, clientDTO.getPhoneNumber());
            psInsert.setString(10, clientDTO.getUnn());
            psInsert.setString(11, clientDTO.getOkpo());
            psInsert.setBoolean(12, clientDTO.getIsResident());
            psInsert.setString(13, clientDTO.getLicence());
            psInsert.setBoolean(14, clientDTO.getIsClient());
            psInsert.setString(15, clientDTO.getDiscount());
            psInsert.setString(16, clientDTO.getCodeCountry());
            psInsert.setString(17, clientDTO.getTypeOrganisation());
            psInsert.setString(18, clientDTO.getDirector());
            psInsert.setString(19, clientDTO.getChiefAccount());
            psInsert.setString(20, clientDTO.getPurchasingManager());

            psInsert.executeUpdate();

        } catch (Exception e) {
            log.severe("Exception changeClient " + e.getMessage());
        }
    }

    public boolean addAddress(AddressDto addressDto) {
        String sql =
                "DECLARE @client_id INT;\n" +
                        "SELECT @client_id = ITEM_ID\n" +
                        "FROM dbo.s_klient\n" +
                        "WHERE KOD = ?; " +
                        "INSERT INTO dbo.s_adres (NAIM, PINDEX, OBLAST, RAION, GOROD, STREET, DOM, KLIENT_ID)\n" +
                        "VALUES(?,?,?,?,?,?,?, @client_id);";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(2, addressDto.getFullAddress());
            ps.setString(3, addressDto.getZipcode());
            ps.setString(4, addressDto.getState());
            ps.setString(5, addressDto.getRaion());
            ps.setString(6, addressDto.getCity());
            ps.setString(7, addressDto.getStreet());
            ps.setString(8, addressDto.getNumber());
            ps.setInt(1, addressDto.getClientId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            log.severe("Exception addAddress " + e.getMessage());
        }
        return false;
    }

    public boolean addContract(ContractDto contractDto) {
        String sql =
                "DECLARE @client_id INT;\n" +
                "    SELECT @client_id = ITEM_ID\n" +
                "    FROM dbo.s_klient\n" +
                "    WHERE KOD = ?;\n" +
                "\n" +
                "INSERT INTO dbo.s_dogovor (NAIM, DATA, NOMER, DATA_END, KLIENT_ID)\n" +
                "VALUES(?,?,?,?, @client_id);";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(2, contractDto.getNameContract());
            ps.setString(4, contractDto.getNumberContract());
            ps.setDate(3, contractDto.getBeginDate());
            ps.setDate(5, contractDto.getEndDate());
            ps.setInt(1, contractDto.getClientId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            log.severe("Exception addContract " + e.getMessage());
        }
        return false;
    }

    public void addCurrentAccount(CurrentAccountDTO currentAccountDTO) {
        String sql =
                "DECLARE @client_id INT;\n" +
                        "SELECT @client_id = ITEM_ID\n" +
                        "FROM dbo.s_klient\n" +
                        "WHERE KOD = ?;\n" +
                        "\n" +
                        "INSERT INTO dbo.s_rschet (NAIM, NOMER, BANK_ID, KLIENT_ID, VALUTA_ID)\n" +
                        "VALUES(?, ?, ?, @client_id, ?);";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, currentAccountDTO.getClientId());
            ps.setString(2, currentAccountDTO.getAccountName());
            ps.setString(3, currentAccountDTO.getAccountNumber());
            ps.setInt(4, currentAccountDTO.getBankId());
            ps.setInt(5, currentAccountDTO.getCurrencyType());
            ps.executeUpdate();
        } catch (Exception e) {
            log.severe("Exception addCurrentAccount " + e.getMessage());
        }
    }

    public BankDto getBankById(Integer bankId) {
        String sql = "select NAIM, PADRES, MFO, ITEM_ID, SWIFT, REZIDENT, KORNAIM, KORUNN, KORSCHET\n" +
                "from dbo.s_bank\n" +
                "where ITEM_ID = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, bankId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return BankDto.builder()
                        .korschet(rs.getString(9).trim())
                        .korunn(rs.getString(8).trim())
                        .kornaim(rs.getString(7).trim())
                        .resident(rs.getBoolean(6))
                        .swift(rs.getString(5).trim())
                        .id(rs.getString(4).trim())
                        .MFO(rs.getString(3).trim())
                        .address(rs.getString(2).trim())
                        .bankName(rs.getString(1).trim())
                        .build();
            }
        } catch (Exception e) {
            log.severe("Exception getBankById " + e.getMessage());
        }
        return null;
    }

    public List<BankDto> getBanks() {
        String sql = "select NAIM, PADRES, MFO, ITEM_ID\n" +
                "from dbo.s_bank";
        List<BankDto> banks = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                banks.add(BankDto.builder()
                        .id(rs.getString(4))
                        .MFO(rs.getString(3))
                        .address(rs.getString(2))
                        .bankName(rs.getString(1))
                        .build());
            }
        } catch (Exception e) {
            log.severe("Exception getBanks " + e.getMessage());
        }
        return banks;
    }
}
