package codes.showme.techlib.pagination;

public class PageRequest {
    private int pageIndex;
    private int pageSize;

    private PageRequest() {

    }

    public PageRequest(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public int getFirstRow() {
        return Math.max(getPageIndex() - 1, 0);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }


}
