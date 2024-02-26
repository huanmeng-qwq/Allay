package org.allaymc.api.component.interfaces;

/**
 * Allay Project 2023/5/6
 *
 * @author daoge_cmd
 */
public interface ComponentManager<T> {
    <E> E callEvent(E event);

    T getComponentedObject();
}
