package by.march8.ecs.application.modules.plan.editors;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.plan.PlanIdItem;
import by.march8.entities.plan.PlanItem;
import by.march8.entities.plan.TypeItem;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by dpliushchai on 19.11.2014.
 * Панель редактирования для логина om(маркетинг)
 */
@SuppressWarnings("all")
public class PlanMarketingEditor extends EditingPane {

    private static final long serialVersionUID = -3954031689774875650L;
    private final MainController mainController;
    private final ComboBoxPanel<TypeItem> cbpType;
    private JTextField tfassortment;
    private JTextField tfmodel;
    private JTextField tfsize;
    private JTextField tfplan;
    private JTextArea taNote;
    private JLabel lNote;
    private JLabel lassortment;
    private JLabel lmodel;
    private JLabel lsize;
    private JLabel lplan;
    private JLabel limage;
    private JLabel hidepath;
    private JLabel ioimage;
    private JLabel ltype;
    private JFileChooser fileChooser;
    private JButton openfile;
    //  private final ComboBoxPanelPlan<PlanIdItem> cbpId;
    private TypeItem typeItem = new TypeItem();
    private PlanIdItem planId = new PlanIdItem();


    private PlanItem source = new PlanItem();

    public PlanMarketingEditor(final MainController mainController) {
        this.mainController = mainController;
        setPreferredSize(new Dimension(280, 490));
        //  tfassortment = new JTextField();
        //   tfmodel = new JTextField();
        //  tfsize = new JTextField();
        //  tfplan = new JTextField();
        ioimage = new JLabel("");

        // lassortment = new JLabel("Ассортимент");
        //  lmodel = new JLabel("Модель");
        //  lsize = new JLabel("Размерная шкала");
        //   lplan = new JLabel("План");
        limage = new JLabel("Картинка");
        hidepath = new JLabel();
        ltype = new JLabel("Вид");

        openfile = new JButton("Выбрать картинку");

        cbpType = new ComboBoxPanel<>(mainController, MarchReferencesType.PLAN_TYPE);
        // cbpId = new ComboBoxPanelPlan<>(mainController, MarchPlanType.PLAN_NAME);

        taNote = new JTextArea();
        lNote = new JLabel("Примечание");

        //  cbpId.setBounds(10, 30, 250, 25);
        //   tfassortment.setBounds(10, 70, 250, 25);
        cbpType.setBounds(10, 28, 250, 25);
        //  tfmodel.setBounds(10, 150, 250, 25);
        //   tfsize.setBounds(10, 190, 250, 25);

        // lassortment.setBounds(10, 50, 100, 25);
        ltype.setBounds(10, 10, 100, 25);
        //    lmodel.setBounds(10, 130, 100, 25);
        //    lsize.setBounds(10, 170, 150, 25);

        //   tfplan.setBounds(10, 230, 250, 25);
        //   lplan.setBounds(10, 210, 100, 25);
//
        limage.setBounds(10, 60, 100, 25);
        ioimage.setBounds(10, 80, 250, 220);
        ioimage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        ioimage.setHorizontalAlignment(SwingConstants.CENTER);
        ioimage.setVerticalAlignment(SwingConstants.CENTER);
        hidepath.setBounds(10, 380, 1, 1);
        hidepath.setVisible(false);


        openfile.setBounds(35, 310, 200, 25);

        lNote.setBounds(10, 340, 250, 25);
        taNote.setBounds(10, 360, 250, 80);
        taNote.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        openfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();

                FileNameExtensionFilter filterJpg = new FileNameExtensionFilter("Jpg", "jpg");
                FileNameExtensionFilter filterPng = new FileNameExtensionFilter("Png", "png");
                fileopen.addChoosableFileFilter(filterJpg);
                fileopen.addChoosableFileFilter(filterPng);
                fileopen.setAcceptAllFileFilterUsed(false);
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen.getSelectedFile();
                    ImageIcon ii = new ImageIcon(file.getAbsolutePath());
                    ioimage.setIcon(new ImageIcon(ii.getImage().getScaledInstance(320, 300, ii.getImage().SCALE_DEFAULT)));
                    hidepath.setText(file.getAbsolutePath());
                }
            }
        });


        //   this.add(lassortment);
        //   this.add(lmodel);
        //    this.add(lsize);
        //    this.add(lplan);
        this.add(limage);


        //    this.add(tfassortment);
        //   this.add(tfmodel);
        //    this.add(tfsize);
        //   this.add(tfplan);
        this.add(ioimage);

        this.add(openfile);

        this.add(hidepath);
        this.add(ltype);
        this.add(cbpType);
        //  this.add(cbpId);

        this.add(lNote);
        this.add(taNote);

        initEvents();

//        this.add(fileChooser);

    }

    @Override
    public Object getSourceEntity() {

        //  source.setModel(Integer.valueOf(tfmodel.getText().trim()));
        //  source.setAssortmentName(tfassortment.getText().trim());
        //  source.setSize(tfsize.getText().trim());
        byte[] imageInByte;
        int IMG_WIDTH = 350, IMG_HEIGHT = 300;
        if (hidepath.getText() != "") {

            // System.out.println(path);
            File fnew = new File(hidepath.getText());
            BufferedImage originalImage = null;
            try {
                originalImage = ImageIO.read(fnew);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, originalImage.getType());
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(resizedImage, "jpg", baos);
                imageInByte = baos.toByteArray();
            } catch (IOException e) {
                imageInByte = new byte[1];
            }


            source.setImage_byte(imageInByte);
            source.setImage(hidepath.getText().trim());

        }


        // source.setType(typeItem.getId());
        source.setType(typeItem);
        source.setNote(taNote.getText());
        //  source.setPlanId(planId);


        return source;


    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            this.source = new PlanItem();
            typeItem = cbpType.getItem(0);
            //   planId = cbpId.getItem(0);
            tfassortment.setText("");
            //  tfmodel.setText("");
            //   tfsize.setText("8");
            //    tfplan.setText("");
            hidepath.setText("");
            ioimage.setText("");
            ioimage.setIcon(null);
            cbpType.preset(typeItem);
            //    cbpId.preset(planId);

        } else {
            ImageIcon ii = new ImageIcon();
            this.source = (PlanItem) object;
            typeItem = this.source.getType();
            //   planId = this.source.getPlanId();
            //   tfassortment.setText(String.valueOf(this.source.getAssortmentName()));
            //   tfmodel.setText(String.valueOf(this.source.getModel()));
            //   tfsize.setText(this.source.getSize());

            hidepath.setText(String.valueOf(this.source.getImage()));

            byte[] rec = this.source.getImage_byte();
            BufferedImage img = null;
            try {
                img = ImageIO.read(new ByteArrayInputStream(rec));
                ii = new ImageIcon(img);
                ioimage.setIcon(new ImageIcon(ii.getImage().getScaledInstance(350, 300, ii.getImage().SCALE_DEFAULT)));
            } catch (IOException | NullPointerException e) {
                // e.printStackTrace();
                img = null;
                ioimage.setIcon(null);

            }


            cbpType.preset(typeItem);
            taNote.setText(source.getNote());
            //   cbpId.preset(planId);
        }
    }

    @Override
    public boolean verificationData() {

        /*
        if (tfassortment.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Ассортимент не может быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfassortment.requestFocusInWindow();
            return false;
        }

        try {
            Integer.valueOf(tfmodel.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Модель\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfmodel.requestFocusInWindow();
            return false;
        }

        if (tfsize.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Размер не может быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfsize.requestFocusInWindow();
            return false;
        }

        try {
            Integer.valueOf(tfplan.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"План проекта\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfplan.requestFocusInWindow();
            return false;
        }

*/
        return true;
    }

    private void initEvents() {


        cbpType.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                typeItem = cbpType.getSelectedItem();
            }
        });
        //cbpId.addComboBoxActionListener(new ActionListener() {
        //       @Override
        //       public void actionPerformed(final ActionEvent e) {
        //           planId = cbpId.getSelectedItem();
        //       }
        //    });

    }
}