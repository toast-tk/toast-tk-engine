package io.toast.tk.dao.domain.impl.test.block.line;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;

import io.toast.tk.dao.domain.impl.test.block.ITestPage;

@Entity(value = "test", noClassnameStored = true)
@Embedded
public class CampaignLine {

    private String name;

    private ITestPage file;

    public CampaignLine() {

    }

    public CampaignLine(final String name, final ITestPage file) {
        this.setName(name);
        this.setFile(file);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ITestPage getFile() {
        return file;
    }

    public void setFile(final ITestPage file) {
        this.file = file;
    }
}