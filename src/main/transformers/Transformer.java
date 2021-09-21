package main.transformers;

public interface Transformer<T, K> {
    T transform(K objectToTransform);
}
