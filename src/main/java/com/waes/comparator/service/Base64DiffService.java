package com.waes.comparator.service;

import com.waes.comparator.controller.request.AspectEnum;

/**
 * Created by volkangumus on 14.07.2019
 */
public interface Base64DiffService {

    void save(Long id, String data, AspectEnum aspect);

    String getDiff(Long id);
}

