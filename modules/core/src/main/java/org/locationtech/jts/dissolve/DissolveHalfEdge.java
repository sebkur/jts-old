/*
 * The JTS Topology Suite is a collection of Java classes that
 * implement the fundamental operations required to validate a given
 * geo-spatial data set to a known topological specification.
 * 
 * Copyright (C) 2016 Vivid Solutions
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Vivid Solutions BSD
 * License v1.0 (found at the root of the repository).
 * 
 */

package org.locationtech.jts.dissolve;

import org.locationtech.jts.edgegraph.MarkHalfEdge;
import org.locationtech.jts.geom.Coordinate;


/**
 * A HalfEdge which carries information
 * required to support {@link LineDissolver}.
 * 
 * @author Martin Davis
 *
 */
class DissolveHalfEdge extends MarkHalfEdge
{
  private boolean isStart = false;
  
  public DissolveHalfEdge(Coordinate orig) {
    super(orig);
  }

  /**
   * Tests whether this edge is the starting segment
   * in a LineString being dissolved.
   * 
   * @return true if this edge is a start segment
   */
  public boolean isStart()
  {
    return isStart;
  }
  
  /**
   * Sets this edge to be the start segment of an input LineString.
   */
  public void setStart()
  {
    isStart = true;
  }
}
