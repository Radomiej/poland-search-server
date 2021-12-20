package pl.radomiej.search.services.imports.states;

import java.util.LinkedList;
import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import pl.radomiej.search.domains.CodgikStreetNode;

public enum StateActionStreet {
	NONE {
		@Override
		public void procces(CodgikStreetNode node, String text) {
		}
	},
	LINIA {
		@Override
		public void procces(CodgikStreetNode node, String text) {

			List<Coordinate> coordinates = new LinkedList<Coordinate>();
			String[] coordsy = text.split(" ");
			
			for(int i = 0; i < coordsy.length; i += 2){
				double x = Double.parseDouble(coordsy[i]);
				double y = Double.parseDouble(coordsy[i + 1]);
				
				Coordinate coordinate = new Coordinate(x, y);
				
				try {
					CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:2180");
					CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
					MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
					JTS.transform( coordinate, coordinate, transform );
				} catch (FactoryException | TransformException e) {
					e.printStackTrace();
				}
				coordinates.add(coordinate);
			}
			
			Coordinate[] coordinatesTab = new Coordinate[coordinates.size()];
			coordinatesTab = coordinates.toArray(coordinatesTab);

			LineString lineString = fact.createLineString(coordinatesTab);
			node.setLine(lineString.toText());
			node.setLatitude(lineString.getCentroid().getX());
			node.setLongitude(lineString.getCentroid().getY());
		}

	};
	private static GeometryFactory fact = new GeometryFactory();

	public abstract void procces(CodgikStreetNode node, String text);
}
