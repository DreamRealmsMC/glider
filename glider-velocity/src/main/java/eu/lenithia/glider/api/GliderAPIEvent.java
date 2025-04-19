package eu.lenithia.glider.api;

import lombok.Getter;

public class GliderAPIEvent {

    @Getter
    private GliderAPI gliderAPI;

    public GliderAPIEvent(GliderAPI gliderAPI) {
        this.gliderAPI = gliderAPI;
    }

}
