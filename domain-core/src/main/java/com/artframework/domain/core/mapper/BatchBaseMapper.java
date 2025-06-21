package com.artframework.domain.core.mapper;

import java.util.List;

public interface BatchBaseMapper<DO> {
    int batchUpdate(List<DO> tList);

    void insertBatch(List<DO> tList);
}
