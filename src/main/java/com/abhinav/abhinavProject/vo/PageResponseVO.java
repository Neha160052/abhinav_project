package com.abhinav.abhinavProject.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponseVO<T> {
    int pageNumber;
    int pageSize;
    boolean hasNextPage;
    T content;

    public PageResponseVO(int pageNumber, int pageSize, boolean hasNextPage, T content) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.hasNextPage = hasNextPage;
        this.content = content;
    }
}
