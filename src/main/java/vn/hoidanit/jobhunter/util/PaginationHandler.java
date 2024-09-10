package vn.hoidanit.jobhunter.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationHandler {
    //    public static Pageable getPageableObject(Optional<String> currentOptional, Optional<String> pageSizeOptional) {
    public static Pageable getPageableObject(int currentPage, int pageSize) {
//        String sCurrentPage = currentOptional.orElse("1"); // default page 1
//        String sPageSize = pageSizeOptional.orElse("5"); // default 5 queries
//        int currentPage = Integer.parseInt(sCurrentPage);
//        int pageSize = Integer.parseInt(sPageSize);
        return PageRequest.of(currentPage - 1, pageSize);
    }
}
