package com.jo.ayo.ayojo.data.model.post.detail;

import com.jo.ayo.ayojo.data.model.Status;
import com.jo.ayo.ayojo.data.model.post.list.Report;

public class ReportDataDetailResult extends Status {
    private ReportDetail data;

    public ReportDetail getData() {
        return data;
    }

    public void setData(ReportDetail data) {
        this.data = data;
    }
}
