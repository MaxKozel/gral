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
package de.erichseifert.gral.uml;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import metamodel.classes.interfaces.Property;
import de.erichseifert.gral.graphics.DrawableContainer;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.StackedLayout;
import de.erichseifert.gral.plots.Label;
import de.erichseifert.gral.util.Insets2D;
import de.erichseifert.gral.util.Orientation;

public class ClassDrawable extends DrawableContainer {
	private final Label className;
	/** Stroke used to paint the border of the plot area. */
	private Stroke borderStroke;

	public ClassDrawable(metamodel.classes.kernel.Class clazz) {
		super(new StackedLayout(Orientation.VERTICAL));
		className = new Label(clazz.getQualifiedName());
		className.setAlignmentX(0.0);
		className.setAlignmentY(0.0);
		add(className);

		for (Property property : clazz.getOwnedAttributes()) {
			Label propertyLabel = new Label(property.getName());
			add(propertyLabel);
		}

		double textHeight = className.getTextRectangle().getHeight();
		double textWidth = className.getTextRectangle().getWidth();
		Insets2D insets = new Insets2D.Double(textHeight);
		setInsets(insets);
		Rectangle2D bounds = new Rectangle2D.Double(
			0.0, 0.0,
			insets.getLeft() + textWidth + insets.getRight(),
			insets.getTop() + textHeight + insets.getBottom()
		);
		setBounds(bounds);
		borderStroke = new BasicStroke(5f);
	}

	@Override
	public void draw(DrawingContext context) {
		drawBorder(context);
		super.drawComponents(context);
	}

	protected void drawBorder(DrawingContext context) {
		Graphics2D g2d = context.getGraphics();

		Stroke strokeOld = g2d.getStroke();
		//Stroke borderStroke = getBorderStroke();
		//g2d.setStroke(borderStroke);

		g2d.draw(getBounds());

		g2d.setStroke(strokeOld);
	}

	public Stroke getBorderStroke() {
		return borderStroke;
	}

	public void setBorderStroke(Stroke borderStroke) {
		this.borderStroke = borderStroke;
	}
}
