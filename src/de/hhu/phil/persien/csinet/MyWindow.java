package de.hhu.phil.persien.csinet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MyWindow extends JFrame {

	private AbstractPanel board;
	private BufferedImage buffer;
	private static int bwidth = 480;
	private static int bheight = 320;
	
	public MyWindow() {
		this(new AbstractPanel());
	}
	
	public MyWindow(AbstractPanel board) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.board = board;
		this.board.setPreferredSize(new Dimension(this.board.getWidth(),this.board.getHeight()));
		this.getContentPane().add(this.board);
		this.pack();
	}
	
	/*
	 * Getters
	 */
	
	public AbstractPanel getBoard() { return this.board; }
	public BufferedImage getBuffer() { return this.board.getBuffer(); }
	
	/*
	 * Setters
	 */
	
	public void setBoard(AbstractPanel board) {
		this.getContentPane().remove(this.board);
		this.board = board;
		this.getContentPane().add(this.board);
		this.pack();
		//this.buffer = new BufferedImage(bwidth, bheight, BufferedImage.TYPE_INT_ARGB);
	}
	
	/*
	 * 
	 */
	
	public void clear() { this.board.getGraphics().clearRect(0, 0, this.board.getWidth(), this.board.getHeight()); }
	public void update() {
		this.board.update();
//		this.clear();
		//this.makeTransparent(this.getBuffer());
		this.board.getGraphics().drawImage(this.board.getBuffer(),0,0,this);
	}
	
	public static void makeTransparent(BufferedImage b) {
		int width = b.getWidth();
		int height = b.getHeight();
		
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
					int rgb = b.getRGB(x, y) & 0x00FFFFFF;
					if (rgb == 0xFF00FF) {
						b.setRGB(x, y, 0);
					}
			}
		}
	}

}
