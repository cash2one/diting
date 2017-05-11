/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.model.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.diting.util.Utils.str;

/**
 * WalletMapping.
 */
public final class WalletMapping {
    public static final Map<String, Map<WalletLotType, Integer>> LOT_PRIORITY_MAPPING =
            Collections.unmodifiableMap(new HashMap<String, Map<WalletLotType, Integer>>() {{
                put(str(WalletType.DIBI), new TreeMap<WalletLotType, Integer>() {{
                    put(WalletLotType.DIBI_D, 100);
                    put(WalletLotType.DIBI_WeChat,200);
                }});
            }});
}
