package module6;

import processing.core.PApplet;
import processing.core.PConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import de.fhpotsdam.unfolding.providers.*;

import java.util.List;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;

import java.util.HashMap;


import de.fhpotsdam.unfolding.marker.Marker;

/**
 * Visualizes life expectancy in different countries. 
 * 
 * It loads the country shapes from a GeoJSON file via a data reader, and loads the population density values from
 * another CSV file (provided by the World Bank). The data value is encoded to transparency via a simplistic linear
 * mapping.
 */
public class UrbanPopulationMap extends PApplet {

	UnfoldingMap map;
	HashMap<String, Float> urbPopMap;
	List<Feature> countries;
	List<Marker> countryMarkers;

	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);

		// Load lifeExpectancy data
		urbPopMap = ParseFeed.loadUrbanPopulationCSV(this,"urbanPopulations.csv");

		// Load country polygons and adds them as markers
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		System.out.println(countryMarkers.get(0).getId());
		
		// Country markers are shaded according to life expectancy (only once)
		shadeCountries();
	}

	public void draw() {
		// Draw map tiles and country markers
		map.draw();
	}

	//Helper method to color each country based on life expectancy
	//Red-orange indicates low (near 40)
	//Blue indicates high (near 100)
	private void shadeCountries() {
		float x;
		float y;
		Location loc;
		
		for (Marker marker : countryMarkers) {
			// Find data for country of the current marker
			String countryId = marker.getId();
			System.out.println(urbPopMap.containsKey(countryId));
			if (urbPopMap.containsKey(countryId)) {
				float urbanPop = urbPopMap.get(countryId);
				// Encode value as brightness (values range: 40-90)
				int colorLevel = (int) map(urbanPop, 40, 90, 10, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			}
			else {
				marker.setColor(color(150,150,150));
			}
			
			loc = marker.getLocation();
			x = loc.x;
			y = loc.y;
//			pushStyle();
//			
//			fill(255, 255, 255);
//			textSize(12);
//			rectMode(PConstants.CORNER);
//			rect(x, y-44, Math.max(textWidth("test"), textWidth("elsetest")) + 6, 39);
//			fill(0, 0, 0);
//			textAlign(PConstants.LEFT, PConstants.TOP);
//			text("test", x+3, y-38);
//			text("yourself", x+3, y - 23);
			
			text("test_yourself", x, y);
//			popStyle();
			
			
		}
	}


}
