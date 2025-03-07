package com.healthcare.visittracker.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    public static Pageable createPageRequest(Integer page, Integer size,
                                             String sortProperty, Sort.Direction direction) {
        int pageNumber = (page != null && page >= 0) ? page : DEFAULT_PAGE;
        int pageSize = (size != null && size > 0) ? Math.min(size, MAX_SIZE) : DEFAULT_SIZE;

        Sort sort = Sort.by(direction, sortProperty);
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    public static int calculateOffset(int page, int size) {
        return page * size;
    }

    public static int calculateTotalPages(long totalItems, int pageSize) {
        return (int) Math.ceil((double) totalItems / pageSize);
    }
}
