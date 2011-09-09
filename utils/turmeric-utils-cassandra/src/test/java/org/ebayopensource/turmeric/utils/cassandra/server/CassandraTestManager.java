/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.utils.cassandra.server;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.service.EmbeddedCassandraService;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.utils.cassandra.CassandraServiceDataCleaner;

/**
 * The Class CassandraTestManager.
 * @author jamuguerza
 */
public class CassandraTestManager {

	/** The cassandra service. */
	private static EmbeddedCassandraService cassandraService = null;

	/**
	 * Set embedded cassandra.
	 *
	 * @throws TTransportException the t transport exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 * @throws ConfigurationException the configuration exception
	 */
	public static void initialize() throws TTransportException, IOException,
			InterruptedException, ConfigurationException {
		
		loadConfig();

		CassandraServiceDataCleaner.prepare();
		
		if (cassandraService == null) {
			cassandraService = new EmbeddedCassandraService();
			cassandraService.start();
		}

	}

	/**
	 * Load config.
	 */	
	private static void loadConfig() {
		// use particular test properties, maybe with copy method
		System.setProperty("log4j.configuration",
				"META-INF/config/cassandra/log4j.properties");

		System.setProperty("cassandra.config",
				"META-INF/config/cassandra/cassandra-test.yaml");
	}

}