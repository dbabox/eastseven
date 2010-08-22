package org.dongq.database.tools;

import org.dongq.database.DatabaseTools;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		DatabaseTools tools = new DatabaseTools();
		tools.start();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
