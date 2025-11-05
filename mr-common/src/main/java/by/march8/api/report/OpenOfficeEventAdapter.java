package by.march8.api.report;

public abstract class OpenOfficeEventAdapter implements OpenOfficeEvents {
    @Override
    public boolean canSaveAfterCreate() {
        return false;
    }

    @Override
    public boolean canOpenAfterCreate() {
        return false;
    }
}
