package info.kgeorgiy.ja.samsikova.implementor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class providing wrapper to be able to compare {@link java.lang.reflect.Method} instances.
 */
public class MethodWrapper {

    /**
     * Inner method
     */
    private final Method method;

    /**
     * @param method inner method
     */
    public MethodWrapper(Method method) {
        this.method = method;
    }

    /**
     * @return the inner method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof MethodWrapper)) {
            return false;
        }
        MethodWrapper other = (MethodWrapper) object;
        return other.method.getName().equals(method.getName())
                && Arrays.equals(other.method.getParameterTypes(), method.getParameterTypes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(method.getName(), Arrays.hashCode(method.getParameterTypes()));
    }
}
