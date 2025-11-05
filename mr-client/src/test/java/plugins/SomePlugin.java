package plugins;

/**
 * @author Andy 23.11.2017.
 */
public class SomePlugin implements IPluginInfo {

    public SomePlugin(SomeCore core) {
    }

    @Override
    public String getPluginInformation() {
        return "Reporting plugin";
    }

}
