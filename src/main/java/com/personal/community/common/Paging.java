package com.personal.community.common;

import lombok.Data;

@Data
public class Paging {
    private int page;
    private int size;
    private Long totalElement;

    public static Paging of(int page, int size, Long totalElement) {
        Paging paging = new Paging();
        paging.page = page;
        paging.size = size;
        paging.totalElement = totalElement;
        return paging;
    }
}
