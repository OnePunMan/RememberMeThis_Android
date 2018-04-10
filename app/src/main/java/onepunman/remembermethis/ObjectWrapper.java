package onepunman.remembermethis;

import java.io.Serializable;

public class ObjectWrapper<T> implements Serializable{
    private T _object;

    public ObjectWrapper(T object) {
        _object = object;
    }

    public T getObject() {
        return _object;
    }
}
