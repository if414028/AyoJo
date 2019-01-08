package com.jo.ayo.ayojo.data.model.post.list;

import java.util.List;

public class ReportData {
    private int count;
    private int page;
    private List<Report> rows;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Report> getRows() {
        return rows;
    }

    public void setRows(List<Report> rows) {
        this.rows = rows;
    }
}
