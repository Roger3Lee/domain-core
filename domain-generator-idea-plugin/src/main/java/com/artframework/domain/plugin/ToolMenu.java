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

        DomainGeneratorDialog dialog = new DomainGeneratorDialog();
        dialog.setLocationRelativeTo(dialog);//居中
        dialog.pack();
        dialog.setVisible(true);
    }
}
