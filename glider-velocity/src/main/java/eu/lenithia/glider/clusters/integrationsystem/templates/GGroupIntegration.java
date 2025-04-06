package eu.lenithia.glider.clusters.integrationsystem.templates;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.lenithia.glider.GliderVelocity;
import eu.lenithia.glider.clusters.system.GGroup;
import lombok.Getter;

@Getter
public abstract class GGroupIntegration {

    private final GGroup gGroup;

    public GGroupIntegration(GGroup gGroup) {
        this.gGroup = gGroup;
    }

    public Section getGroupConfig() {
        return gGroup.getGroupConfig();
    }

    public GliderVelocity getGlider() {
        return gGroup.getGlider();
    }

    public abstract void onLoad();

    public abstract void onReload();

    public abstract void onUnload();

}
