package org.allaymc.api.plugin;

import org.cloudburstmc.protocol.common.util.Preconditions;

import java.util.List;

/**
 * PluginDescriptor contains the metadata of a plugin.
 *
 * @author daoge_cmd
 */
public interface PluginDescriptor {

    static void checkDescriptorValid(PluginDescriptor descriptor) {
        Preconditions.checkNotNull(descriptor.getName(), "Plugin name cannot be null");
        Preconditions.checkNotNull(descriptor.getEntrance(), "Plugin entrance cannot be null");
        Preconditions.checkNotNull(descriptor.getVersion(), "Plugin version cannot be null");
        Preconditions.checkNotNull(descriptor.getAuthors(), "Plugin authors cannot be null");
    }

    String getName();

    String getEntrance();

    // Plugins can leave this information unavailable
    String getDescription();

    String getVersion();

    List<String> getAuthors();

    // Plugins can leave this information unavailable
    List<PluginDependency> getDependencies();

    // Plugins can leave this information unavailable
    String getWebsite();
}