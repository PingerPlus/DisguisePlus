package net.pinger.disguiseplus.task;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.pinger.disguise.Skin;
import net.pinger.disguise.http.HttpRequest;
import net.pinger.disguise.http.HttpResponse;
import net.pinger.disguise.http.request.HttpGetRequest;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.SkinFactory;
import net.pinger.disguiseplus.utils.HttpUtil;
import net.pinger.disguiseplus.utils.ReferenceUtil;
import net.pinger.disguiseplus.utils.SkinUtil;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SkinDownloadTask extends BukkitRunnable {

	// The factory to load the skins into
	private final SkinFactory factory;

	public SkinDownloadTask(SkinFactory factory) {
		this.factory = factory;
	}

	@Override
	public void run() {
		try {
			// Establish the connection
			HttpRequest request = new HttpGetRequest(HttpUtil.CATEGORY_URL);
			HttpResponse response = request.connect();

			// This is essentially the categories.json file
			JsonObject object = DisguisePlus.GSON.fromJson(response.getResponse(), JsonObject.class);

			for (Map.Entry<String, JsonElement> element : object.entrySet()) {
				for (JsonElement packName : element.getValue().getAsJsonArray()) {
					// Check if the item
					// Is not already loaded
					if (this.factory.getSkinPack(element.getKey(), packName.getAsString()) != null)
						continue;

					this.createSkinPack(element.getKey(), packName.getAsString());
				}
			}
		} catch (IOException e) {
			DisguisePlus.getOutput().error("Failed to load skin packs -> ");
			DisguisePlus.getOutput().error(e.getMessage());
		}
	}

	private void createSkinPack(String category, String name) throws IOException {
		List<Skin> skins = new ArrayList<>();

		HttpRequest request = new HttpGetRequest(HttpUtil.toSkinPack(category, name));
		HttpResponse response = request.connect();

		// Getting the skins
		JsonArray array = ReferenceUtil.GSON.fromJson(response.getResponse(), JsonArray.class);

		// Looping through each skin
		for (JsonElement object : array) {
			skins.add(SkinUtil.getFromJson(object.getAsJsonObject()));
		}

		// Create the skin pack at last
		this.factory.createSkinPack(category, name, skins, false);
	}

}
