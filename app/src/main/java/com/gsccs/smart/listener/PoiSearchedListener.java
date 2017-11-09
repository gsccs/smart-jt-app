package com.gsccs.smart.listener;

import com.gsccs.smart.model.ErrorStatus;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import java.util.*;

/**
 * Created by x.d zhang on 2016/10/25.
 */
public interface PoiSearchedListener {
    public void onGetPoiMessage(List<PoiItem> poiItems, String poiTitle, LatLonPoint addressPoint, ErrorStatus status);
}
