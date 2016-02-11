package com.synaptix.toast.dao.domain.impl.test.block;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.synaptix.toast.dao.domain.impl.test.block.line.CampaignLine;

import java.util.ArrayList;
import java.util.List;

@Embedded
public class CampaignBlock implements IBlock {

    private List<CampaignLine> testCases;

    private String campaignName;

    public CampaignBlock() {
    	this.testCases = new ArrayList<>();
    }

    public void addTestCase(final String name, final ITestPage testCase) {
        testCases.add(new CampaignLine(name, testCase));
    }

    public List<CampaignLine> getTestCases() {
        return testCases;
    }

    public void setTestCases(final List<CampaignLine> lines) {
        this.testCases = lines;
    }

    @Override
    public String getBlockType() {
        return "campaign";
    }

    @Override
    public int getHeaderSize() {
        return 1;
    }

    public void setCampaignName(final String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignName() {
        return campaignName;
    }
}