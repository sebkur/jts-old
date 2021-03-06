
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
package org.locationtech.jts.index.bintree;

/**
 * Represents an (1-dimensional) closed interval on the Real number line.
 *
 * @version 1.7
 */
public class Interval {

  public double min, max;

  public Interval()
  {
    min = 0.0;
    max = 0.0;
  }

  public Interval(double min, double max)
  {
    init(min, max);
  }
  public Interval(Interval interval)
  {
    init(interval.min, interval.max);
  }
  public void init(double min, double max)
  {
    this.min = min;
    this.max = max;
    if (min > max) {
      this.min = max;
      this.max = min;
    }
  }
  public double getMin() { return min; }
  public double getMax() { return max; }
  public double getWidth() { return max - min; }

  public void expandToInclude(Interval interval)
  {
    if (interval.max > max) max = interval.max;
    if (interval.min < min) min = interval.min;
  }
  public boolean overlaps(Interval interval)
  {
    return overlaps(interval.min, interval.max);
  }

  public boolean overlaps(double min, double max)
  {
    if (this.min > max || this.max < min) return false;
    return true;
  }

  public boolean contains(Interval interval)
  {
    return contains(interval.min, interval.max);
  }
  public boolean contains(double min, double max)
  {
    return (min >= this.min && max <= this.max);
  }
  public boolean contains(double p)
  {
    return (p >= this.min && p <= this.max);
  }

  public String toString()
  {
    return "[" + min + ", " + max + "]";
  }
}
