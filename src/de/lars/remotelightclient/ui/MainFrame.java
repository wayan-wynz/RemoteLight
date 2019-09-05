package de.lars.remotelightclient.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import de.lars.remotelightclient.DataStorage;
import de.lars.remotelightclient.Main;
import de.lars.remotelightclient.lang.i18n;
import de.lars.remotelightclient.settings.SettingsManager;
import de.lars.remotelightclient.settings.types.SettingSelection;
import de.lars.remotelightclient.settings.types.SettingString;
import de.lars.remotelightclient.ui.panels.OutputPanel;
import de.lars.remotelightclient.ui.panels.SettingsPanel;
import de.lars.remotelightclient.ui.panels.SideMenuSmall;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.FlowLayout;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6397116310182308082L;
	private JPanel contentPane;
	private JPanel bgrSideMenu;
	private JPanel bgrContentPanel;
	
	private String selectedMenu = "output";
	private JPanel displayedPanel;
	private SettingsManager sm;


	/**
	 * Create the frame.
	 */
	public MainFrame() {
		sm = Main.getInstance().getSettingsManager();
		setTitle("RemoteLight");
		setMinimumSize(new Dimension(400, 350));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 836, 391);
		addWindowListener(closeListener);
		
		this.setFrameContetPane();
		this.displayPanel(new OutputPanel(this));
	}
	
	private void setFrameContetPane() {
		SettingSelection style = (SettingSelection) sm.getSettingFromId("ui.style");
		Style.setStyle(style.getSelected());
		Locale.setDefault(new Locale(i18n.langNameToCode(((SettingSelection) sm.getSettingFromId("ui.language")).getSelected())));
		
		contentPane = new JPanel();
		contentPane.setBackground(Style.panelBackground);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		bgrSideMenu = new JPanel();
		bgrSideMenu.setLayout(new BorderLayout(0, 0));
		contentPane.add(bgrSideMenu, BorderLayout.WEST);
		
		JPanel sideMenu = new SideMenuSmall(this);
		bgrSideMenu.add(sideMenu, BorderLayout.CENTER);
		
		bgrContentPanel = new JPanel();
		bgrContentPanel.setBackground(Style.panelBackground);
		contentPane.add(bgrContentPanel, BorderLayout.CENTER);
		bgrContentPanel.setLayout(new BorderLayout(0, 0));
		
		panelNotification = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelNotification.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelNotification.setBackground(Style.panelBackground);
		bgrContentPanel.add(panelNotification, BorderLayout.NORTH);
		
		lblNotification = new JLabel("");
		panelNotification.add(lblNotification);
		
		contentArea = new JPanel();
		contentArea.setBackground(Style.panelBackground);
		bgrContentPanel.add(contentArea, BorderLayout.CENTER);
		contentArea.setLayout(new BorderLayout(0, 0));
		
		bgrControlBar = new JPanel();
		bgrControlBar.setBackground(Style.panelDarkBackground);
		contentPane.add(bgrControlBar, BorderLayout.SOUTH);
	}
	
	
	WindowListener closeListener = new WindowAdapter() {
		public void windowClosing(WindowEvent windowEvent) {
			if(DataStorage.isStored(DataStorage.SETTINGS_HIDE) && (boolean) DataStorage.getData(DataStorage.SETTINGS_HIDE)) {
				SystemTrayIcon.showTrayIcon();
				dispose();
			} else {
				System.exit(0);
			}
		}
	};
	private JPanel bgrControlBar;
	private JPanel panelNotification;
	private JLabel lblNotification;
	private JPanel contentArea;
	
	public JPanel getSideMenu() {
		return bgrSideMenu;
	}
	
	public String getSelectedMenu() {
		return selectedMenu;
	}
	
	public void setSelectedMenu(String menu) {
		selectedMenu = menu.toLowerCase();
	}
	
	public void displayPanel(JPanel panel) {
		contentArea.removeAll();
		contentArea.add(panel, BorderLayout.CENTER);
		this.displayedPanel = panel;
		contentArea.updateUI();
	}
	
	public JPanel getDisplayedPanel() {
		return this.displayedPanel;
	}
	
	public void showControlBar(boolean enabled) {
		bgrControlBar.setVisible(enabled);
	}
	
	public void updateFrame() {
		this.getContentPane().removeAll();
		this.setFrameContetPane();
		this.revalidate();
		this.repaint();
	}
	
	public void menuSelected(String menu) {
		printNotification("", NotificationType.Unimportant);
		switch(menu.toLowerCase())
		{
		case "settings":
			this.displayPanel(new SettingsPanel(this, sm));
			break;
		case "output":
			this.displayPanel(new OutputPanel(this));
			break;
		}
	}
	
	public enum NotificationType {
		Error, Info, Unimportant, Success
	}
	public void printNotification(String text, NotificationType type) {
		if(text == null || text.equals("")) {
			panelNotification.setVisible(false);
			return;
		}
		panelNotification.setVisible(true);
		lblNotification.setText(text);
		switch (type) {
		case Error:
			lblNotification.setForeground(Color.RED);
			break;
		case Info:
			lblNotification.setForeground(Style.accent);
			break;
		case Unimportant:
			lblNotification.setForeground(Style.textColor);
			break;
		case Success:
			lblNotification.setForeground(Color.GREEN);
			break;
		}
	}

}
