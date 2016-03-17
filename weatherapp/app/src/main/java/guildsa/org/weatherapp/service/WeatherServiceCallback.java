package guildsa.org.weatherapp.service;

import guildsa.org.weatherapp.data.Channel;

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
