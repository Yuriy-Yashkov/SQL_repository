package by.march8.ecs.application.modules.plan.render;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * @author Andy 22.03.2016.
 */
public class PlanPictureCellRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // возвращаем Jlabel на котором находится изображение картинки в строке
        try {
            JLabel jimage = new JLabel();
            if (value != null) {
                byte[] rec = (byte[]) value;
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(rec));
                ImageIcon ii = new ImageIcon(img);
                jimage.setIcon(new ImageIcon(ii.getImage().getScaledInstance(80, 57, ii.getImage().SCALE_DEFAULT)));
                jimage.setHorizontalAlignment(SwingConstants.CENTER);
                jimage.setVerticalAlignment(SwingConstants.CENTER);
                return jimage;
            } else {
                return new JLabel("");
            }
        } catch (Exception e) {
            return new JLabel("");
        }
    }
}
