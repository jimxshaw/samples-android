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

public class Channel implements JSONPopulator {
    private Units units;
    private Item item;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public void populate(JSONObject data) {
        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));
    }
}
