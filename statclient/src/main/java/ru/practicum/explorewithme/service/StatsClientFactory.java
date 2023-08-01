package ru.practicum.explorewithme.service;

public final class StatsClientFactory {

    private StatsClientFactory() {

    }

    public static StatClient getClient(String uri) {
        return new StatClientImpl(uri);
    }
}
