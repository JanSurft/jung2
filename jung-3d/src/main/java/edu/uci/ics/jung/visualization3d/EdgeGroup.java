/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 */
package edu.uci.ics.jung.visualization3d;

/**
 */

import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */public class EdgeGroup<E> extends TransformGroup {

	 E edge;
	 Node shape;
	 
	 public EdgeGroup(E edge, Node shape) {
		 this.edge = edge;
		 this.shape = shape;
		 setCapability(TransformGroup.ENABLE_PICK_REPORTING);

//		 Cylinder cylinder = new Cylinder(radius, 1, 
//				 Cylinder.GENERATE_NORMALS |
//				 Cylinder.GENERATE_TEXTURE_COORDS |
//				 Cylinder.ENABLE_GEOMETRY_PICKING,
//				 26, 26, look);

		 Transform3D t = new Transform3D();
		 t.setTranslation(new Vector3d(0., .5, 0.));
		 TransformGroup group = new TransformGroup(t);
		 group.addChild(shape);
		 addChild(group);
	 }
	 
	 public String toString() { return edge.toString(); }
	 
	 public void setEndpoints(Point3d p0, Point3d p1) {

		 final double[] coords0 = new double[3];
		 final double[] coords1 = new double[3];
		 p1.get(coords1);
		 p0.get(coords0);
		 // calculate length
		 double length = p0.distance(p1);
		 
		 // transform to accumulate values
		 Transform3D tx = new Transform3D();
		 
		 // translate so end is at p0
		 Transform3D p0tx = new Transform3D();
		 p0tx.setTranslation(new Vector3d(coords0[0], coords0[1], coords0[2]));

		 // scale so length is dist p0,p1
		 Transform3D scaletx = new Transform3D();
		 scaletx.setScale(new Vector3d(1,length,1));

		 Vector3d yunit = new Vector3d(0,1,0);



		 Vector3d v = new Vector3d(coords1[0] - coords0[0], coords1[1] - coords0[1], coords1[2] - coords0[2]);

		 Vector3d cross = new Vector3d();
		 cross.cross(yunit, v);
		 // cross is the vector to rotate about
		 double angle = yunit.angle(v);
		 
		 Transform3D rot = new Transform3D();
		 double coords[] = new double[3];
		 cross.get(coords);
		 rot.setRotation(new AxisAngle4d(coords[0], coords[1], coords[2], angle));
		 tx.mul(rot);

		 tx.mul(scaletx);
		 tx.setTranslation(new Vector3d(coords0[0], coords0[1], coords0[2]));

		 try {
			 setTransform(tx);
		 } catch(Exception ex) {
			 System.err.println("tx = \n"+tx);
		 }
	 }
 }
