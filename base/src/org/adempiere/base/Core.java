package org.adempiere.base;

import java.net.URL;
import java.util.List;

/**
 * This is a facade class for the Service Locator.
 * It provides simple access to all core services.
 *
 * @author viola
 */
public class Core {

	public static final String OSGI_PREFIX = "osgi://";

	public static IResourceFinder getResourceFinder() {
		return new IResourceFinder() {

			public URL getResource(String name) {
				List<IResourceFinder> f = Service.list(IResourceFinder.class);
				for (IResourceFinder finder : f) {
					URL url = finder.getResource(name);
					if (url!=null)
						return url;
				}
				return null;
			}
		};
	}

	public static List<IColumnCallout> findCallout(String tableName, String columnName) {
		ServiceQuery query = new ServiceQuery();
		query.put("tableName", tableName);
		query.put("columnName", columnName);

		return Service.list(IColumnCallout.class, query);
	}

	public static boolean isExtension(String className) {
		return className.startsWith(OSGI_PREFIX);
	}


}
