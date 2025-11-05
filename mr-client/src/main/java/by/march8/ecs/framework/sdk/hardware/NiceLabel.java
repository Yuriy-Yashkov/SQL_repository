package by.march8.ecs.framework.sdk.hardware;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Обертка для работы ActiveX компонентом NiceLabel.ocx
 * Будет работать только под Windows, посему делать проверку на ОС во избежании ошибок
 * Created by Andy on 07.02.2019.
 */
public class NiceLabel {

    private ActiveXComponent component;

    public NiceLabel() {
        initialComponent();
    }

    private void initialComponent() {
        component = new ActiveXComponent("NiceLabel.Application");
    }

    public Variant openLabel(String file) {
        return Dispatch.call(component, "LabelOpen", file);
    }

    public boolean closeLabel(Variant labelId) {
        return Dispatch.call(component, "LabelClose", labelId).getBoolean();
    }

    public Variant getLabelVariablesCount(Variant labelId) {
        return Dispatch.call(component, "LabelGetVarCount", labelId);
    }

    public String getLabelVariableName(Variant labelId, int varId) {
        return Dispatch.call(component, "LabelGetVarName", labelId, new Variant(varId)).getString();
    }

    public boolean sessionStart(Variant labelId) {
        return Dispatch.call(component, "LabelSessionStart", labelId).getBoolean();
    }

    public boolean sessionStop(Variant labelId) {
        return Dispatch.call(component, "LabelSessionStop", labelId).getBoolean();
    }

    public boolean sessionPrint(Variant labelId, int count) {
        return Dispatch.call(component, "LabelSessionPrint", labelId, String.valueOf(count)).getBoolean();
    }

    public boolean printLabel(Variant labelId, int count) {
        return Dispatch.call(component, "LabelPrint", labelId, String.valueOf(count)).getBoolean();
    }


    public boolean setLabelVariableValue(Variant labelId, String name, String value) {
        return Dispatch.call(component, "LabelSetVar", labelId, name, value, -9999, -9999).getBoolean();
    }

    public boolean testLabel(Variant labelId) {
        return Dispatch.call(component, "LabelTestConnection", labelId).getBoolean();
    }


    public boolean setPrinter(Variant labelId, String printerName) {
        return Dispatch.call(component, "LabelSetPrinter", labelId, printerName).getBoolean();
    }

    public Set<String> getVariablesForLabel(Variant labelId) {
        Set<String> result = new HashSet<>();
        Variant count = getLabelVariablesCount(labelId);
        for (int i = 0; i < count.getInt(); i++) {
            result.add(getLabelVariableName(labelId, i));
        }
        return result;
    }

    public void printLabelVariables(Variant labelId) {
        Set<String> result = getVariablesForLabel(labelId);
        for (String str : result) {
            System.out.println(str);
        }
    }

    public long openLabel(File file) {
        if (file.exists()) {
            return openLabel(file.getAbsoluteFile());
        } else {
            return -1L;
        }
    }

    public boolean printLabel(Variant labelId, HashMap<String, String> map, int count) {
        sessionStart(labelId);
        for (Object o : map.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            setLabelVariableValue(labelId, (String) pair.getKey(), (String) pair.getValue());
        }
        sessionPrint(labelId, count);
        return sessionStop(labelId);
    }

    public void createImage(String labelFile, String saveFile, Dimension size) {
        Dispatch iNiceLabel = Dispatch.call(component, "LabelOpenEx", labelFile).getDispatch();

        Dispatch.call(iNiceLabel,
                "GetLabelPreview",
                new Variant(saveFile),
                new Variant(size.width),
                new Variant(size.height));
    }

    public void getLabelVariables(String labelFile) {
        Dispatch iNiceLabel = Dispatch.call(component, "LabelOpenEx", labelFile).getDispatch();

        Dispatch variables = Dispatch.call(iNiceLabel, "Variables").getDispatch();
        Variant count = Dispatch.call(variables, "Count");
        System.out.println(count);
    }
}
