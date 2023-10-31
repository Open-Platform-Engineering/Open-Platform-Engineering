package codes.showme.server.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IncidentReq {
    @JsonProperty("routing_key")
    private String routingKey;
    @JsonProperty("event_action")
    private EventAction eventAction;
    @JsonProperty("dedup_key")
    private String dedupKey;
    @JsonProperty("payload")
    private Payload payload;
    @JsonProperty("images")
    private List<ImageContext> images;
    @JsonProperty("links")
    private List<LinkContext> links;

    public EventAction getEventAction() {
        return eventAction;
    }

    public void setEventAction(EventAction eventAction) {
        this.eventAction = eventAction;
    }

    public String getDedupKey() {
        return dedupKey;
    }

    public void setDedupKey(String dedupKey) {
        this.dedupKey = dedupKey;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public List<ImageContext> getImages() {
        return images;
    }

    public void setImages(List<ImageContext> images) {
        this.images = images;
    }

    public List<LinkContext> getLinks() {
        return links;
    }

    public void setLinks(List<LinkContext> links) {
        this.links = links;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
