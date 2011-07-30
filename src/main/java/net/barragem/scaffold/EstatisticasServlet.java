package net.barragem.scaffold;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.KeypointPNGEncoderAdapter;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

public class EstatisticasServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getParameter("vitorias") == null || req.getParameter("derrotas") == null) {
			return;
		}

		String vitorias = new StringBuilder().append(MessageBundleUtils.getInstance().get("label_vitorias"))
				.append("(").append(req.getParameter("vitorias")).append(")").toString();
		String derrotas = new StringBuilder().append(MessageBundleUtils.getInstance().get("label_derrotas"))
				.append("(").append(req.getParameter("derrotas")).append(")").toString();

		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue(vitorias, new Integer(req.getParameter("vitorias")));
		pieDataset.setValue(derrotas, new Integer(req.getParameter("derrotas")));
		JFreeChart chart = ChartFactory.createPieChart3D(null, pieDataset, true, false, false);
		chart.setBackgroundPaint(new Color(255, 255, 255, 0));
		chart.setBorderVisible(false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(255, 255, 255, 0));
		plot.setSectionPaint(vitorias, new Color(123, 200, 133));
		plot.setSectionPaint(derrotas, new Color(13, 40, 111));
		plot.setOutlineVisible(false);
		plot.setDirection(Rotation.ANTICLOCKWISE);
		plot.setCircular(false);
		plot.setLabelLinksVisible(false);
		plot.setLabelGenerator(null);

		((PiePlot3D) plot).setDarkerSides(true);
		((PiePlot3D) plot).setDepthFactor(0.05d);

		KeypointPNGEncoderAdapter encoder = new KeypointPNGEncoderAdapter();
		encoder.setEncodingAlpha(true);

		resp.setContentType("image/png");
		resp.getOutputStream().write(encoder.encode(chart.createBufferedImage(200, 120, BufferedImage.BITMASK, null)));
	}
}
