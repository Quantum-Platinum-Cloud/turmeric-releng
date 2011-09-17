/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.utils.cassandra.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import org.ebayopensource.turmeric.utils.cassandra.dao.SuperModelDao;
import org.ebayopensource.turmeric.utils.cassandra.dao.SuperModelDaoImpl;
import org.ebayopensource.turmeric.utils.cassandra.model.Model;
import org.ebayopensource.turmeric.utils.cassandra.model.SuperModel;
import org.ebayopensource.turmeric.utils.cassandra.server.CassandraTestManager;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The Class ModelDaoTest.
 * 
 * @author jamuguerza
 */
public class SuperModelDaoTest extends BaseTest {

	/** The test Super model dao. */
	private static SuperModelDao testSuperModelDao;

	/** The SUPER_KEY. */
	private static String SUPER_KEY = "superKey_001";

	/** The KEY. */
	private static String KEY = "key_aaa01";

	/**
	 * Before.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		CassandraTestManager.initialize();
		testSuperModelDao = new SuperModelDaoImpl(TURMERIC_TEST_CLUSTER, HOST, KEY_SPACE,
				"TestSuperCF");
	}

	@After
	public void after() throws Exception {
		for (String superKey : testSuperModelDao.getAllKeys()) {
			SuperModel superModel = new SuperModel();
			superModel.setKey(superKey);
			testSuperModelDao.delete(superModel);
		}
		Thread.sleep(3000);
	}

	@Test
	public void testSave() {
		SuperModel testSuperModel = createSuperModel();

		// save
		testSuperModelDao.save(testSuperModel, testSuperModel.getColumns());

		// find
		testSuperModel = testSuperModelDao.find(SUPER_KEY);
		assertNotNull(testSuperModel);
	}



	@Test
	public void testDelete() {
		SuperModel testSuperModel = createSuperModel();
		testSuperModel.setKey(SUPER_KEY);
		// save
		testSuperModelDao.save(testSuperModel, testSuperModel.getColumns());

		// find
		testSuperModel = testSuperModelDao.find(SUPER_KEY);
		assertNotNull(testSuperModel);

		// delete
		testSuperModelDao.delete(testSuperModel);
//		assertFalse(testSuperModelDao.containsKey(SUPER_KEY));
		testSuperModel = testSuperModelDao.find(SUPER_KEY);
		assertTrue(testSuperModel == null);
	}

	@Test
	public void testGetAllKeys() {

		SuperModel testSuperModel = createSuperModel();

		// save
		for (int i = 0; i < 20; i++) {
			testSuperModel.setKey(SUPER_KEY + i);
			testSuperModelDao.save(testSuperModel, testSuperModel.getColumns());
		}

		// gelAllKeys
		Set<String> allKeys = testSuperModelDao.getAllKeys();

		assertEquals(20, allKeys.size());
		assertTrue(allKeys.contains(testSuperModel.getKey()));
	}

	@Test
	public void testFindItems() {

		// findItems
		ArrayList<String> superKeyList = new ArrayList<String>();
		superKeyList.add(SUPER_KEY + "findItem_001");
		superKeyList.add(SUPER_KEY + "findItem_002");
		superKeyList.add(SUPER_KEY + "findItem_003");

		SuperModel testSuperModel0 = createSuperModel();
		testSuperModel0.setKey(superKeyList.get(0));
		SuperModel testSuperModel1 = createSuperModel();
		testSuperModel1.setKey(superKeyList.get(1));
		SuperModel testSuperModel2 = createSuperModel();
		testSuperModel2.setKey(superKeyList.get(2));

		// save
		testSuperModelDao.save(testSuperModel0, testSuperModel0.getColumns());
		testSuperModelDao.save(testSuperModel1, testSuperModel1.getColumns());
		testSuperModelDao.save(testSuperModel2, testSuperModel2.getColumns());

		 Map<String, SuperModel> result = testSuperModelDao.findSuperItems(superKeyList, Arrays.asList("stringData", "booleanData"), Arrays.asList(KEY+ "model1", KEY+ "model3"), "", "");
		assertNotNull(result);
		assertEquals(3, result.size());

	}

	private SuperModel createSuperModel() {
		SuperModel testSuperModel = new SuperModel();
		
		Model model1 = createModel();
		model1.setKey(model1.getKey()  + "model1");
		Model model2 = createModel();
		model2.setKey(model2.getKey() + "model2");
		Model model3 = createModel();
		model3.setKey(model3.getKey()  + "model3");
		
		HashMap<String, Model> columns = new HashMap<String, Model>();
		columns.put(model1.getKey(), model1);
		columns.put(model2.getKey(), model2);
		columns.put(model3.getKey(), model3);
		
		testSuperModel.setKey(SUPER_KEY);
		testSuperModel.setColumns(columns);

		return testSuperModel;
	}
	
	private Model createModel() {
		Model testModel = new Model();
		testModel.setKey(KEY);
		testModel.setBooleanData(Boolean.TRUE);
		testModel.setIntData(Integer.MAX_VALUE);
		testModel.setLongData(Long.MAX_VALUE);
		testModel.setStringData("any String");
		testModel.setTimeData(new Date(System.currentTimeMillis()));
		return testModel;
	}

}