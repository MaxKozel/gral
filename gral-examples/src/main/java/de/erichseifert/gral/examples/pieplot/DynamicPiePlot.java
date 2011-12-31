/*
 * GRAL: GRAphing Library for Java(R)
 *
 * (C) Copyright 2009-2011 Erich Seifert <dev[at]erichseifert.de>,
 * Michael Seifert <michael.seifert[at]gmx.net>
 *
 * This file is part of GRAL.
 *
 * GRAL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GRAL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GRAL.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erichseifert.gral.examples.pieplot;

import java.awt.BorderLayout;
import java.text.MessageFormat;
import java.util.Random;

import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.examples.ExamplePanel;
import de.erichseifert.gral.plots.PiePlot;
import de.erichseifert.gral.plots.Plot;
import de.erichseifert.gral.plots.PiePlot.PieSliceRenderer;
import de.erichseifert.gral.plots.colors.LinearGradient;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;


public class DynamicPiePlot extends ExamplePanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	private static final int SAMPLE_COUNT = 5;
	/** Instance to generate random data values. */
	private static Random random = new Random();

	private final DataTable data;
	private final PiePlot plot;
	private final JSlider valueCountSlider;

	@SuppressWarnings("unchecked")
	public DynamicPiePlot() {
		// Create initial data
		data = new DataTable(Integer.class);

		// Create new pie plot
		plot = new PiePlot(data);
		// Change relative size of pie
		plot.setSetting(PiePlot.RADIUS, 0.9);
		// Change the width of gaps between segments
		plot.setSetting(PiePlot.START, 90.0);
		// Add some margin to the plot area
		plot.setInsets(new Insets2D.Double(20.0));

		// Change the width of gaps between segments
		plot.getPointRenderer(data).setSetting(PieSliceRenderer.GAP, 0.2);
		// Change the colors
		LinearGradient colors = new LinearGradient(COLOR1, COLOR2);
		plot.getPointRenderer(data).setSetting(PieSliceRenderer.COLORS, colors);

		// Add plot to Swing component
		InteractivePanel panel = new InteractivePanel(plot);
		add(panel, BorderLayout.CENTER);

		setValueCount(SAMPLE_COUNT);

		// Create a slider to change the number of data values
		valueCountSlider = new JSlider(0, 50, SAMPLE_COUNT);
		valueCountSlider.setBorder(new EmptyBorder(15, 15, 5, 15));
		valueCountSlider.setMajorTickSpacing(10);
		valueCountSlider.setMinorTickSpacing(1);
		valueCountSlider.setSnapToTicks(true);
		valueCountSlider.setPaintTicks(true);
		valueCountSlider.addChangeListener(this);
		add(valueCountSlider, BorderLayout.SOUTH);
	}

	@Override
	public String getTitle() {
		return "Pie plot";
	}

	@Override
	public String getDescription() {
		return "Pie with a changeable number of random data values";
	}

	private void setValueCount(int count) {
		if (count == data.getRowCount()) {
			return;
		}
		while (data.getRowCount() != count) {
			if (data.getRowCount() < count) {
				int val = random.nextInt(10) + 1;
				data.add(val);
			} else {
				int rowIndexLast = data.getRowCount() - 1;
				data.remove(rowIndexLast);
			}
		}
		if (plot != null) {
			String title = MessageFormat.format("{0,number,integer} random values", data.getRowCount());
			plot.setSetting(Plot.TITLE, title);
		}
	}

	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		if (source == valueCountSlider) {
			int countNew = valueCountSlider.getValue();
			setValueCount(countNew);
			repaint();
		}
	}

	public static void main(String[] args) {
		new DynamicPiePlot().showInFrame();
	}
}