package com.synaptix.toast.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {

	String id() default "";
	
	String action();

	String description();

	String[] examples() default {};

	boolean display() default false;
}