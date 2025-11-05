package by.march8.ecs.framework.sdk.reference;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReferenceRegion;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ControlPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.api.ISimpleReference;
import by.march8.ecs.application.modules.art.controlpane.ModelSampleControl;
import by.march8.ecs.application.modules.art.editor.ModelConfectionMapEditor;
import by.march8.ecs.application.modules.art.editor.ModelProtocolEditor;
import by.march8.ecs.application.modules.art.editor.ModelSampleEditor;
import by.march8.ecs.application.modules.references.client.editor.ClientEmailEditor;
import by.march8.ecs.application.modules.references.company.editor.CompanyDepartmentEditor;
import by.march8.ecs.application.modules.references.company.editor.EmployeeEditor;
import by.march8.ecs.application.modules.references.company.editor.EquipmentEditor;
import by.march8.ecs.application.modules.references.documents.editors.DocumentEditor;
import by.march8.ecs.application.modules.references.documents.editors.DocumentRelationEditor;
import by.march8.ecs.application.modules.references.documents.editors.DocumentTypeEditor;
import by.march8.ecs.application.modules.references.materals.controlpane.YarnControlPane;
import by.march8.ecs.application.modules.references.materals.editor.CanvasComponentEditor;
import by.march8.ecs.application.modules.references.materals.editor.CanvasItemEditor;
import by.march8.ecs.application.modules.references.materals.editor.CanvasModifierEditor;
import by.march8.ecs.application.modules.references.materals.editor.CanvasWeaveEditor;
import by.march8.ecs.application.modules.references.materals.editor.YarnCategoryEditor;
import by.march8.ecs.application.modules.references.materals.editor.YarnItemEditor;
import by.march8.ecs.application.modules.references.materals.editor.YarnTypeEditor;
import by.march8.ecs.application.modules.references.product.controlpane.ModelControl;
import by.march8.ecs.application.modules.references.product.controlpane.ModelSizeControl;
import by.march8.ecs.application.modules.references.product.editor.ModelEditor;
import by.march8.ecs.application.modules.references.product.editor.ModelMaterialEditor;
import by.march8.ecs.application.modules.references.product.editor.ModelSizeEditor;
import by.march8.ecs.application.modules.references.standard.editor.CodeTypeEditor;
import by.march8.ecs.application.modules.references.standard.editor.StandardEditor;
import by.march8.ecs.application.modules.references.standard.editor.UnitEditor;
import by.march8.ecs.application.shell.administration.controlpane.AdmRoleControl;
import by.march8.ecs.application.shell.administration.uicontrol.FunctionModeEditor;
import by.march8.ecs.application.shell.administration.uicontrol.RoleEditor;
import by.march8.ecs.framework.sdk.reference.uicontrols.SimpleReferenceEditor;

/**
 * В данном классе регистрируются контролирующие объекты справочников
 * Created by Andy on 21.12.14.
 */
public class ReferenceFactory implements IReferenceRegion {
    private EditingPane editingPane = null;
    private ControlPane controlPane = null;

    public ReferenceFactory(final IReference reference) {
        generateInstance(reference);
    }

    @Override
    public EditingPane getEditingPane() {
        return editingPane;
    }

    @Override
    public ControlPane getControlPane() {
        return controlPane;
    }

    @Override
    public void setControlPane(final ControlPane controlPane) {
        this.controlPane = controlPane;
    }

    /**
     * Возвращает ссылку на интерфейс
     * @return ссылка на интерфейс
     */
    public IReferenceRegion getReferenceRegion() {
        return this;
    }

    /**
     * Метод устанавливает для текущего типа справочника его модель таблицы и
     * его панель редактирования
     */
    private void generateInstance(final IReference reference) {
        switch (reference.getReferences()) {
            case MODEL_PRODUCT:
                editingPane = new ModelEditor(reference);
                controlPane = new ModelControl();
                break;
            case MODEL_MATERIAL:
                editingPane = new ModelMaterialEditor(reference);
                break;
            case MODEL_SIZE:
                editingPane = new ModelSizeEditor(reference, false);
                controlPane = new ModelSizeControl();
                break;
            case MODEL_CONFECTION_MAP:
                editingPane = new ModelConfectionMapEditor(reference);
                break;
            case MODEL_SAMPLE:
                editingPane = new ModelSampleEditor(reference);
                controlPane = new ModelSampleControl();
                break;
            case MODEL_PROTOCOL:
                editingPane = new ModelProtocolEditor(reference);
                break;
            case UNIT:
                editingPane = new UnitEditor(reference);
                break;
            case MATERIAL_CANVAS:
                editingPane = new CanvasItemEditor(reference);
                break;
            case EQUIPMENT:
                editingPane = new EquipmentEditor(reference);
                break;
            case MATERIAL_CANVAS_MODIFIER:
                editingPane = new CanvasModifierEditor(reference);
                break;
            case MATERIAL_CANVAS_WEAVE:
                editingPane = new CanvasWeaveEditor(reference);
                break;
            case MATERIAL_CANVAS_COMPONENT:
                editingPane = new CanvasComponentEditor(reference);
                break;
            case MATERIAL_YARN_TYPE:
                editingPane = new YarnTypeEditor(reference);
                break;
            case MATERIAL_YARN:
                editingPane = new YarnItemEditor(reference);
                controlPane = new YarnControlPane();
                break;
            case MATERIAL_YARN_CATEGORY:
                editingPane = new YarnCategoryEditor(reference);
                break;
            case COMPANY_EMPLOYEES:
                editingPane = new EmployeeEditor(reference);
                break;
            case COMPANY_POSITION:
                editingPane = new SimpleReferenceEditor(reference);
                break;
            case COMPANY_DEPARTMENTS:
                editingPane = new CompanyDepartmentEditor(reference);
                break;
            case CODELIST:
                editingPane = new StandardEditor(reference);
                break;
            case CODETYPE:
                editingPane = new CodeTypeEditor(reference);
                break;
            case ADM_ROLE:
                editingPane = new RoleEditor(reference);
                controlPane = new AdmRoleControl();
                break;
            case ADM_FUNCTION_MODE:
                editingPane = new FunctionModeEditor(reference);
                break;

            case CLIENT_EMAIL:
                editingPane = new ClientEmailEditor(reference);
                break;

            case SETTINGS_DOCUMENT_TYPE:
                editingPane = new DocumentTypeEditor(reference);
                break;
            case SETTINGS_DOCUMENT_RELATION:
                editingPane = new DocumentRelationEditor(reference);
                break;
            case SETTINGS_DOCUMENT:
                editingPane = new DocumentEditor(reference);
                break;


            default:
                break;
        }

        // Если панель редактирования не определена, проверяем
        // не является ли справочник простым
        //В таком случае панель редактирования будет простой
        if (editingPane == null) {
            Class c = reference.getReferences().getClassifierClass();
            Object source;
            try {
                // пытаемся созать объект данного класса
                source = c.newInstance();
                // ПРоверка , на реализацию интерфейса ISimpleReference
                if (source instanceof ISimpleReference) {
                    editingPane = new SimpleReferenceEditor(reference);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }


    }
}
