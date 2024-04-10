package edu.sjsu.cmpe272.simpleblog.common.request;

import lombok.Data;

@Data
public class PaginatedRequest {
    Integer limit = 3;
    Integer next;
    Long startId;
}
