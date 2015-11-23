package com.synaptix.toast.dao.domain.impl.test.block.line;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;

@Entity(value = "test", noClassnameStored = true)
@Embedded
public class CampaignLine {

    private String name;

    private ITestPage file;

    public CampaignLine() {
    }

    public CampaignLine(String name, ITestPage file) {
        this.setName(name);
        this.setFile(file);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ITestPage getFile() {
        return file;
    }

    public void setFile(ITestPage file) {
        this.file = file;
    }
}