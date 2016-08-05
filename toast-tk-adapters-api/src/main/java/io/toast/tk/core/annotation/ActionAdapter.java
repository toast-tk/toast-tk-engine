package io.toast.tk.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.toast.tk.core.adapter.ActionAdapterKind;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionAdapter {

	ActionAdapterKind value();

	String name();

	String description() default "";
}