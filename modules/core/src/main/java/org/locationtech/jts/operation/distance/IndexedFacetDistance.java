/*
 * The JTS Topology Suite is a collection of Java classes that
 * implement the fundamental operations required to validate a given
 * geo-spatial data set to a known topological specification.
 * 
 * Copyright (C) 2016 Martin Davis
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Martin Davis BSD
 * License v1.0 (found at the root of the repository).
 * 
 */

package org.locationtech.jts.operation.distance;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Lineal;
import org.locationtech.jts.geom.Polygonal;
import org.locationtech.jts.geom.Puntal;
import org.locationtech.jts.index.strtree.ItemBoundable;
import org.locationtech.jts.index.strtree.ItemDistance;
import org.locationtech.jts.index.strtree.STRtree;

/**
 * Computes the distance between the facets (segments and vertices) 
 * of two {@link Geometry}s
 * using a Branch-and-Bound algorithm.
 * The Branch-and-Bound algorithm operates over a 
 * traversal of R-trees built
 * on the target and possibly also the query geometries.
 * <p>
 * This approach provides the following benefits:
 * <ul>
 * <li>Performance is improved due to the effects of the 
 * R-tree index
 * and the pruning due to the Branch-and-Bound approach
 * <li>The spatial index on the target geometry can be cached
 * to allow reuse in an incremental query situation.
 * </ul>
 * Using this technique can be much more performant 
 * than using {@link #getDistance(Geometry)} 
 * when one or both
 * input geometries are large, 
 * or when evaluating many distance computations against 
 * a single geometry.
 * <p>
 * This class is not thread-safe.
 * 
 * @author Martin Davis
 *
 */
public class IndexedFacetDistance 
{
  /**
   * Computes the distance between two geometries using
   * the indexed approach.
   * <p>
   * For geometries with many segments or points, 
   * this can be faster than using a simple distance
   * algorithm.
   * 
   * @param g1 a geometry
   * @param g2 a geometry
   * @return the distance between the two geometries
   */
  public static double distance(Geometry g1, Geometry g2)
  {
    IndexedFacetDistance dist = new IndexedFacetDistance(g1);
    return dist.getDistance(g2);
  }
  
  private STRtree cachedTree;
  
  /**
   * Creates a new distance-finding instance for a given target {@link Geometry}.
   * <p>
   * Distances will be computed to all facets of the input geometry.
   * The facets of the geometry are the discrete segments and points 
   * contained in its components.  
   * In the case of {@link Lineal} and {@link Puntal} inputs,
   * this is equivalent to computing the conventional distance.
   * In the case of {@link Polygonal} inputs, this is equivalent 
   * to computing the distance to the polygons boundaries. 
   * 
   * @param g1 a Geometry, which may be of any type.
   */
  public IndexedFacetDistance(Geometry g1) {
    cachedTree = FacetSequenceTreeBuilder.build(g1);
  }

  /**
   * Computes the distance from the base geometry to 
   * the given geometry.
   *  
   * @param g the geometry to compute the distance to
   * 
   * @return the computed distance
   */
  public double getDistance(Geometry g)
  {
    STRtree tree2 = FacetSequenceTreeBuilder.build(g);
    Object[] obj = cachedTree.nearestNeighbour(tree2, 
        new FacetSequenceDistance());
    return facetDistance(obj);
  }
  
  private static double facetDistance(Object[] obj)
  {
    Object o1 = obj[0];
    Object o2 = obj[1];
    return ((FacetSequence) o1).distance((FacetSequence) o2);
  }
  
  /**
   * Computes the distance from the base geometry to 
   * the given geometry, up to and including a given 
   * maximum distance.
   * 
   * @param g the geometry to compute the distance to
   * @param maximumDistance the maximum distance to compute.
   * 
   * @return the computed distance,
   *    or <tt>maximumDistance</tt> if the true distance is determined to be greater
   */
  // TODO: implement this
  /*
  public double getDistanceWithin(Geometry g, double maximumDistance)
  {
    STRtree tree2 = FacetSequenceTreeBuilder.build(g);
    Object[] obj = cachedTree.nearestNeighbours(tree2, 
        new FacetSequenceDistance());
    return facetDistance(obj);
  }
  */
  

  /**
   * Tests whether the base geometry lies within
   * a specified distance of the given geometry.
   * 
//   * @param g the geometry to test
//   * @param maximumDistance the maximum distance to test
//   * @return true if the geometry lies with the specified distance
   */
  // TODO: implement this
  /*
  public boolean isWithinDistance(Geometry g, double maximumDistance)
  {
    STRtree tree2 = FacetSequenceTreeBuilder.build(g);
    double dist = findMinDistance(cachedTree.getRoot(), tree2.getRoot(), maximumDistance);
    if (dist <= maximumDistance)
      return false;
    return true;
  }
  */
  
  private static class FacetSequenceDistance
  implements ItemDistance
  {
    public double distance(ItemBoundable item1, ItemBoundable item2) {
      FacetSequence fs1 = (FacetSequence) item1.getItem();
      FacetSequence fs2 = (FacetSequence) item2.getItem();
      return fs1.distance(fs2);    
    }
  }
}


