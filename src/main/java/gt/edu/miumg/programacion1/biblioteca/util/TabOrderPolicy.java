/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.List;

/**
 *
 * @author wgarciac
 */
public class TabOrderPolicy extends FocusTraversalPolicy {

    private final List<Component> orderedComponents;

    public TabOrderPolicy(List<Component> orderedComponents) {
        this.orderedComponents = orderedComponents;
    }

    @Override
    public Component getComponentAfter(Container aContainer, Component aComponent) {
        int idx = orderedComponents.indexOf(aComponent);
        return orderedComponents.get((idx + 1) % orderedComponents.size());
    }

    @Override
    public Component getComponentBefore(Container aContainer, Component aComponent) {
        int idx = orderedComponents.indexOf(aComponent);
        return orderedComponents.get((idx - 1 + orderedComponents.size()) % orderedComponents.size());
    }

    @Override
    public Component getFirstComponent(Container aContainer) {
        return orderedComponents.get(0);
    }

    @Override
    public Component getLastComponent(Container aContainer) {
        return orderedComponents.get(orderedComponents.size() - 1);
    }

    @Override
    public Component getDefaultComponent(Container aContainer) {
        return orderedComponents.get(0);
    }

}
