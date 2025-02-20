package view;
import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.Polyline;
import com.teamdev.jxmaps.PolylineOptions;
import com.teamdev.jxmaps.swing.MapView;

import model.data_structures.*;

import com.teamdev.jxmaps.Circle;
import com.teamdev.jxmaps.LatLngBounds;
import com.teamdev.jxmaps.CircleOptions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * Clase que representa un mapa generado a partir de un grafo
 * @author nicot
 */
public class MapVisual extends MapView
{
	/**
	 * Grafo a graficar en el mapa
	 */
	private Graph<Long, VertexContent, Double> mapa;
	/**
	 * Contructor de la clase
	 * @param pMapa
	 */
	public MapVisual(Graph<Long, VertexContent, Double> pMapa,LatLng min,LatLng max,Boolean hacerSoloVertices, Graph<Long,VertexContent,Double> pGrafoAdicional) 
	{
		mapa = pMapa;
		setOnMapReadyHandler(new MapReadyHandler() 
		{
			@Override
			public void onMapReady(MapStatus status) 
			{
				if(!hacerSoloVertices)
				{
					// Check if the map is loaded correctly
					if (status == MapStatus.MAP_STATUS_OK) 
					{
						// Getting the associated map objec
						final Map map = getMap();
						Iterator<Long> vertices = mapa.vertices();
						//Contador de vertices
						int n = 0;
						ArrayList<Polyline> arcos = new ArrayList<Polyline>();
						Iterator<Edge<Double>> colaArcos = mapa.edges().iterator();
						//Opciones para imprimir el mapa.
						PolylineOptions options = new PolylineOptions();
						// Setting geodesic property value
						options.setGeodesic(true);
						// Setting stroke color value
						options.setStrokeColor("#36D61C");
						// Setting stroke opacity value
						options.setStrokeOpacity(1.0);
						// Setting stroke weight value
						options.setStrokeWeight(2.0);
						// Applying options to the polyline
						CircleOptions op = new CircleOptions();
						op.setFillColor("#EC2C03");
						op.setStrokeColor("#EC2C03");
						//op.setStrokeOpacity(1.0);
						op.setRadius(2);
						ArrayList<Circle> circulos = new ArrayList<Circle>();

						while(colaArcos.hasNext()) 
						{
							Edge<Double> arc = colaArcos.next();
							double lat1 = mapa.getInfoVertex(mapa.translateInverse(arc.either())).coor.lat;
							double lon1 = mapa.getInfoVertex(mapa.translateInverse(arc.either())).coor.lon;
							double lat2 = mapa.getInfoVertex(mapa.translateInverse(arc.other(arc.either()))).coor.lat;
							double lon2 = mapa.getInfoVertex(mapa.translateInverse(arc.other(arc.either()))).coor.lon;

							if(n <2000&& lat1 >= min.getLat() && lat1 <= max.getLat() && lon1 >= min.getLng() && lon1 <= max.getLng()
									&& lat2 >= min.getLat() && lat2 <= max.getLat() && lon2 >= min.getLng() && lon2 <= max.getLng()) 
							{
								LatLng[] path = {new LatLng(lat1,lon1),new LatLng(lat2,lon2)};
								arcos.add(new Polyline(map));
								arcos.get(arcos.size()-1).setPath(path);
								arcos.get(arcos.size()-1).setOptions(options);
								circulos.add(new Circle(map));
								circulos.get(circulos.size()-1).setCenter(new LatLng(lat1,lon1));
								circulos.get(circulos.size()-1).setOptions(op);
								circulos.add(new Circle(map));
								circulos.get(circulos.size()-1).setCenter(new LatLng(lat2,lon2));
								circulos.get(circulos.size()-1).setOptions(op);
								n++;
							}
						}
						
						if(pGrafoAdicional != null) {

							vertices = mapa.vertices();
							//Contador de vertices
							n = 0;
							colaArcos = mapa.edges().iterator();
							//Opciones para imprimir el mapa.
							// Setting geodesic property value
							options.setGeodesic(true);
							// Setting stroke color value
							options.setStrokeColor("#C929E3");
							// Setting stroke opacity value
							options.setStrokeOpacity(1.0);
							// Setting stroke weight value
							options.setStrokeWeight(4.0);
							// Applying options to the polyline
							op.setFillColor("#13D3FC");
							op.setStrokeColor("#13D3FC");
							//op.setStrokeOpacity(1.0);
							op.setRadius(4);

							while(colaArcos.hasNext()) 
							{
								Edge<Double> arc = colaArcos.next();
								double lat1 = mapa.getInfoVertex(mapa.translateInverse(arc.either())).coor.lat;
								double lon1 = mapa.getInfoVertex(mapa.translateInverse(arc.either())).coor.lon;
								double lat2 = mapa.getInfoVertex(mapa.translateInverse(arc.other(arc.either()))).coor.lat;
								double lon2 = mapa.getInfoVertex(mapa.translateInverse(arc.other(arc.either()))).coor.lon;

								if(n <2000&& lat1 >= min.getLat() && lat1 <= max.getLat() && lon1 >= min.getLng() && lon1 <= max.getLng()
										&& lat2 >= min.getLat() && lat2 <= max.getLat() && lon2 >= min.getLng() && lon2 <= max.getLng()) 
								{
									LatLng[] path = {new LatLng(lat1,lon1),new LatLng(lat2,lon2)};
									arcos.add(new Polyline(map));
									arcos.get(arcos.size()-1).setPath(path);
									arcos.get(arcos.size()-1).setOptions(options);
									circulos.add(new Circle(map));
									circulos.get(circulos.size()-1).setCenter(new LatLng(lat1,lon1));
									circulos.get(circulos.size()-1).setOptions(op);
									circulos.add(new Circle(map));
									circulos.get(circulos.size()-1).setCenter(new LatLng(lat2,lon2));
									circulos.get(circulos.size()-1).setOptions(op);
									n++;
								}
							}
						}

						
						//						System.out.println("Numero de arcos graficados:: "+n);
						map.fitBounds(new LatLngBounds(min,max));
						// Setting initial zoom value
						map.setZoom(14);
					}
				}
				else
				{
					if (status == MapStatus.MAP_STATUS_OK) 
					{
						// Getting the associated map objec
						final Map map = getMap();
						Iterator<Long> vertices = mapa.vertices();
						//Contador de vertices
						int n = 0;
						// Applying options to the polyline
						CircleOptions op = new CircleOptions();
						op.setFillColor("#EC2C03");
						op.setStrokeColor("#EC2C03");
						//op.setStrokeOpacity(1.0);
						op.setRadius(2);
						ArrayList<Circle> circulos = new ArrayList<Circle>();

						while(vertices.hasNext())
						{
							Long id = vertices.next();
							double lat1 = mapa.getInfoVertex(id).coor.lat;
							double lon1 = mapa.getInfoVertex(id).coor.lon;

							if(n <2000&& lat1 >= min.getLat() && lat1 <= max.getLat() && lon1 >= min.getLng() && lon1 <= max.getLng()) 
							{
								circulos.add(new Circle(map));
								circulos.get(circulos.size()-1).setCenter(new LatLng(lat1,lon1));
								circulos.get(circulos.size()-1).setOptions(op);
								n++;
							}
						}

						if(pGrafoAdicional != null) {

							ArrayList<Polyline> arcos = new ArrayList<Polyline>();
							Iterator<Edge<Double>> colaArcos = mapa.edges().iterator();
							PolylineOptions options = new PolylineOptions();

							//Contador de vertices
							n = 0;
							colaArcos = mapa.edges().iterator();
							//Opciones para imprimir el mapa.
							// Setting geodesic property value
							options.setGeodesic(true);
							// Setting stroke color value
							options.setStrokeColor("#C929E3");
							// Setting stroke opacity value
							options.setStrokeOpacity(1.0);
							// Setting stroke weight value
							options.setStrokeWeight(4.0);
							// Applying options to the polyline
							op.setFillColor("#13D3FC");
							op.setStrokeColor("#13D3FC");
							//op.setStrokeOpacity(1.0);
							op.setRadius(4);

							while(colaArcos.hasNext()) 
							{
								Edge<Double> arc = colaArcos.next();
								double lat1 = mapa.getInfoVertex(mapa.translateInverse(arc.either())).coor.lat;
								double lon1 = mapa.getInfoVertex(mapa.translateInverse(arc.either())).coor.lon;
								double lat2 = mapa.getInfoVertex(mapa.translateInverse(arc.other(arc.either()))).coor.lat;
								double lon2 = mapa.getInfoVertex(mapa.translateInverse(arc.other(arc.either()))).coor.lon;

								if(n <2000&& lat1 >= min.getLat() && lat1 <= max.getLat() && lon1 >= min.getLng() && lon1 <= max.getLng()
										&& lat2 >= min.getLat() && lat2 <= max.getLat() && lon2 >= min.getLng() && lon2 <= max.getLng()) 
								{
									LatLng[] path = {new LatLng(lat1,lon1),new LatLng(lat2,lon2)};
									arcos.add(new Polyline(map));
									arcos.get(arcos.size()-1).setPath(path);
									arcos.get(arcos.size()-1).setOptions(options);
									circulos.add(new Circle(map));
									circulos.get(circulos.size()-1).setCenter(new LatLng(lat1,lon1));
									circulos.get(circulos.size()-1).setOptions(op);
									circulos.add(new Circle(map));
									circulos.get(circulos.size()-1).setCenter(new LatLng(lat2,lon2));
									circulos.get(circulos.size()-1).setOptions(op);
									n++;
								}
							}
						}
						
						//					System.out.println("Numero de arcos graficados:: "+n);
						map.fitBounds(new LatLngBounds(min,max));
						// Setting initial zoom value
						map.setZoom(14);
					}
				}
			}
		});
	}

	/**
	 * Metodo que genera la ventana del mapa 
	 * @param sample
	 */
	public static void graficarMapa(MapVisual sample)
	{
		JFrame frame = new JFrame("Washington D.C");

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(sample, BorderLayout.CENTER);
		frame.setSize(700, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}

