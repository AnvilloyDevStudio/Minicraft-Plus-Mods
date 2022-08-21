package minicraft.mods;

import java.awt.BorderLayout;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.tinylog.Logger;

public class ModLoadingHandler {
	private static final int width = 300;
	private static final int height = 300;

	private static JFrame frame = new JFrame("MiniMods Loading");
	private static Canvas canvas = new Canvas();
	private static Font font = new Font(Font.SANS_SERIF, Font.BOLD, 15);

	/** The overall loading progress. */
	public static final Progress overallPro = new Progress(6);
	public static Progress secondaryPro = null;
	public static Progress detailPro = null;

	private static boolean enabled = false;
	private static Thread rendering = new Thread(() -> {
		while (enabled) {
			BufferStrategy bs = canvas.getBufferStrategy(); // Creates a buffer strategy to determine how the graphics should be buffered.
			Graphics2D g = (Graphics2D) bs.getDrawGraphics(); // Gets the graphics in which java draws the picture
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Draws a rect to fill the whole window (to cover last?)
			g.setColor(Color.GREEN);
			g.setFont(font);
			g.setStroke(new BasicStroke(2));
			Graphics textG = g.create(0, 0, width, height);
			textG.setColor(Color.BLACK);
			FontRenderContext frc = g.getFontRenderContext();

			String progressText;
			LineMetrics progressTextBounds;

			// Draw the overall progress bar.
			synchronized (overallPro) {
				if (overallPro.text != null && !overallPro.text.isEmpty()) {
					progressTextBounds = font.getLineMetrics(overallPro.text, frc);
					textG.drawString(overallPro.text, 10, (int) (35 - progressTextBounds.getDescent()));
				}

				g.drawRect(10, 40, width - 20, 50);
				g.fillRect(10, 40, (int) ((width - 20) * Math.min((double) overallPro.cur / (double) overallPro.max, 1d)), 50);
				progressText = overallPro.cur + "/" + overallPro.max;
				progressTextBounds = font.getLineMetrics(progressText, frc);
				textG.drawString(progressText, (int) (width / 2 - font.getStringBounds(progressText, frc).getWidth() / 2), (int) (65 + progressTextBounds.getHeight() / 2 - progressTextBounds.getDescent()));
			}

			// Draw the second progress bar.
			try {
				if (secondaryPro != null) synchronized (secondaryPro) {
					if (secondaryPro != null) {
						if (secondaryPro.text != null && !secondaryPro.text.isEmpty()) {
							progressTextBounds = font.getLineMetrics(secondaryPro.text, frc);
							textG.drawString(secondaryPro.text, 10, (int) (135 - progressTextBounds.getDescent()));
						}

						g.drawRect(10, 140, width - 20, 50);
						g.fillRect(10, 140, (int) ((width - 20) * Math.min((double) secondaryPro.cur / (double) secondaryPro.max, 1d)), 50);
						progressText = secondaryPro.cur + "/" + secondaryPro.max;
						progressTextBounds = font.getLineMetrics(progressText, frc);
						textG.drawString(progressText, (int) (width / 2 - font.getStringBounds(progressText, frc).getWidth() / 2), (int) (165 + progressTextBounds.getHeight() / 2 - progressTextBounds.getDescent()));
					}
				}
			} catch (NullPointerException e) {} // There might be some NullPointerException occurs when accessing secondaryPro.

			// Draw the third progress bar.
			try {
				if (detailPro != null) synchronized (detailPro) {
					if (detailPro != null) {
						if (detailPro.text != null && !detailPro.text.isEmpty()) {
							progressTextBounds = font.getLineMetrics(detailPro.text, frc);
							textG.drawString(detailPro.text, 10, (int) (235 - progressTextBounds.getDescent()));
						}

						g.drawRect(10, 240, width - 20, 50);
						g.fillRect(10, 240, (int) ((width - 20) * Math.min((double) detailPro.cur / (double) detailPro.max, 1d)), 50);
						progressText = detailPro.cur + "/" + detailPro.max;
						progressTextBounds = font.getLineMetrics(progressText, frc);
						textG.drawString(progressText, (int) (width / 2 - font.getStringBounds(progressText, frc).getWidth() / 2), (int) (265 + progressTextBounds.getHeight() / 2 - progressTextBounds.getDescent()));
					}
				}
			} catch (NullPointerException e) {} // There might be some NullPointerException occurs when accessing detailPro.

			// Release any system items that are using this method. (so we don't have crappy framerates)
			g.dispose();

			// Make the picture visible.
			bs.show();
		}
	}, "Loading Display Renderer");

	public static class Progress {
		public final int max;
		public int cur = 0;
		public String text = null;

		public Progress(int max) {
			this.max = max;
		}
	}

	public static void initLoadingScreen() {
		canvas.setSize(width, height);
		canvas.setBackground(Color.WHITE);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.pack();

		try {
			BufferedImage logo = ImageIO.read(ModLoadingHandler.class.getResourceAsStream("/resources/logo.png")); // Load the window logo
			frame.setIconImage(logo);
		} catch (IOException e) {
			e.printStackTrace();
		}

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		canvas.createBufferStrategy(3);
		canvas.requestFocus();
		new Thread(() -> frame.setVisible(true), "Window Starter").start();
		enabled = true;
		rendering.start();
	}

	public static void toFront() {
		frame.toFront();
	}

	public static void closeWindow() {
		enabled = false;
		frame.setVisible(false);
		frame.dispose();
		Logger.debug("Loading window closed.");
	}
}
