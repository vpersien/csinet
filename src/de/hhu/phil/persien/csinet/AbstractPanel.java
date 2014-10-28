package de.hhu.phil.persien.csinet;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class AbstractPanel extends JPanel {
	protected int width = 480;
	protected int height = 320;
	protected BufferedImage buffer;
	protected Graphics graphics;
	protected boolean recording = Constants.TAKESCREENSHOTS;
	
	public AbstractPanel(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.init();
		
	}
	
	public AbstractPanel() {
		this(480,320);
	}
	
	/*
	 * Getters
	 */
	
	public BufferedImage getBuffer() { return this.buffer; }
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	public boolean isRecording() { return this.recording; }
	
	/*
	 * Setters
	 */
	
	public void setWidth(int width) { this.width = width; }
	public void setHeight(int height) { this.height = height; }
	public void setRecording(boolean recording) { this.recording = recording; }
	
	/*
	 * 
	 */

	public void update() {}
	public void init() {
		this.setPreferredSize(new Dimension(this.width,this.height));
		this.buffer = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		this.graphics = this.buffer.getGraphics();
	}
	
//	public MyPanel(LayoutManager layout) {
//		super(layout);
//		// TODO Auto-generated constructor stub
//	}
//
//	public MyPanel(boolean isDoubleBuffered) {
//		super(isDoubleBuffered);
//		// TODO Auto-generated constructor stub
//	}
//
//	public MyPanel(LayoutManager layout, boolean isDoubleBuffered) {
//		super(layout, isDoubleBuffered);
//		// TODO Auto-generated constructor stub
//	}

}
