import java.util.*;

/**
 * Point object for use with Watershed algorithms. Used in queueing specific spots
 * on the grid.
 *
 * @author <a href="mailto:bg5087a@american.edu">Ben Goldstein</a>
 * @version 1.1
 */

public class Point implements Comparable<Point>{

	public int x;
	public int y;
	public int distFromCtr;
	public Country country;

	 /**
     * Point constructor for use in Terrain and Watershed algorithms
     *
     * @param x is the x-coordinate
     * @param y is the y-coordinate
     * @param height is the point's height
     */
	public Point (int x, int y, int distFromCtr, Country country) {
		this.x = x;
		this.y = y;
		this.distFromCtr = distFromCtr;
		this.country = country;
	}

	 /**
     * Gets the distance to another point
     *
     * @param other is the point to compare with.
     */
	public float distanceTo(Point other) {

		float distance = (float)Math.sqrt(Math.pow(Math.abs(this.x - other.x),2) + Math.pow(Math.abs(this.y - other.y),2));
		return distance;

	} 

	 /**
     * toString returns the coordinates of the point followed by its height.
     */
	@Override
	public String toString(){
		return "x: " + x + "y: " + y;
	}

	 /**
     * Compares how far they are from their center, in order to prioritize the spread of nearer points
     */
	@Override 
	public int compareTo(Point other) {
		if (this.distFromCtr > other.distFromCtr) return 1;
		return -1;
	}


	 /**
     * equals override. Compares coordinates; assumes two points on the same Terrain object
     * with the same loc have the same properties.
     *
     * @param obj is the other item.
     */
	@Override
	public boolean equals(Object obj){
		
		Point other;

		if (obj == null) return false;
		if (obj instanceof Point){
			other = (Point) obj;
		} else return false;

		if (other.x == this.x && other.y == this.y) return true;
		return false;
	}


	 /**
     * hashCode override corresponding to the above equals override
     */
	@Override
	public int hashCode() {
		return this.x * 107 + this.y;
	}

}