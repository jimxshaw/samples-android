package guildsa.org.weatherapp.data;

import org.json.JSONObject;

public class Item implements JSONPopulator {
    private Condition condition;

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
