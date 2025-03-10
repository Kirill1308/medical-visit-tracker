package com.healthcare.visittracker.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtil {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    public static Pageable createPageRequest(Integer page, Integer size) {
        int pageNumber = (page != null && page >= 0) ? page : DEFAULT_PAGE;
        int pageSize = (size != null && size > 0) ? Math.min(size, MAX_SIZE) : DEFAULT_SIZE;

        return PageRequest.of(pageNumber, pageSize);
    }
}
