package com.nokor.frmk.vaadin.util.json;

import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;


/**
 * 
 * @author ly.youhort
 *
 */
public class IndexedJsonContainer extends IndexedContainer implements JsonContainer {

	private static final long serialVersionUID = -5769922488284612212L;

	/**
	 * @param parsedJsonData
	 */
	public IndexedJsonContainer(JsonElement parsedJsonData) {
        if (parsedJsonData.isJsonArray()) {
            populateContainer(parsedJsonData.getAsJsonArray());
        } else if (parsedJsonData.isJsonObject()) {
            addJsonObject(parsedJsonData.getAsJsonObject());
        }
    }

	/**
	 * @param parsedJsonArray
	 */
    private void populateContainer(JsonArray parsedJsonArray) {
        for (JsonElement j : parsedJsonArray) {
            if (j.isJsonObject()) {
                addJsonObject(j.getAsJsonObject());
            }
        }
    }

    /**
     * @param jsonObject
     */
    private void addJsonObject(JsonObject jsonObject) {
        // use itemId generated by IndexedContainer
        Object itemId = addItem();
        Item i = getItem(itemId);
        for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            addContainerProperty(entry.getKey(), String.class, null);
            i.getItemProperty(entry.getKey()).setValue(entry.getValue().getAsString());
        }
    }
}
