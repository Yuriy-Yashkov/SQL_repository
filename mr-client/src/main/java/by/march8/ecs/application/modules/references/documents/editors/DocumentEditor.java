package by.march8.ecs.application.modules.references.documents.editors;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.BoundsPopupMenuListener;
import by.gomel.freedev.ucframework.ucswing.ComponentConfiguration;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.filechooserfilters.FilterType;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.documents.DocumentEntity;
import by.march8.entities.documents.DocumentTypeEntity;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static by.gomel.freedev.ucframework.ucswing.filechooserfilters.ExtFileFilter.getExtension;
import static by.gomel.freedev.ucframework.ucswing.filechooserfilters.ExtFilter.addFiltersByDocumentType;


/**
 * Created by suvarov on 04.12.14.
 */
public class DocumentEditor extends EditingPane {
    final static int FIRST_FILTER_ELEMENT = 1;
    final static int FIRST_FILTER_EXTENSION_TYPE = 0;
    private static final long serialVersionUID = 1L;
    private final ComboBoxPanel<DocumentTypeEntity> cbpDocumentType;
    private final JTextArea tfName = new JTextArea();
    private final JTextArea tfNote = new JTextArea();
    private final JLabel lName = new JLabel("*Наименование документа");
    private final JLabel lNote = new JLabel("Примечание");
    private final JButton btnImport = new JButton(
            "Импорт");
    private final JButton btnExport = new JButton(
            "Экспорт");
    JLabel importLabel = new JLabel("Файл выбран");
    JLabel exportLabel = new JLabel("Файл сохранён");
    FileInputStream inputStream;
    FileOutputStream outputStream;
    private DocumentEntity source = new DocumentEntity();
    private DocumentTypeEntity documentType;
    private byte[] attachment;

    public DocumentEditor(final IReference reference) {
        super(reference);
        setLayout(new MigLayout());
        setPreferredSize(new Dimension(600, 200));
        ComponentConfiguration.fontPatch(lName, lNote);
        final JScrollPane saNote = new JScrollPane(tfNote);
        ComponentConfiguration.textAreaPatch(tfNote, saNote);
        final JScrollPane saName = new JScrollPane(tfName);
        ComponentConfiguration.textAreaPatch(tfName, saName);
        final JLabel clDocumentType = new JLabel(MarchReferencesType.SETTINGS_DOCUMENT_TYPE.getLabelName());
        ComponentConfiguration.fontPatch(clDocumentType, lName, lNote, btnExport, btnImport, importLabel, exportLabel);
        cbpDocumentType = new ComboBoxPanel<>(true, reference.getMainController(),
                MarchReferencesType.SETTINGS_DOCUMENT_TYPE);

        exportLabel.setForeground(Color.green.darker());
        importLabel.setForeground(Color.green.darker());
        importLabel.setVisible(false);
        exportLabel.setVisible(false);
        add(clDocumentType);
        add(cbpDocumentType, "w 400::350, wrap");
        add(lName);
        add(saName, "w 400::350, wrap");
        add(lNote);
        add(saNote, "w 400::350, wrap");
        add(importLabel);
        add(btnImport, "w 400::350, wrap");
        add(exportLabel);
        add(btnExport, "w 400::350,wrap");
        //add(btnImport,"w 300,span,align center,wrap");
        //add(btnExport,"w 300, span,align 50% 50%");
        initEvents();
    }

    @Override
    public Object getSourceEntity() {
        source.setName(tfName.getText());
        source.setNote(tfNote.getText());
        source.setAttachment(attachment);
        source.setDocumentType(documentType);
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            this.source = new DocumentEntity();
            tfName.setText("");
            tfNote.setText("");
            cbpDocumentType.setSelectedIndex(-1);
            cbpDocumentType.getItem(-1);
            btnExport.setVisible(false);
            exportLabel.setVisible(false);
            importLabel.setVisible(false);
            importLabel.setText("Файл выбран");
            attachment = null;
        } else {
            this.source = (DocumentEntity) object;
            documentType = this.source.getDocumentType();
            tfName.setText(this.source.getName());
            tfNote.setText(this.source.getNote());
            attachment = this.source.getAttachment();
            cbpDocumentType.preset(documentType);
            importLabel.setVisible(false);
            exportLabel.setVisible(false);
            if (attachment != null) btnExport.setVisible(true);
            else btnExport.setVisible(false);
        }
    }

    @Override
    public boolean verificationData() {

        if (tfName.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Наименование документа\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false;
        }
        return true;
    }

    public void initEvents() {
        btnImport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cbpDocumentType.getSelectedItem() != null) {
                    JFileChooser fileopen = new JFileChooser();
                    fileopen.setDialogTitle("Открыть файл");
                    //Добавляем типы файлов в список фильтров, по типу документа
                    if (documentType.getName().contains("Шаблон"))
                        addFiltersByDocumentType(fileopen, FilterType.TEMPLATES);
                    else if (documentType.getName().contains("Докумен") || documentType.getName().contains("Отчёт"))
                        addFiltersByDocumentType(fileopen, FilterType.DOCUMENTS);
                    else if (documentType.getName().contains("Изображ"))
                        addFiltersByDocumentType(fileopen, FilterType.IMAGES);
                    else
                        addFiltersByDocumentType(fileopen, FilterType.DOCUMENTS);
                    //fileopen.setApproveButtonText("Открыть");
                    int ret = fileopen.showDialog(null, "Открыть");

                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File file = fileopen.getSelectedFile();
                        try {
                            //Если в названии файла не указан тип и в фильтре выбрано 'All Files'
                            //выводим сообщение об ошибке 
                            if (getExtension(file) == null && (fileopen.getFileFilter().
                                    getDescription().equals("All Files"))) {
                                JOptionPane.showMessageDialog(fileopen,
                                        "Не указан/выбран тип файла",
                                        "Ошибка",
                                        JOptionPane.ERROR_MESSAGE);
                            } else if (getExtension(file) == null) {
                                //Если в названии файла не указан тип и указан тип в фильтре
                                //Проверка на наличие файла
                                if (new File(file + "." + ((FileNameExtensionFilter) fileopen.getFileFilter()).
                                        getExtensions()[FIRST_FILTER_EXTENSION_TYPE]).exists()) {
                                    inputStream = new FileInputStream(file + "." + ((FileNameExtensionFilter) fileopen.
                                            getFileFilter()).getExtensions()[FIRST_FILTER_EXTENSION_TYPE]);
                                    String fullFileName = "" + file.getName() + "." + ((FileNameExtensionFilter) fileopen.getFileFilter()).
                                            getExtensions()[FIRST_FILTER_EXTENSION_TYPE];
                                    attachment = new byte[(int) file.length()];
                                    inputStream.read(attachment);
                                    inputStream.close();
                                    importLabel.setText(fullFileName);
                                    importLabel.setVisible(true);
                                    //Если поле название документа пустое
                                    if (tfName.getText().trim().isEmpty()) tfName.setText(fullFileName);
                                } else
                                    JOptionPane.showMessageDialog(fileopen,
                                            "Файл отсутствует",
                                            "Ошибка",
                                            JOptionPane.ERROR_MESSAGE);
                            } else {
                                //Если файл выбран в ручную
                                inputStream = new FileInputStream(file);
                                attachment = new byte[(int) file.length()];
                                inputStream.read(attachment);
                                inputStream.close();
                                importLabel.setText(file.getName());
                                importLabel.setVisible(true);
                                if (tfName.getText().trim().isEmpty()) tfName.setText(file.getName());
                            }
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Поле \"Тип документа\" не должно быть пустым", "Ошибка!!!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rewrite = 0;
                JFileChooser dialog = new JFileChooser();
                dialog.setDialogTitle("Сохранить файл");
                //Добавляем типы файлов в список фильтров по типу документа
                if (documentType.getName().contains("Шаблон"))
                    addFiltersByDocumentType(dialog, FilterType.TEMPLATES);
                else if (documentType.getName().contains("Докумен"))
                    addFiltersByDocumentType(dialog, FilterType.DOCUMENTS);
                else if (documentType.getName().contains("Изображ"))
                    addFiltersByDocumentType(dialog, FilterType.IMAGES);
                else if (documentType.getName().contains("Отчёт"))
                    addFiltersByDocumentType(dialog, FilterType.DOCUMENTS);
                //Устанавливаем первый элемент фильтра текущим (первый после All Files)
                dialog.setFileFilter(dialog.getChoosableFileFilters()[FIRST_FILTER_ELEMENT]);
                dialog.setSelectedFile(new File(tfName.getText()));
                //dialog.setApproveButtonText("Сохранить");
                int ret = dialog.showDialog(null, "Сохранить");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    try {

                        File saveFile = dialog.getSelectedFile();
                        //Если в названии файла не указан тип и в фильтре выбрано 'All Files'
                        //выводим сообщение об ошибке
                        if (getExtension(saveFile) == null && (dialog.getFileFilter().
                                getDescription().equals("All Files"))) {
                            JOptionPane.showMessageDialog(dialog,
                                    "Не указан/выбран тип файла",
                                    "Ошибка",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        //Если в названии файла не указан тип, берём тип из фильтра
                        else if (getExtension(saveFile) == null) {
                            //Проверка на существование файла)
                            if (new File(saveFile + "." + ((FileNameExtensionFilter) dialog.getFileFilter()).
                                    getExtensions()[FIRST_FILTER_EXTENSION_TYPE]).exists()) {
                                rewrite = Dialogs.showRewriteDialog(saveFile.getName());
                            }
                            //Нажал Подтвердить
                            if (rewrite == 0) {
                                outputStream = new FileOutputStream(saveFile + "." + ((FileNameExtensionFilter) dialog.
                                        getFileFilter()).getExtensions()[FIRST_FILTER_EXTENSION_TYPE]);
                                outputStream.write(attachment);
                                outputStream.close();
                                exportLabel.setVisible(true);
                            }
                        }
                        //Если тип указан в названии файла
                        else {
                            //Если файл существует
                            if (saveFile.exists()) {
                                rewrite = Dialogs.showRewriteDialog(saveFile.getName());
                            }
                            //Нажал Подтвердить
                            if (rewrite == 0) {
                                outputStream = new FileOutputStream(saveFile);
                                outputStream.write(attachment);
                                outputStream.close();
                                exportLabel.setVisible(true);
                            }
                        }
                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }
                }
            }
        });
        cbpDocumentType.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                documentType = cbpDocumentType.selectFromReference(false);
            }
        });

        cbpDocumentType.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                documentType = cbpDocumentType.getSelectedItem();
            }
        });
        cbpDocumentType.addButtonDefaultActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                cbpDocumentType.setSelectedIndex(-1);
            }
        });
        cbpDocumentType.addPopupMenuListener(new BoundsPopupMenuListener());
    }
}
