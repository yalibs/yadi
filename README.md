# yadi
Yet another framework-agnostic skyhook dependency injection implementation for Java applications.

This library is a fairly simple global store where you can store object instances and `Supplier<T>`'s.
Even though it is simple, it can be very powerful.

## Usage
Say you have an interfaced class called `MyFoo` like so:

```java
interface IFooable {
    void foo();
}

class MyFoo implements IFooable {
    @Override
    public void foo() {
        // Do a thing
    }
}
```

Then you would inject the instance at program start like so:

```java
public class Main {
    public static void main(String[] args) {
        DI.add(IFooable.class, new MyFoo());
        DI.lock(); // Do not allow any other place in the application to add more stuff to DI (optional)
        // start your application
    }
}

// ... further inside the application code ...

public class SomeService {
    private final IFooable injectedFooInstance;

    public SomeService() {
        this.injectedFooInstance = DI.get(IFooable.class);
    }
}
```


