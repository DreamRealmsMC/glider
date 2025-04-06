package eu.lenithia.glider.clusters.events;

import eu.lenithia.glider.clusters.system.GGroup;
import lombok.Getter;

@Getter
public class GGroupLoadEvent {

    private final GGroup group;

    public GGroupLoadEvent(GGroup group) { this.group = group; }

}
