package de.lars.remotelightclient.ui.panels.settings.settingComps;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;

import de.lars.remotelightclient.settings.Setting;

public abstract class SettingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2743868556642656374L;
	private int width = 2000;
	private int height = 40;
	private SettingChangedListener listener;
	
	public SettingPanel() {
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
	}
	
	public interface SettingChangedListener {
		public void onSettingChanged(Setting setting);
	}
	
	public abstract void setValue();

	public synchronized void setSettingChangedListener(SettingChangedListener l) {
		this.listener = l;
	}
	
	public void onChanged(Setting setting) {
		if(listener != null) {
			listener.onSettingChanged(setting);
		}
	}
	
}
