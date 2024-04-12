package edu.sjsu.cmpe272.simpleblog.common.request;

import lombok.Data;

@Data
public class PaginatedRequest {
    Integer limit = 10;
    Long next;
    Integer page = 0;
}
