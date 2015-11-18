package com.synaptix.toast.dao.domain.impl.test.block;

import java.util.List;

public interface ITestPage extends ITaggable, IBlock {

    List<IBlock> getBlocks();

    long getExecutionTime();

    int getTechnicalErrorNumber();

    int getTestFailureNumber();

    int getTestSuccessNumber();

    void setTechnicalErrorNumber(int technicalErrorNumber);

    void setTestFailureNumber(int testFailureNumber);

    void setTestSuccessNumber(int testSuccessNumber);

    String getName();

    void setId(String id);

    boolean isPreviousIsSuccess();

    void setPreviousIsSuccess(boolean isSuccess);

    long getPreviousExecutionTime();

    void startExecution();

    void stopExecution();

    Object getParsingErrorMessage();

    String getIdAsString();

    void setPreviousExecutionTime(long previousExecutionTime);

    long getStartDateTime();

    void setName(String name);

    void addBlock(IBlock block);
}
