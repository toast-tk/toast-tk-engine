package com.synaptix.toast.runtime.result;

import com.synaptix.toast.runtime.IActionItemRepository;

public abstract class AbstractResultHandler<T> implements IResultHandler<T> {

    protected IActionItemRepository objectRepository;

    public AbstractResultHandler(IActionItemRepository objectRepository) {
        this.objectRepository = objectRepository;
    }
}
