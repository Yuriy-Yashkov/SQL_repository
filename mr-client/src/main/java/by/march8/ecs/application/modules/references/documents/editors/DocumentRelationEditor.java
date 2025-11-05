package by.march8.ecs.application.modules.references.documents.editors;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.BoundsPopupMenuListener;
import by.gomel.freedev.ucframework.ucswing.ComponentConfiguration;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.documents.DocumentEntity;
import by.march8.entities.documents.DocumentRelation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by suvarov on 04.12.14.
 */
public class DocumentRelationEditor extends EditingPane {
    private final ComboBoxPanel<DocumentEntity> cbpDocument;
    private final ComboBoxPanel<DocumentEntity> cbpDocumentRel;

    private DocumentRelation source = new DocumentRelation();
    private DocumentEntity document;
    private DocumentEntity documentRel;

    public DocumentRelationEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(400, 170));
        this.setLayout(new MigLayout());
        final JLabel clDocument = new JLabel("* Выберите основной документ");
        final JLabel clDocumentRel = new JLabel("Выберите подчинённый документ");
        ComponentConfiguration.fontPatch(clDocument, clDocumentRel);
        cbpDocument = new ComboBoxPanel<>(true, reference.getMainController(),
                MarchReferencesType.SETTINGS_DOCUMENT);
        cbpDocumentRel = new ComboBoxPanel<>(true, reference.getMainController(),
                MarchReferencesType.SETTINGS_DOCUMENT);
        add(clDocument, "wrap");
        add(cbpDocument, "w 400::350,wrap");
        add(clDocumentRel, "wrap");
        add(cbpDocumentRel, "w 400::350, wrap");
        initEvents();
    }

    @Override
    public Object getSourceEntity() {
        // source = new ConRegion();
        source.setDocument(document);
        source.setRelatedDocument(documentRel);
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {

        if (object == null) {
            source = new DocumentRelation();
            cbpDocument.setSelectedIndex(-1);
            cbpDocumentRel.setSelectedIndex(-1);
            cbpDocument.getItem(-1);
            cbpDocumentRel.getItem(-1);
        } else {
            this.source = (DocumentRelation) object;
            document = this.source.getDocument();
            documentRel = this.source.getRelatedDocument();
            cbpDocument.preset(document);
            cbpDocumentRel.preset(documentRel);
        }
    }

    @Override
    public boolean verificationData() {
        if (cbpDocument.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо выбрать основной документ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            cbpDocument.getComboBox().requestFocusInWindow();
            return false;
        }
        return true;
    }

    /**
     * Инициализация событий на панели
     */
    private void initEvents() {

        cbpDocument.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                document = cbpDocument.selectFromReference(false);
            }
        });

        cbpDocument.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                document = cbpDocument.getSelectedItem();
            }
        });
        cbpDocument.addButtonDefaultActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                cbpDocument.setSelectedIndex(-1);
            }
        });
        cbpDocument.addPopupMenuListener(new BoundsPopupMenuListener());

        cbpDocumentRel.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                documentRel = cbpDocumentRel.selectFromReference(false);
            }
        });

        cbpDocumentRel.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                documentRel = cbpDocumentRel.getSelectedItem();
            }
        });
        cbpDocumentRel.addButtonDefaultActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                cbpDocumentRel.setSelectedIndex(-1);
            }
        });
        cbpDocumentRel.addPopupMenuListener(new BoundsPopupMenuListener());

    }
}
