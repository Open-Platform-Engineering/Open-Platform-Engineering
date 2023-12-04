package codes.showme.techlib.pagination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagination<T> implements Serializable {
    private static final long serialVersionUID = 2373637336667452864L;

    public static final Integer DEFAULT_PAGESIZE = 20;
    public static final Integer DEFAULT_PAGEINDEX = 1;

    /**
     * 页码，默认是第一页
     */
    private int pageIndex = 1;

    /**
     * 每页显示的记录数
     */
    private int pageSize = 20;

    /**
     * 总记录数
     */
    private long totalRecord;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 对应的当前页记录
     */
    private List<T> results;

    private int offset;

    /**
     * 页码数组
     */
    private int pageArraySize = 6;

    /**
     * 查询耗时
     */
    private long tookTimeValueMs;

    public Pagination() {
    }

    public Pagination(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.offset = pageIndex * pageSize;
    }


    public Pagination(int pageIndex, int pageSize, long totalRecord, List<T> results) {
        this(pageIndex, pageSize);
        this.totalRecord = totalRecord;
        this.results = results;
        this.totalPage = pageSize == 0 ? 1 : (int) Math.ceil((double) totalRecord / (double) pageSize);
    }

    public static <T> Pagination<T> empty() {
        return new Pagination<T>(0, 0, 0, new ArrayList<>());
    }

    public static int buildDbQueryFirstRow(int pageNum, int limit) {
        int limitInner = buildDbQueryLimit(limit);
        int firstRow = (Math.max(pageNum, 1) - 1) * limitInner;
        return firstRow;
    }

    public static int buildDbQueryLimit(int limit){
        return Math.min(Pagination.DEFAULT_PAGESIZE, limit);
    }

    public boolean hasNext() {
        return getPageIndex() + 1 <= totalPage;
    }

    public boolean isLast() {
        return !hasNext();
    }

    public boolean hasPre() {
        return totalPage > 1 && pageIndex > 1;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<T> getResults() {
        return results;
    }

    public int getOffset() {
        return offset;
    }

    public int getNextPageIndex() {
        return pageIndex + 1;
    }

    public int getPrePageIndex() {
        return pageIndex - 1;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int[] getPageArray() {
        if (totalPage >= pageArraySize) {
            int[] result = new int[pageArraySize];
            int a = (int) (pageArraySize / 2.0);
            int start = pageIndex - a;
            if (start <= 0) {
                start = 1;
            }
            int end = pageIndex + a;
            if (end > totalPage) {
                start = start - (end - totalPage) + 1;
                end = totalPage;
            }
            for (int i = 0; i < pageArraySize; i++) {
                result[i] = start + i;
            }
            return result;
        } else {
            int[] result = new int[totalPage];
            for (int i = 0; i < totalPage; i++) {
                result[i] = i + 1;
            }
            return result;
        }

    }

    public boolean isCurrent(int index) {
        return pageIndex == index;
    }

    public int getPageArraySize() {
        return pageArraySize;
    }

    public void setPageArraySize(int pageArraySize) {
        this.pageArraySize = pageArraySize;
    }

    public void setTookTimeValueMs(long tookTimeValueMs) {
        this.tookTimeValueMs = tookTimeValueMs;
    }

    public long getTookTimeValueMs() {
        return tookTimeValueMs;
    }
}
