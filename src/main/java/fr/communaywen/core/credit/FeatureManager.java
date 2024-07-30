package fr.communaywen.core.credit;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Collaborators()
@Getter
public class FeatureManager {

    private final List<FeatureData> features = new ArrayList<>();

    public FeatureManager() {
        Reflections reflections = new Reflections("fr.communaywen.core");
        Set<Class<?>> creditedClasses = reflections.getTypesAnnotatedWith(Credit.class);
        for (Class<?> clazz : creditedClasses) {
            Credit credit = clazz.getAnnotation(Credit.class);
            Feature feature = clazz.getAnnotation(Feature.class);
            if (feature == null) {
                continue;
            }
            Collaborators collaborators = clazz.getAnnotation(Collaborators.class);
            if (collaborators == null) features.add(new FeatureData(feature.value(), credit.value()));
            else features.add(new FeatureData(feature.value(), credit.value(), collaborators.value()));
        }
    }

    public FeatureData getFeature(String name) {
        for (FeatureData feature : features) {
            if (feature.getFeature().equalsIgnoreCase(name)) {
                return feature;
            }
        }
        return null;
    }

}
