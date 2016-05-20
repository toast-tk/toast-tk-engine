package com.synaptix.toast.adapter.cache;

import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;

@ActionAdapter(value = ActionAdapterKind.service, name = "cache-test")
public class CacheTestAdapter {
    @Action(action = "cacheAction", description = "no")
    public void cacheAction() {
    }
}