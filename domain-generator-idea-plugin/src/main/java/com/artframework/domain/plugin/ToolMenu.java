package com.artframework.domain.plugin;

import com.artframework.domain.plugin.ui.DomainGeneratorDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;


/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/12
 **/
public class ToolMenu extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        try {
            Class.forName ("com.mysql.jdbc.Driver");
            Class.forName ("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        DomainGeneratorDialog dialog = null;
        dialog = new DomainGeneratorDialog(anActionEvent);
        dialog.setSize(1000,350);
        dialog.setLocationRelativeTo(null);//居中
        dialog.pack();
        dialog.setVisible(true);
    }
}
