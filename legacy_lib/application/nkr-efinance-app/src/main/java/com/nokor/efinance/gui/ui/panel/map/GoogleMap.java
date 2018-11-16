package com.nokor.efinance.gui.ui.panel.map;

import java.util.List;

import net.sf.json.JSONObject;

import com.nokor.efinance.gui.ui.panel.map.model.Marker;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;

import elemental.json.JsonArray;

@JavaScript({
    "http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js",
		"javascript/googlemap.js", "javascript/googlemap-connector.js" })
public class GoogleMap extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 1048232485001075315L;
	private OnMarkerClickListener markerListener = null;
    
	public void setOnMarkerClickListener(OnMarkerClickListener listener) {
		this.markerListener = listener;
    }

	public void setLatLng(double latitude, double longitude) {
		getState().setLatitude(latitude);
		getState().setLongitude(longitude);
		markAsDirty();
	}

	public void setMarkers(List<Marker> makerList) {
		String markers[] = new String[makerList.size()];

		for (int i = 0; i < markers.length; i++) {
			Marker markerObj = makerList.get(i);

			JSONObject jsonMarker = new JSONObject();
			jsonMarker.put("title", markerObj.getTitle());
            jsonMarker.put("iconUrl", markerObj.getIconUrl());
			jsonMarker.put("latitude", markerObj.getLatitude());
			jsonMarker.put("longitude", markerObj.getLongitude());
            jsonMarker.put("nbActivate", markerObj.getNbActivate().toJSONObject());
			jsonMarker.put("nbReject", markerObj.getNbReject().toJSONObject());
            jsonMarker.put("nbDecline", markerObj.getNbDecline().toJSONObject());
            jsonMarker.put("nbProposal", markerObj.getNbProposal().toJSONObject());

			markers[i] = jsonMarker.toString();
		}
		getState().setMarkers(markers);
		markAsDirty();
	}

	public void setZoomLevel(int zoomLevel) {
		getState().setZoomLevel(zoomLevel);
		markAsDirty();
	}
   
    @Override
    public GoogleMapState getState() {
        return (GoogleMapState) super.getState();
    }
   
	public GoogleMap() {
		addFunction("onMarkerClick", new JavaScriptFunction() {

			private static final long serialVersionUID = 8491565406159294325L;


			@Override
			public void call(JsonArray arguments) {
				if (markerListener != null) {
					markerListener.onClick((int) arguments.getNumber(0));
				}
			}
		});

		addFunction("onMarkerDoubleClick", new JavaScriptFunction() {

			private static final long serialVersionUID = -6990390773752090629L;

			@Override
			public void call(JsonArray arguments) {
				if (markerListener != null) {
					markerListener.onDoubleClick((int) arguments.getNumber(0));
                }
			}
		});

		addFunction("onClick", new JavaScriptFunction() {

			private static final long serialVersionUID = 3188727826220643234L;

			@Override
			public void call(JsonArray arguments) {

			}
		});
    }
}
