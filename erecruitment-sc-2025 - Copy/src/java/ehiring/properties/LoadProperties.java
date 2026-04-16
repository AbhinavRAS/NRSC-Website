/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehiring.properties;

import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {
	public String getProperty(String name) throws Exception {
		String value = "";
		try {
			// load the environment variables from the file "ppsIntranet.properties"
			InputStream fi = getClass().getResourceAsStream("/ehiring.properties");
			if (fi == null) {
				throw new Exception("InputStream is null");
			}
			Properties p = new Properties(System.getProperties());
			p.load(fi);
			// set the system properties
			System.setProperties(p);
			value = p.getProperty(name).trim();
                        fi.close(); // ChangeId: 2024020501
		} catch (Exception e) {
			throw new Exception(
					"Unable to read the properties file. " + "Make sure ehiring.properties is in the CLASSPATH");
		}
		return value;
	}
}