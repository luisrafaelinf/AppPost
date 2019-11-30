package com.itla.apppost.apicontroller.interfaces;

import java.util.function.Consumer;

public interface ResponseContent<T> {

	abstract void apply(Consumer<T> consumer);

}
