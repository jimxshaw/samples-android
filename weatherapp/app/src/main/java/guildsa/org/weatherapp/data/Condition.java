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

public class Condition implements JSONPopulator {
    private int code;
    private int temperature;
    private String description;

    public int getCode() {
        return code;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void populate(JSONObject data) {
        code = data.optInt("code");
        temperature = data.optInt("temp");
        description = data.optString("text");
    }
}
