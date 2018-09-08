package pl.radomiej.search.tools;

import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public enum GeoToolsHelper {
    INSTANCE;

    private boolean initSuccess;
    private CoordinateReferenceSystem sourceCRS;
    private CoordinateReferenceSystem targetCRS;
    private MathTransform transform;

    private GeoToolsHelper(){
        try {
            sourceCRS = CRS.decode("EPSG:2180");
            targetCRS = CRS.decode("EPSG:4326");
            transform = CRS.findMathTransform(sourceCRS, targetCRS);
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }

    public boolean transformToLatLng(Coordinate coordinate) {
        try {
            JTS.transform(coordinate, coordinate, transform);
        } catch (TransformException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean isInitSuccess() {
        return initSuccess;
    }
}
