package fr.lip6.move.meduse.core.activator;

import org.osgi.framework.BundleContext;
import de.ovgu.featureide.fm.core.AbstractCorePlugin;

/**
 * Images plugin
 * 
 * @author Jabier Martinez
 *
 */
public class MeduseCorePlugin extends AbstractCorePlugin {

	public static final String PLUGIN_ID = "fr.lip6.move.core.meduse.composer";

	@Override
	public String getID() {
		return PLUGIN_ID;
	}

	private static MeduseCorePlugin plugin;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static MeduseCorePlugin getDefault() {
		return plugin;
	}

}
