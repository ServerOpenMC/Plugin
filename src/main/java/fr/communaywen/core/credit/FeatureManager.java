package fr.communaywen.core.credit;

import lombok.Getter;
import org.reflections.Reflections;

import java.util.Set;

@Getter
public class CreditManager {

    private final Set<Class<?>> creditedClasses;

    public CreditManager() {
        Reflections reflections = new Reflections("fr.communaywen.core");
        this.creditedClasses = reflections.getTypesAnnotatedWith(Credit.class);
    }

    

}
