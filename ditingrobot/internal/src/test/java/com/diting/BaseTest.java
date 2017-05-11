package com.diting;
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * BaseTest.
 */
@ContextConfiguration(locations = {"classpath:/**/test-context.xml"})
public abstract class BaseTest extends AbstractTestNGSpringContextTests {

}
