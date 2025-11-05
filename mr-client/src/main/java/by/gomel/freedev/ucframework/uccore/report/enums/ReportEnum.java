package by.gomel.freedev.ucframework.uccore.report.enums;

/**
 * Created by Andy on 15.10.2014.
 */
public enum ReportEnum {
    TEST_REPORT("TEST_REPORT", ReportType.REPORT_XLS),
    XLSM_REPORT("XLSM_REPORT", ReportType.REPORT_XLSM);

    private String name;
    private ReportType type;

    ReportEnum(final String name, final ReportType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
