
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class LawenaView extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JComboBox<String> cmbSkybox;
    private JComboBox<String> cmbResolution;
    private JComboBox<String> cmbHud;
    private JComboBox<String> cmbFramerate;
    private JComboBox<String> cmbQuality;
    private JSpinner spinnerViewmodelFov;
    private JCheckBox enableMotionBlur;
    private JCheckBox disableCrosshair;
    private JCheckBox disableCrosshairSwitch;
    private JCheckBox disableCombatText;
    private JCheckBox disableHitSounds;
    private JCheckBox disableVoiceChat;
    private JButton btnStartTf;
    private JLabel lblResolution;
    private JLabel lblFrameRate;
    private JButton btnSaveSettings;
    private JButton btnClearMovieFolder;
    private JPanel panelBottomLeft;
    private JLabel lblSkyboxPreview;
    private JTable tableCustomContent;
    private JScrollPane scrollPane;
    private JScrollPane scrollPane_2;
    private JTextArea textAreaLog;
    private JTabbedPane tabbedPane;
    private JMenuBar menuBar;
    private JMenu mnFile;
    private JMenuItem mntmChangeTfDirectory;
    private JMenuItem mntmChangeMovieDirectory;
    private JPanel panelStatusbar;
    private JLabel lblStatus;
    private JPanel panelCheckboxes;
    private JPanel panelCustomContent;
    private JComboBox<String> cmbViewmodel;
    private Component verticalStrut;
    private JLabel lblPreview;
    private Component horizontalStrut;
    private JPanel panelBottomRight;
    private JProgressBar progressBar;

    /**
     * Create the frame.
     */
    public LawenaView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnFile = new JMenu(" File ");
        menuBar.add(mnFile);

        mntmChangeTfDirectory = new JMenuItem("Change TF2 directory...");
        mnFile.add(mntmChangeTfDirectory);

        mntmChangeMovieDirectory = new JMenuItem("Change Movie directory...");
        mnFile.add(mntmChangeMovieDirectory);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setPreferredSize(new Dimension(650, 400));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panelSettings = new JPanel();
        tabbedPane.addTab("Settings", null, panelSettings, null);
        GridBagLayout gbl_panelSettings = new GridBagLayout();
        gbl_panelSettings.columnWidths = new int[] {
                0, 1, 0, 1, 1, 0
        };
        gbl_panelSettings.rowHeights = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
        gbl_panelSettings.columnWeights = new double[] {
                0.0, 1.0, 0.0, 1.0, 10.0
        };
        gbl_panelSettings.rowWeights = new double[] {
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE
        };
        panelSettings.setLayout(gbl_panelSettings);

        lblResolution = new JLabel("Resolution:");
        GridBagConstraints gbc_lblResolution = new GridBagConstraints();
        gbc_lblResolution.insets = new Insets(5, 5, 5, 5);
        gbc_lblResolution.anchor = GridBagConstraints.EAST;
        gbc_lblResolution.gridx = 0;
        gbc_lblResolution.gridy = 0;
        panelSettings.add(lblResolution, gbc_lblResolution);

        cmbResolution = new JComboBox<>();
        cmbResolution.setModel(new DefaultComboBoxModel<String>(new String[] {
                "640x360", "854x480", "1280x720", "1920x1080"
        }));
        cmbResolution.setEditable(true);
        GridBagConstraints gbc_cmbResolution = new GridBagConstraints();
        gbc_cmbResolution.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmbResolution.insets = new Insets(5, 0, 5, 5);
        gbc_cmbResolution.gridx = 1;
        gbc_cmbResolution.gridy = 0;
        panelSettings.add(cmbResolution, gbc_cmbResolution);

        lblFrameRate = new JLabel("FPS:");
        GridBagConstraints gbc_lblFrameRate = new GridBagConstraints();
        gbc_lblFrameRate.anchor = GridBagConstraints.EAST;
        gbc_lblFrameRate.insets = new Insets(5, 0, 5, 5);
        gbc_lblFrameRate.gridx = 2;
        gbc_lblFrameRate.gridy = 0;
        panelSettings.add(lblFrameRate, gbc_lblFrameRate);

        cmbFramerate = new JComboBox<>();
        cmbFramerate.setModel(new DefaultComboBoxModel<String>(new String[] {
                "60", "120", "240", "480", "960", "1920", "3840"
        }));
        cmbFramerate.setEditable(true);
        GridBagConstraints gbc_cmbFramerate = new GridBagConstraints();
        gbc_cmbFramerate.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmbFramerate.insets = new Insets(5, 0, 5, 5);
        gbc_cmbFramerate.gridx = 3;
        gbc_cmbFramerate.gridy = 0;
        panelSettings.add(cmbFramerate, gbc_cmbFramerate);

        panelCustomContent = new JPanel();
        panelCustomContent.setBorder(new TitledBorder(null, "Custom Content", TitledBorder.LEADING,
                TitledBorder.TOP, null, null));
        GridBagConstraints gbc_panelCustomContent = new GridBagConstraints();
        gbc_panelCustomContent.insets = new Insets(0, 0, 5, 0);
        gbc_panelCustomContent.gridwidth = 2;
        gbc_panelCustomContent.gridheight = 10;
        gbc_panelCustomContent.fill = GridBagConstraints.BOTH;
        gbc_panelCustomContent.gridx = 4;
        gbc_panelCustomContent.gridy = 0;
        panelSettings.add(panelCustomContent, gbc_panelCustomContent);
        GridBagLayout gbl_panelCustomContent = new GridBagLayout();
        gbl_panelCustomContent.columnWidths = new int[] {
                0, 0
        };
        gbl_panelCustomContent.rowHeights = new int[] {
                0, 0, 0
        };
        gbl_panelCustomContent.columnWeights = new double[] {
                1.0, Double.MIN_VALUE
        };
        gbl_panelCustomContent.rowWeights = new double[] {
                1.0, 0.0, Double.MIN_VALUE
        };
        panelCustomContent.setLayout(gbl_panelCustomContent);

        scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        panelCustomContent.add(scrollPane, gbc_scrollPane);

        tableCustomContent = new JTable();
        tableCustomContent.setTableHeader(null);
        tableCustomContent.setGridColor(new Color(0, 0, 0, 30));
        tableCustomContent.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        tableCustomContent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(tableCustomContent);

        JLabel lblHud = new JLabel("HUD:");
        GridBagConstraints gbc_lblHud = new GridBagConstraints();
        gbc_lblHud.anchor = GridBagConstraints.EAST;
        gbc_lblHud.insets = new Insets(0, 5, 5, 5);
        gbc_lblHud.gridx = 0;
        gbc_lblHud.gridy = 1;
        panelSettings.add(lblHud, gbc_lblHud);

        cmbHud = new JComboBox<>();
        cmbHud.setModel(new DefaultComboBoxModel<>(new String[] {
                "Minimal (kill notices)", "Medic (hp, ubercharge, cp)", "Full", "Custom"
        }));
        GridBagConstraints gbc_cmbHud = new GridBagConstraints();
        gbc_cmbHud.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmbHud.insets = new Insets(0, 0, 5, 5);
        gbc_cmbHud.gridx = 1;
        gbc_cmbHud.gridy = 1;
        panelSettings.add(cmbHud, gbc_cmbHud);

        JLabel lblDxLevel = new JLabel("Quality:");
        GridBagConstraints gbc_lblDxLevel = new GridBagConstraints();
        gbc_lblDxLevel.anchor = GridBagConstraints.EAST;
        gbc_lblDxLevel.insets = new Insets(0, 5, 5, 5);
        gbc_lblDxLevel.gridx = 2;
        gbc_lblDxLevel.gridy = 1;
        panelSettings.add(lblDxLevel, gbc_lblDxLevel);

        cmbQuality = new JComboBox<>();
        cmbQuality.setModel(new DefaultComboBoxModel<String>(new String[] {
                "DirectX 8.0 (Lowest)", "DirectX 8.1 (Low)", "DirectX 9.0 (Medium)",
                "DirectX 9.5 (High)", "DirectX 9.8 (Highest)"
        }));
        GridBagConstraints gbc_cmbQuality = new GridBagConstraints();
        gbc_cmbQuality.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmbQuality.insets = new Insets(0, 0, 5, 5);
        gbc_cmbQuality.gridx = 3;
        gbc_cmbQuality.gridy = 1;
        panelSettings.add(cmbQuality, gbc_cmbQuality);

        JLabel lblSkybox = new JLabel("Skybox:");
        GridBagConstraints gbc_lblSkybox = new GridBagConstraints();
        gbc_lblSkybox.anchor = GridBagConstraints.EAST;
        gbc_lblSkybox.insets = new Insets(0, 5, 5, 5);
        gbc_lblSkybox.gridx = 0;
        gbc_lblSkybox.gridy = 2;
        panelSettings.add(lblSkybox, gbc_lblSkybox);

        cmbSkybox = new JComboBox<>();
        GridBagConstraints gbc_cmbSkybox = new GridBagConstraints();
        gbc_cmbSkybox.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmbSkybox.insets = new Insets(0, 0, 5, 5);
        gbc_cmbSkybox.gridx = 1;
        gbc_cmbSkybox.gridy = 2;
        panelSettings.add(cmbSkybox, gbc_cmbSkybox);

        lblPreview = new JLabel("");
        GridBagConstraints gbc_lblPreview = new GridBagConstraints();
        gbc_lblPreview.anchor = GridBagConstraints.EAST;
        gbc_lblPreview.insets = new Insets(0, 0, 5, 5);
        gbc_lblPreview.gridx = 2;
        gbc_lblPreview.gridy = 2;
        panelSettings.add(lblPreview, gbc_lblPreview);

        lblSkyboxPreview = new JLabel("");
        GridBagConstraints gbc_lblSkyboxPreview = new GridBagConstraints();
        gbc_lblSkyboxPreview.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblSkyboxPreview.gridheight = 4;
        gbc_lblSkyboxPreview.insets = new Insets(0, 0, 5, 5);
        gbc_lblSkyboxPreview.gridx = 3;
        gbc_lblSkyboxPreview.gridy = 2;
        panelSettings.add(lblSkyboxPreview, gbc_lblSkyboxPreview);

        JLabel lblViewmodels = new JLabel("Viewmodels:");
        GridBagConstraints gbc_lblViewmodels = new GridBagConstraints();
        gbc_lblViewmodels.anchor = GridBagConstraints.EAST;
        gbc_lblViewmodels.insets = new Insets(0, 5, 5, 5);
        gbc_lblViewmodels.gridx = 0;
        gbc_lblViewmodels.gridy = 3;
        panelSettings.add(lblViewmodels, gbc_lblViewmodels);

        cmbViewmodel = new JComboBox<>();
        cmbViewmodel.setModel(new DefaultComboBoxModel<String>(new String[] {
                "On", "Off", "Default"
        }));
        GridBagConstraints gbc_cmbViewmodel = new GridBagConstraints();
        gbc_cmbViewmodel.insets = new Insets(0, 0, 5, 5);
        gbc_cmbViewmodel.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmbViewmodel.gridx = 1;
        gbc_cmbViewmodel.gridy = 3;
        panelSettings.add(cmbViewmodel, gbc_cmbViewmodel);

        horizontalStrut = Box.createHorizontalStrut(24);
        GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
        gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalStrut.gridx = 2;
        gbc_horizontalStrut.gridy = 3;
        panelSettings.add(horizontalStrut, gbc_horizontalStrut);

        JLabel lblViewmodelFov = new JLabel("Viewmodel FOV:");
        GridBagConstraints gbc_lblViewmodelFov = new GridBagConstraints();
        gbc_lblViewmodelFov.anchor = GridBagConstraints.EAST;
        gbc_lblViewmodelFov.insets = new Insets(0, 5, 5, 5);
        gbc_lblViewmodelFov.gridx = 0;
        gbc_lblViewmodelFov.gridy = 4;
        panelSettings.add(lblViewmodelFov, gbc_lblViewmodelFov);

        spinnerViewmodelFov = new JSpinner();
        GridBagConstraints gbc_spinnerViewmodelFov = new GridBagConstraints();
        gbc_spinnerViewmodelFov.anchor = GridBagConstraints.WEST;
        gbc_spinnerViewmodelFov.insets = new Insets(0, 0, 5, 5);
        gbc_spinnerViewmodelFov.gridx = 1;
        gbc_spinnerViewmodelFov.gridy = 4;
        panelSettings.add(spinnerViewmodelFov, gbc_spinnerViewmodelFov);
        spinnerViewmodelFov.setModel(new SpinnerNumberModel(70, 55, 90, 1));

        verticalStrut = Box.createVerticalStrut(22);
        GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
        gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_verticalStrut.gridx = 0;
        gbc_verticalStrut.gridy = 5;
        panelSettings.add(verticalStrut, gbc_verticalStrut);

        panelCheckboxes = new JPanel();
        panelCheckboxes.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
                "Additional Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GridBagConstraints gbc_panelCheckboxes = new GridBagConstraints();
        gbc_panelCheckboxes.insets = new Insets(0, 0, 5, 5);
        gbc_panelCheckboxes.gridheight = 4;
        gbc_panelCheckboxes.gridwidth = 4;
        gbc_panelCheckboxes.fill = GridBagConstraints.BOTH;
        gbc_panelCheckboxes.gridx = 0;
        gbc_panelCheckboxes.gridy = 6;
        panelSettings.add(panelCheckboxes, gbc_panelCheckboxes);
        GridBagLayout gbl_panelCheckboxes = new GridBagLayout();
        gbl_panelCheckboxes.columnWidths = new int[] {
                0, 0, 0
        };
        gbl_panelCheckboxes.rowHeights = new int[] {
                0, 0, 0, 0
        };
        gbl_panelCheckboxes.columnWeights = new double[] {
                1.0, 1.0, Double.MIN_VALUE
        };
        gbl_panelCheckboxes.rowWeights = new double[] {
                0.0, 0.0, 0.0, Double.MIN_VALUE
        };
        panelCheckboxes.setLayout(gbl_panelCheckboxes);

        enableMotionBlur = new JCheckBox("Enable Motion Blur");
        GridBagConstraints gbc_enableMotionBlur = new GridBagConstraints();
        gbc_enableMotionBlur.insets = new Insets(0, 0, 0, 5);
        gbc_enableMotionBlur.anchor = GridBagConstraints.WEST;
        gbc_enableMotionBlur.gridx = 0;
        gbc_enableMotionBlur.gridy = 0;
        panelCheckboxes.add(enableMotionBlur, gbc_enableMotionBlur);

        disableCombatText = new JCheckBox("Disable Combat Text");
        GridBagConstraints gbc_disableCombatText = new GridBagConstraints();
        gbc_disableCombatText.anchor = GridBagConstraints.WEST;
        gbc_disableCombatText.gridx = 1;
        gbc_disableCombatText.gridy = 0;
        panelCheckboxes.add(disableCombatText, gbc_disableCombatText);

        disableHitSounds = new JCheckBox("Disable Hit Sounds");
        GridBagConstraints gbc_disableHitSounds = new GridBagConstraints();
        gbc_disableHitSounds.insets = new Insets(0, 0, 0, 5);
        gbc_disableHitSounds.anchor = GridBagConstraints.WEST;
        gbc_disableHitSounds.gridx = 0;
        gbc_disableHitSounds.gridy = 1;
        panelCheckboxes.add(disableHitSounds, gbc_disableHitSounds);

        disableCrosshair = new JCheckBox("Disable Crosshair");
        GridBagConstraints gbc_disableCrosshair = new GridBagConstraints();
        gbc_disableCrosshair.anchor = GridBagConstraints.WEST;
        gbc_disableCrosshair.gridx = 1;
        gbc_disableCrosshair.gridy = 1;
        panelCheckboxes.add(disableCrosshair, gbc_disableCrosshair);

        disableVoiceChat = new JCheckBox("Disable Voice Chat");
        GridBagConstraints gbc_disableVoiceChat = new GridBagConstraints();
        gbc_disableVoiceChat.insets = new Insets(0, 0, 0, 5);
        gbc_disableVoiceChat.anchor = GridBagConstraints.WEST;
        gbc_disableVoiceChat.gridx = 0;
        gbc_disableVoiceChat.gridy = 2;
        panelCheckboxes.add(disableVoiceChat, gbc_disableVoiceChat);

        disableCrosshairSwitch = new JCheckBox("Disable Crosshair Switching");
        GridBagConstraints gbc_disableCrosshairSwitch = new GridBagConstraints();
        gbc_disableCrosshairSwitch.anchor = GridBagConstraints.WEST;
        gbc_disableCrosshairSwitch.gridx = 1;
        gbc_disableCrosshairSwitch.gridy = 2;
        panelCheckboxes.add(disableCrosshairSwitch, gbc_disableCrosshairSwitch);

        panelBottomLeft = new JPanel();
        FlowLayout fl_panelBottomLeft = (FlowLayout) panelBottomLeft.getLayout();
        fl_panelBottomLeft.setVgap(0);
        fl_panelBottomLeft.setHgap(0);
        GridBagConstraints gbc_panelBottomLeft = new GridBagConstraints();
        gbc_panelBottomLeft.anchor = GridBagConstraints.WEST;
        gbc_panelBottomLeft.gridwidth = 3;
        gbc_panelBottomLeft.insets = new Insets(0, 5, 5, 5);
        gbc_panelBottomLeft.fill = GridBagConstraints.VERTICAL;
        gbc_panelBottomLeft.gridx = 0;
        gbc_panelBottomLeft.gridy = 10;
        panelSettings.add(panelBottomLeft, gbc_panelBottomLeft);

        btnSaveSettings = new JButton("Save Settings");
        panelBottomLeft.add(btnSaveSettings);

        btnClearMovieFolder = new JButton("Clear Movie Files");
        panelBottomLeft.add(btnClearMovieFolder);

        panelBottomRight = new JPanel();
        FlowLayout fl_panelBottomRight = (FlowLayout) panelBottomRight.getLayout();
        fl_panelBottomRight.setVgap(0);
        fl_panelBottomRight.setHgap(0);
        GridBagConstraints gbc_panelBottomRight = new GridBagConstraints();
        gbc_panelBottomRight.gridwidth = 3;
        gbc_panelBottomRight.anchor = GridBagConstraints.EAST;
        gbc_panelBottomRight.insets = new Insets(0, 0, 5, 5);
        gbc_panelBottomRight.fill = GridBagConstraints.VERTICAL;
        gbc_panelBottomRight.gridx = 3;
        gbc_panelBottomRight.gridy = 10;
        panelSettings.add(panelBottomRight, gbc_panelBottomRight);

        btnStartTf = new JButton("Start Team Fortress 2");
        panelBottomRight.add(btnStartTf);

        JPanel panelLog = new JPanel();
        tabbedPane.addTab("Log", null, panelLog, null);
        panelLog.setLayout(new BorderLayout(0, 0));

        scrollPane_2 = new JScrollPane();
        panelLog.add(scrollPane_2, BorderLayout.CENTER);

        textAreaLog = new JTextArea();
        textAreaLog.setFont(new Font("Tahoma", Font.PLAIN, 10));
        textAreaLog.setEditable(false);
        scrollPane_2.setViewportView(textAreaLog);

        panelStatusbar = new JPanel();
        contentPane.add(panelStatusbar, BorderLayout.SOUTH);
        GridBagLayout gbl_panelStatusbar = new GridBagLayout();
        gbl_panelStatusbar.columnWidths = new int[] {
                31, 0, 0, 0
        };
        gbl_panelStatusbar.rowHeights = new int[] {
                12, 0
        };
        gbl_panelStatusbar.columnWeights = new double[] {
                0.0, 1.0, 0.0, Double.MIN_VALUE
        };
        gbl_panelStatusbar.rowWeights = new double[] {
                0.0, Double.MIN_VALUE
        };
        panelStatusbar.setLayout(gbl_panelStatusbar);

        lblStatus = new JLabel("Status");
        GridBagConstraints gbc_lblStatus = new GridBagConstraints();
        gbc_lblStatus.insets = new Insets(0, 5, 0, 5);
        gbc_lblStatus.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblStatus.gridx = 0;
        gbc_lblStatus.gridy = 0;
        panelStatusbar.add(lblStatus, gbc_lblStatus);

        progressBar = new JProgressBar();
        GridBagConstraints gbc_progressBar = new GridBagConstraints();
        gbc_progressBar.anchor = GridBagConstraints.EAST;
        gbc_progressBar.gridx = 2;
        gbc_progressBar.gridy = 0;
        panelStatusbar.add(progressBar, gbc_progressBar);

        pack();
        setMinimumSize(new Dimension(750, 400));
        setLocationByPlatform(true);
    }

    public JComboBox<String> getCmbResolution() {
        return cmbResolution;
    }

    public JComboBox<String> getCmbSkybox() {
        return cmbSkybox;
    }

    public JComboBox<String> getCmbHud() {
        return cmbHud;
    }

    public JComboBox<String> getCmbFramerate() {
        return cmbFramerate;
    }

    public JComboBox<String> getCmbQuality() {
        return cmbQuality;
    }

    public JSpinner getSpinnerViewmodelFov() {
        return spinnerViewmodelFov;
    }

    public JCheckBox getEnableMotionBlur() {
        return enableMotionBlur;
    }

    public JCheckBox getDisableCrosshair() {
        return disableCrosshair;
    }

    public JCheckBox getDisableCrosshairSwitch() {
        return disableCrosshairSwitch;
    }

    public JCheckBox getDisableCombatText() {
        return disableCombatText;
    }

    public JCheckBox getDisableHitSounds() {
        return disableHitSounds;
    }

    public JCheckBox getDisableVoiceChat() {
        return disableVoiceChat;
    }

    public JButton getBtnStartTf() {
        return btnStartTf;
    }

    public JLabel getLblResolution() {
        return lblResolution;
    }

    public JLabel getLblFrameRate() {
        return lblFrameRate;
    }

    public JLabel getLblSkyboxPreview() {
        return lblSkyboxPreview;
    }

    public JButton getBtnSaveSettings() {
        return btnSaveSettings;
    }

    public JButton getBtnClearMovieFolder() {
        return btnClearMovieFolder;
    }

    public JTextArea getTextAreaLog() {
        return textAreaLog;
    }

    public JTable getTableCustomContent() {
        return tableCustomContent;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JMenuItem getMntmChangeTfDirectory() {
        return mntmChangeTfDirectory;
    }

    public JMenuItem getMntmChangeMovieDirectory() {
        return mntmChangeMovieDirectory;
    }

    public JLabel getLblStatus() {
        return lblStatus;
    }

    public JComboBox<String> getCmbViewmodel() {
        return cmbViewmodel;
    }

    public JLabel getLblPreview() {
        return lblPreview;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

}
