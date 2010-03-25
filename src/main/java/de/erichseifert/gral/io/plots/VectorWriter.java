/**
 * GRAL: Vector export for Java(R) Graphics2D
 *
 * (C) Copyright 2009-2010 Erich Seifert <info[at]erichseifert.de>, Michael Seifert <michael.seifert[at]gmx.net>
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

package de.erichseifert.gral.io.plots;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;

import de.erichseifert.gral.Drawable;
import de.erichseifert.gral.io.AbstractWriter;
import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.vectorgraphics2d.EPSGraphics2D;
import de.erichseifert.vectorgraphics2d.PDFGraphics2D;
import de.erichseifert.vectorgraphics2d.SVGGraphics2D;

/**
 * Class that stores <code>Drawable</code> instances as vector graphics.
 * Supported formats:
 * <ul>
 * <li>EPS</li>
 * <li>SVG</li>
 * </ul>
 */
public class VectorWriter extends AbstractWriter implements DrawableWriter {
	static {
		IOCapabilities EPS_CAPABILITIES = new IOCapabilities(
			"EPS",
			"Encapsulated PostScript",
			TYPE_EPS,
			"eps", "epsf", "epsi"
		);
		addCapabilities(EPS_CAPABILITIES);

		IOCapabilities PDF_CAPABILITIES = new IOCapabilities(
			"PDF",
			"Portable Document Format",
			TYPE_PDF,
			"pdf"
		);
		addCapabilities(PDF_CAPABILITIES);

		IOCapabilities SVG_CAPABILITIES = new IOCapabilities(
			"SVG",
			"Scalable Vector Graphics",
			TYPE_SVG,
			"svg", "svgz"
		);
		addCapabilities(SVG_CAPABILITIES);
	}

	private final String mimeType;

	/**
	 * Creates a new <code>VectorWriter</code> object with the specified MIME-Type.
	 * @param mimeType Output MIME-Type.
	 */
	protected VectorWriter(String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public void write(Drawable d, OutputStream destination, double width, double height) throws IOException {
		write(d, destination, 0.0, 0.0, width, height);
	}

	@Override
	public void write(Drawable d, OutputStream destination, double x, double y, double width,	double height) throws IOException {
		Rectangle2D boundsOld = d.getBounds();
		d.setBounds(x, y, width, height);

		Graphics2D g2d = null;
		if (TYPE_EPS.equals(getMimeType())) {
			g2d = new EPSGraphics2D(x, y, width, height);
		} else if (TYPE_PDF.equals(getMimeType())) {
			g2d = new PDFGraphics2D(x, y, width, height);
		} else if (TYPE_SVG.equals(getMimeType())) {
			g2d = new SVGGraphics2D(x, y, width, height);
		} else {
			throw new IllegalArgumentException("Unsupported format: " +getMimeType());
		}
		d.draw(g2d);
		destination.write(g2d.toString().getBytes());

		d.setBounds(boundsOld);
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

}
