package net.barragem.scaffold;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.captcha.text.renderer.WordRenderer;

public class CustomWordRenderer implements WordRenderer {

	private static final Color DEFAULT_COLOR = Color.BLACK;
	private static final List<Font> DEFAULT_FONTS = new ArrayList<Font>();

	private final Color _color;
	private final List<Font> _fonts;

	static {
		DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 30));
		DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, 30));
	}

	/**
	 * Will render the characters in black and in either 40pt Arial or Courier.
	 */
	public CustomWordRenderer() {
		this(DEFAULT_COLOR, DEFAULT_FONTS);
	}

	public CustomWordRenderer(Color color, List<Font> fonts) {
		_color = color != null ? color : DEFAULT_COLOR;
		_fonts = fonts != null ? fonts : DEFAULT_FONTS;
	}

	/**
	 * Render a word onto a BufferedImage.
	 * 
	 * @param word
	 *            The word to be rendered.
	 * @param bi
	 *            The BufferedImage onto which the word will be painted on to
	 */
	@Override
	public void render(String word, BufferedImage image) {
		Graphics2D g = image.createGraphics();

		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g.setRenderingHints(hints);

		g.setColor(_color);
		FontRenderContext frc = g.getFontRenderContext();
		int startPosX = 60;
		char[] wc = word.toCharArray();
		Random generator = new Random();
		for (char element : wc) {
			char[] itchar = new char[] { element };
			int choiceFont = generator.nextInt(_fonts.size());
			Font itFont = _fonts.get(choiceFont);
			g.setFont(itFont);

			GlyphVector gv = itFont.createGlyphVector(frc, itchar);
			double charWitdth = gv.getVisualBounds().getWidth();

			g.drawChars(itchar, 0, itchar.length, startPosX, 30);
			startPosX = startPosX + (int) charWitdth;
		}
	}
}