package org.allaymc.server.plugin.js;

import lombok.SneakyThrows;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.plugin.PluginContainer;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.IOAccess;

/**
 * @author daoge_cmd
 */
public class JsPlugin extends Plugin {

    protected Context jsContext;
    protected Value jsExport;
    protected JSPluginProxyLogger proxyLogger;

    @Override
    public void setPluginContainer(PluginContainer pluginContainer) {
        super.setPluginContainer(pluginContainer);
        proxyLogger = new JSPluginProxyLogger(pluginLogger);
    }

    @SneakyThrows
    @Override
    public void onLoad() {
        // ClassCastException won't happen
        var chromeDebugPort = ((JsPluginDescriptor) pluginContainer.descriptor()).getDebugPort();
        var cbd = Context.newBuilder("js")
                .allowIO(IOAccess.ALL)
                .allowAllAccess(true)
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLoading(true)
                .allowHostClassLookup(className -> true)
                .allowExperimentalOptions(true)
                .option("js.esm-eval-returns-exports", "true");
        if (chromeDebugPort > 0) {
            pluginLogger.info("Debug mode for js plugin {} is enabled. Port: {}", pluginContainer.descriptor().getName(), chromeDebugPort);
            // Debug mode is enabled
            cbd.option("inspect", String.valueOf(chromeDebugPort))
                    .option("inspect.Path", pluginContainer.descriptor().getName())
                    .option("inspect.Suspend", "true")
                    .option("inspect.Internal", "true")
                    .option("inspect.SourcePath", pluginContainer.loader().getPluginPath().toFile().getAbsolutePath());
        }
        jsContext = cbd.build();

        initGlobalMembers();

        var entranceJsFileName = pluginContainer.descriptor().getEntrance();
        var path = pluginContainer.loader().getPluginPath().resolve(entranceJsFileName);
        jsExport = jsContext.eval(
                Source.newBuilder("js", path.toFile())
                        .name(entranceJsFileName)
                        .mimeType("application/javascript+module")
                        .build()
        );
        tryCallJsFunction("onLoad");
    }

    protected void initGlobalMembers() {
        var binding = jsContext.getBindings("js");
        binding.putMember("plugin", this);
        binding.putMember("console", proxyLogger);
    }

    @Override
    public void onEnable() {
        tryCallJsFunction("onEnable");
    }

    @Override
    public void onDisable() {
        tryCallJsFunction("onDisable");
        jsContext.close(true);
    }

    @Override
    public boolean isReloadable() {
        return true;
    }

    @Override
    public void reload() {
        onDisable();
        onLoad();
        onEnable();
    }

    protected void tryCallJsFunction(String onLoad) {
        var func = jsExport.getMember(onLoad);
        if (func != null && func.canExecute())
            func.executeVoid();
    }
}