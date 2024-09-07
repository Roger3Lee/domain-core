package com.artframework.domain.plugin.ui;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.customize.MyPostgreSqlQuery;
import com.artframework.domain.customize.MyPostgreSqlTypeConvert;
import com.artframework.domain.plugin.SettingsCache;
import com.artframework.domain.utils.GenerateUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.query.SQLQuery;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DomainGeneratorDialog extends JDialog {
    private AnActionEvent anActionEvent;
    private static final String MY_SQL = "Mysql";
    private static final String POLAR_DB = "Polar DB";
    private static final String PG = "Postgresql";


    private JPanel contentPane;
    private JButton btn_OK;
    private JButton btn_Cancel;
    private JTextField db_url;
    private JTextField db_user;
    private JPasswordField db_password;
    private JComboBox db_type;
    private JTextField t_schema;
    private JLabel l_schema;
    private JCheckBox chk_do;
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
    private JCheckBox chk_entity_over;
    private JCheckBox chk_mapper_over;
    private JCheckBox chk_domain_over;
    private JCheckBox chk_controller_over;
    private JCheckBox chk_mapper;
    private JButton btn_entity_save;
    private JButton btn_mapper_save;
    private JButton btn_domain_save;
    private JButton btn_controller_save;
    private JButton btn_test;

    public DomainGeneratorDialog(AnActionEvent anActionEvent) {
        this.anActionEvent = anActionEvent;
        this.setTitle("DDD代碼生成器");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btn_OK);

        btn_OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        btn_Cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

        l_schema.setVisible(false);
        t_schema.setVisible(false);

        btn_fileChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooseDialog(JFileChooser.FILES_ONLY, t_domainFile);
            }
        });
        btn_entity_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooseDialog(JFileChooser.DIRECTORIES_ONLY, t_entity_save);
            }
        });

        btn_mapper_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooseDialog(JFileChooser.DIRECTORIES_ONLY, t_mapper_save);
            }
        });
        btn_domain_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooseDialog(JFileChooser.DIRECTORIES_ONLY, t_domain_save);
            }
        });
        btn_controller_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooseDialog(JFileChooser.DIRECTORIES_ONLY, t_controller_save);
            }
        });

        chk_domain.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!chk_domain.isSelected()) {
                    setDisable(t_domain_package);
                    setDisable(t_domain_save);
                    setDisable(chk_domain_over);
                } else {
                    setEnable(t_domain_package);
                    setEnable(t_domain_save);
                    setEnable(chk_domain_over);
                }
            }
        });

        chk_controller.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!chk_controller.isSelected()) {
                    setDisable(t_controller_package);
                    setDisable(t_controller_save);
                    setDisable(chk_controller_over);
                } else {
                    setEnable(t_controller_package);
                    setEnable(t_controller_save);
                    setEnable(chk_controller_over);
                }
            }
        });

        chk_do.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!chk_do.isSelected()) {
                    setDisable(t_eneity_package);
                    setDisable(t_entity_save);
                    setDisable(chk_entity_over);
                } else {
                    setEnable(t_eneity_package);
                    setEnable(t_entity_save);
                    setEnable(chk_entity_over);
                }
            }
        });
        chk_mapper.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!chk_mapper.isSelected()) {
                    setDisable(t_mapper_package);
                    setDisable(t_mapper_save);
                    setDisable(chk_mapper_over);
                } else {
                    setEnable(t_mapper_package);
                    setEnable(t_mapper_save);
                    setEnable(chk_mapper_over);
                }
            }
        });

        btn_test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test_connect();
            }
        });

        initView();
    }

    private void setEnable(JCheckBox chk_domain_over) {
        chk_domain_over.enable();
    }

    private void setDisable(JCheckBox chk_domain_over) {
        chk_domain_over.disable();
    }


    public void setDisable(JTextField textField) {
        textField.disable();
        textField.setDisabledTextColor(Color.white);
    }

    public void setEnable(JTextField textField) {
        textField.enable();
    }

    private void showFileChooseDialog(Integer selectMode, JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(selectMode);
        fileChooser.setMultiSelectionEnabled(false);
        int result = fileChooser.showDialog(this, "選擇");
        if (result == JFileChooser.APPROVE_OPTION) {
            textField.setText(fileChooser.getSelectedFile().getPath());
        }
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

    public void test_connect() {
        try {
            DataSourceConfig.Builder builder = new DataSourceConfig
                    .Builder(db_url.getText(), db_user.getText(), String.valueOf(db_password.getPassword()));
            if (Objects.equals(db_type.getSelectedItem(), MY_SQL)) {
                builder.dbQuery(new MySqlQuery())
                        .typeConvert(new MySqlTypeConvert())
                        .keyWordsHandler(new MySqlKeyWordsHandler())
                        .databaseQueryClass(SQLQuery.class);
            } else if (Objects.equals(db_type.getSelectedItem(), POLAR_DB) || Objects.equals(db_type.getSelectedItem(), PG)) {
                builder.dbQuery(new MyPostgreSqlQuery(t_schema.getText()))
                        .schema(t_schema.getText())
                        .typeConvert(new MyPostgreSqlTypeConvert())
                        .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                        .addConnectionProperty("currentSchema",t_schema.getText())
                        .databaseQueryClass(SQLQuery.class);
            }

            DataSourceConfig dataSourceConfig = builder.build();
            dataSourceConfig.getConn();

            Messages.showInfoMessage(this.contentPane, "链接成功", "提示");
        } catch (Exception ex) {
//            Notification
            Messages.showErrorDialog(this.contentPane, ex.getMessage(), "错误");
        }
    }

    private void generate() {
        try {
            DataSourceConfig.Builder builder = new DataSourceConfig
                    .Builder(db_url.getText(), db_user.getText(), String.valueOf(db_password.getPassword()));
            if (Objects.equals(db_type.getSelectedItem(), MY_SQL)) {
                builder.dbQuery(new MySqlQuery())
                        .typeConvert(new MySqlTypeConvert())
                        .keyWordsHandler(new MySqlKeyWordsHandler())
                        .databaseQueryClass(SQLQuery.class);
            } else if (Objects.equals(db_type.getSelectedItem(), POLAR_DB) || Objects.equals(db_type.getSelectedItem(), PG)) {
                builder.dbQuery(new MyPostgreSqlQuery(t_schema.getText()))
                        .schema(t_schema.getText())
                        .typeConvert(new MyPostgreSqlTypeConvert())
                        .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                        .addConnectionProperty("currentSchema",t_schema.getText())
                        .databaseQueryClass(SQLQuery.class);
            }

            DataSourceConfig dataSourceConfig = builder.build();

            GlobalSetting.loadFromDB(dataSourceConfig,
                    new File(t_domainFile.getText()));

            Map<String, String> packageParam = new HashMap<>();
            packageParam.put("tablePackage", t_eneity_package.getText());
            packageParam.put("mapperPackage", t_mapper_package.getText());
            packageParam.put("domainPackage", t_domain_package.getText());
            packageParam.put("controllerPackage", t_controller_package.getText());

            if(chk_do.isSelected() ||chk_mapper.isSelected()){
                GenerateUtils.generateTables(t_mapper_save.getText()
                        , t_entity_save.getText()
                        , GlobalSetting.INSTANCE.getTableList(), packageParam, chk_entity_over.isSelected(), chk_mapper_over.isSelected());
            }
            if(chk_domain.isSelected()){
                GenerateUtils.generateDomains(t_domain_save.getText(),
                        GlobalSetting.INSTANCE.getDomainList(), packageParam,chk_domain_over.isSelected());
            }
            if (chk_controller.isSelected()) {
                GenerateUtils.generateController(t_controller_save.getText(),
                        GlobalSetting.INSTANCE.getDomainList(), packageParam,chk_controller_over.isSelected());
            }
            Messages.showInfoMessage(this.contentPane, "生成成功", "提示");
        } catch (Exception ex) {
//            Notification
            Messages.showErrorDialog(this.contentPane, ex.getMessage(), "错误");
        }
    }

    private void onOK() {
        // add your code here
        generate();
    }


    private void initView() {
        Map<String, String> cacheMap = SettingsCache.getInstance(anActionEvent.getProject()).cacheMap;
        db_url.setText(cacheMap.getOrDefault("url", ""));
        db_user.setText(cacheMap.getOrDefault("user", ""));
        db_password.setText(cacheMap.getOrDefault("password", ""));
        t_schema.setText(cacheMap.getOrDefault("schema", ""));
        db_type.setSelectedItem(cacheMap.getOrDefault("type", POLAR_DB));

        t_eneity_package.setText(cacheMap.getOrDefault("t_eneity_package", ""));
        t_mapper_package.setText(cacheMap.getOrDefault("t_mapper_package", ""));
        t_domain_package.setText(cacheMap.getOrDefault("t_domain_package", ""));
        t_controller_package.setText(cacheMap.getOrDefault("t_controller_package", ""));
        t_entity_save.setText(cacheMap.getOrDefault("t_entity_save", ""));
        t_mapper_save.setText(cacheMap.getOrDefault("t_mapper_save", ""));
        t_domain_save.setText(cacheMap.getOrDefault("t_domain_save", ""));
        t_controller_save.setText(cacheMap.getOrDefault("t_controller_save", ""));

        t_domainFile.setText(cacheMap.getOrDefault("t_domainFile", ""));

        chk_mapper_over.setSelected(Boolean.parseBoolean(cacheMap.getOrDefault("chk_mapper_over", Boolean.toString(false))));
        chk_entity_over.setSelected(Boolean.parseBoolean(cacheMap.getOrDefault("chk_entity_over", Boolean.toString(false))));
        chk_domain_over.setSelected(Boolean.parseBoolean(cacheMap.getOrDefault("chk_domain_over", Boolean.toString(false))));
        chk_controller_over.setSelected(Boolean.parseBoolean(cacheMap.getOrDefault("chk_controller_over", Boolean.toString(false))));
        chk_mapper.setSelected(Boolean.parseBoolean(cacheMap.getOrDefault("chk_mapper", Boolean.toString(false))));
        chk_do.setSelected(Boolean.parseBoolean(cacheMap.getOrDefault("chk_entity", Boolean.toString(true))));
        chk_domain.setSelected(Boolean.parseBoolean(cacheMap.getOrDefault("chk_domain", Boolean.toString(true))));
        chk_controller.setSelected(Boolean.parseBoolean(cacheMap.getOrDefault("chk_controller", Boolean.toString(false))));
    }
    @Override
    public void dispose() {
        // add your code here if necessary
        Map<String, String> cacheMap = SettingsCache.getInstance(anActionEvent.getProject()).cacheMap;
        cacheMap.put("url", db_url.getText());
        cacheMap.put("user", db_user.getText());
        cacheMap.put("password", db_password.getText());
        cacheMap.put("schema", t_schema.getText());
        cacheMap.put("type", db_type.getSelectedItem().toString());

        cacheMap.put("t_eneity_package", t_eneity_package.getText());
        cacheMap.put("t_mapper_package", t_mapper_package.getText());
        cacheMap.put("t_domain_package", t_domain_package.getText());
        cacheMap.put("t_controller_package", t_controller_package.getText());
        cacheMap.put("t_entity_save", t_entity_save.getText());
        cacheMap.put("t_mapper_save", t_mapper_save.getText());
        cacheMap.put("t_domain_save", t_domain_save.getText());
        cacheMap.put("t_controller_save", t_controller_save.getText());

        cacheMap.put("t_domainFile", t_domainFile.getText());
        cacheMap.put("chk_mapper_over", Boolean.toString(chk_mapper_over.isSelected()));
        cacheMap.put("chk_entity_over", Boolean.toString(chk_entity_over.isSelected()));
        cacheMap.put("chk_domain_over", Boolean.toString(chk_domain_over.isSelected()));
        cacheMap.put("chk_controller_over", Boolean.toString(chk_controller_over.isSelected()));
        cacheMap.put("chk_mapper", Boolean.toString(chk_mapper.isSelected()));
        cacheMap.put("chk_entity", Boolean.toString(chk_do.isSelected()));
        cacheMap.put("chk_domain", Boolean.toString(chk_domain.isSelected()));
        cacheMap.put("chk_controller", Boolean.toString(chk_controller.isSelected()));
        super.dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
