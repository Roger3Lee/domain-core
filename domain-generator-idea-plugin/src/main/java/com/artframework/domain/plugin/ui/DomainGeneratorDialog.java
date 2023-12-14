package com.artframework.domain.plugin.ui;

import com.artframework.domain.datasource.TableQuery;
import com.artframework.domain.plugin.component.CheckBoxRenderer;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.query.SQLQuery;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;
import com.intellij.ui.SideBorder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DomainGeneratorDialog extends JDialog {
    private static final String MY_SQL = "Mysql";
    private static final String POLAR_DB = "Polar DB";
    private static final String PG = "Postgresql";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField db_url;
    private JTable db_tables;
    private JTextField db_user;
    private JPasswordField db_password;
    private JButton btn_loadTables;
    private JComboBox db_type;
    private JTextField t_schema;
    private JLabel l_schema;
    private JCheckBox chk_mapper;
    private JCheckBox chk_domain;
    private JTextField t_domainFile;
    private JButton btn_fileChoose;
    private JTextField t_mapper_package;
    private JTextField t_eneity_package;
    private JTextField t_entity_save;
    private JTextField t_mapper_save;
    private JTextField t_domain_save;
    private JTextField t_domain_package;
    private JTextField t_controller_package;
    private JTextField t_controller_save;
    private JCheckBox chk_controller;


    public DomainGeneratorDialog() {
        this.setTitle("DDD代碼生成器");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        btn_loadTables.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTables();
            }
        });

        db_type.addItem(MY_SQL);
        db_type.addItem(POLAR_DB);
        db_type.addItem(PG);
        db_type.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    onDbTypeChange(db_type.getSelectedItem().toString());
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //
        initTableGrid(new ArrayList<>());
        l_schema.setVisible(false);
        t_schema.setVisible(false);

        btn_fileChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooseDialog();
            }
        });


        db_tables.setBorder(new SideBorder(JBColor.BLACK, SideBorder.ALL));
        db_tables.setVisible(true);

        chk_domain.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!chk_domain.isSelected()) {
                    setDisable(t_domain_package);
                    setDisable(t_domain_save);
                } else {
                    setEnable(t_domain_package);
                    setEnable( t_domain_save);
                }
            }
        });

        chk_controller.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!chk_controller.isSelected()) {
                    setDisable(t_controller_package);
                    setDisable(t_controller_save);
                } else {
                    setEnable(t_controller_package);
                    setEnable(t_controller_save);
                }
            }
        });

        chk_mapper.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!chk_mapper.isSelected()) {
                    setDisable(t_eneity_package);
                    setDisable(t_entity_save);
                    setDisable(t_mapper_package);
                    setDisable(t_mapper_save);
                } else {
                    setEnable(t_eneity_package);
                    setEnable(t_entity_save);
                    setEnable(t_mapper_package);
                    setEnable(t_mapper_save);
                }
            }
        });
    }

    public  void setDisable(JTextField textField){
        textField.disable();
        textField.setDisabledTextColor(Color.white);
    }
    public  void setEnable(JTextField textField){
        textField.enable();
    }

    private void showFileChooseDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        int result = fileChooser.showDialog(null, "選擇文件");
        if (result == JFileChooser.APPROVE_OPTION) {
            t_domainFile.setText(fileChooser.getSelectedFile().getPath());
        }
    }

    private void initTableGrid(List<TableInfo> objects) {
        String[] columnNames = {"選擇", "表名", "描述"};
        Object[][] tableData = new Object[][]{};
        for (int i = 0; i < objects.size(); i++) {
            tableData[i + 1][1] = false;
            tableData[i + 1][2] = objects.get(i).getName();
            tableData[i + 1][3] = objects.get(i).getComment();
            tableData[i + 1][4] = objects.get(i);
        }

        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames);
//        db_tables = new JBTable(tableModel);
        db_tables.setModel(tableModel);
        db_tables.getColumnModel().getColumn(0).setWidth(80);
        db_tables.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxRenderer());
        db_tables.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
    }

    private void onDbTypeChange(String selectedItem) {
        if (selectedItem.equals(MY_SQL)) {
            l_schema.setVisible(false);
            t_schema.setVisible(false);
        } else {
            l_schema.setVisible(true);
            t_schema.setVisible(true);
        }
    }

    private void loadTables() {
        try {
            DataSourceConfig.Builder builder = new DataSourceConfig
                    .Builder(db_url.getText(), db_user.getText(), String.valueOf(db_password.getPassword()));
            if (db_type.getSelectedItem().equals(MY_SQL)) {
                builder.dbQuery(new MySqlQuery())
                        .typeConvert(new MySqlTypeConvert())
                        .keyWordsHandler(new MySqlKeyWordsHandler())
                        .databaseQueryClass(SQLQuery.class);
            } else if (db_type.getSelectedItem().equals(POLAR_DB) || db_type.getSelectedItem().equals(PG)) {
                builder.dbQuery(new PostgreSqlQuery())
                        .schema(t_schema.getText())
                        .typeConvert(new PostgreSqlTypeConvert())
                        .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                        .databaseQueryClass(SQLQuery.class);
            }

            DataSourceConfig dataSourceConfig = builder.build();
            TableQuery tableQuery = new TableQuery(dataSourceConfig);
            initTableGrid(tableQuery.queryTables());
        } catch (Exception ex) {
            Messages.showErrorDialog(ex.getMessage(), "錯誤");
        }
    }

    private void onOK() {
        // add your code here

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
