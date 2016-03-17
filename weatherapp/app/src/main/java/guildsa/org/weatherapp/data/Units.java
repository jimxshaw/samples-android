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

public class Units implements JSONPopulator {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
