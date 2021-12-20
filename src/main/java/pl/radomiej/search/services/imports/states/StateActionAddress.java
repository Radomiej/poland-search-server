package pl.radomiej.search.services.imports.states;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import com.vividsolutions.jts.geom.Coordinate;

import pl.radomiej.search.domains.AddressNode;
import pl.radomiej.search.tools.GeoToolsHelper;

public enum StateActionAddress {
    NONE {
        @Override
        public void procces(AddressNode node, String text) {
        }
    },
    ADMINISTRACJA {
        @Override
        public void procces(AddressNode node, String text) {
            if (node.getCountry() == null) {
                node.setCountry(text);
            } else if (node.getVoivodeship() == null) {
                node.setVoivodeship(text);
            } else if (node.getCounties() == null) {
                node.setCounties(text);
            } else if (node.getGmine() == null) {
                node.setGmine(text);
            }
        }

    },
    MIEJSCOWOSC {
        @Override
        public void procces(AddressNode node, String text) {
            node.setCity(text);
        }
    },
    CZESC_MIEJSCOWOSC {
        @Override
        public void procces(AddressNode node, String text) {
        }
    },
    ULICA {
        @Override
        public void procces(AddressNode node, String text) {
            node.setStreet(text);
        }
    },
    NUMER_PORZADKOWY {
        @Override
        public void procces(AddressNode node, String text) {
            node.setHouseNumber(text);
        }
    },
    KOD_POCZTOWY {
        @Override
        public void procces(AddressNode node, String text) {
            node.setPostcode(text);
        }
    },
    POZYCJA {
        @Override
        public void procces(AddressNode node, String text) {
//			System.out.println("coords: " + text);
            String[] posText = text.split(" ");
            double x = Double.parseDouble(posText[0]);
            double y = Double.parseDouble(posText[1]);

            Coordinate coordinate = new Coordinate(x, y);

            GeoToolsHelper.INSTANCE.transformToLatLng(coordinate);

            node.setPosition(new GeoPoint(coordinate.x, coordinate.y));
//			System.out.println("Out coordinate: " + coordinate);
        }
    };

    public abstract void procces(AddressNode node, String text);
}
