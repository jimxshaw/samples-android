package guildsa.org.weatherapp.data;

import org.json.JSONObject;

/*
    The Yahoo Weather API JSON object with the key-value pairs we need essentially look like this:

    "channel": {
        "units": { "temperature": "F"
        },
        "item": { "condition": {
                        "code": "33",
                        "temp": "-9",
                        "text": "Fair"
                    }
        }
    }

 */

public class Item implements JSONPopulator {
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
