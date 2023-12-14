package com.artframework.domain.plugin.component;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/14
 **/
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class CheckBoxRenderer implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null)
            return null;
        return (Component) new JCheckBox("",(boolean)value);
    }
}