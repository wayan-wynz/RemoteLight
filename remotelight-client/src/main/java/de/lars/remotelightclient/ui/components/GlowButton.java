package de.lars.remotelightclient.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import org.tinylog.Logger;

import de.lars.remotelightcore.effect.AbstractEffect;

public class GlowButton extends BigTextButton implements ActionListener {
	private static final long serialVersionUID = 1702652196883299731L;
	
	private final Class<? extends AbstractEffect> clazz;
	private AbstractEffect effect;
	private final Timer timer;
	private int renderDelay = 30;
	
	public GlowButton(String title, String text, int glowSize, Class<? extends AbstractEffect> effectClass) {
		super(title, text);
		this.clazz = effectClass;
		this.setBorder(new GlowBorder(this, null, glowSize));
		this.addMouseListener(mouseAdapter);
		timer = new Timer(0, this);
		timer.setDelay(renderDelay);
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseEntered(MouseEvent e) {
			try { // initialize effect
				effect = clazz.newInstance();
			} catch(Exception ex) {}
			
			if(effect != null) {
				try {
					effect.onEnable(60);
				} catch (Exception ex) {
					Logger.error(ex);
				}
				timer.restart();
			}
		};
		
		@Override
		public void mouseExited(MouseEvent e) {
			// disable effect and set it to null causing the timer to stop
			if(effect != null) {
				try {
					effect.onDisable();
				} catch(Exception ex) {
					Logger.error(ex);
				}
				effect = null;
			}
		};
	};
	
	public void setRenderDelay(int renderDelay) {
		this.renderDelay = renderDelay;
		timer.setDelay(renderDelay);
	}

	/**
	 * Triggered when the border should be repainted.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!(getBorder() instanceof GlowBorder))
			return;
		GlowBorder border = (GlowBorder) getBorder();
		if(effect == null) { // stop timer when effect is null
			border.updateColor(null);
			timer.stop();
			return;
		}
		try {
			border.updateColor(effect.onEffect());
		} catch(Exception ex) {
			Logger.error(ex);
		}
	}

}
