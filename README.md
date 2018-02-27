#interflower
*Library for combining java objects based on the mechanism of Reflection*

##Example

######Class for experiments
```java
public class Pojo {

    int integer;
    public Integer wrp;
    private String string;
    
    public Pojo() {
    }
    
    public Pojo(int integer, Integer wrp, String string) {
        this.integer = integer;
        this.wrp = wrp;
        this.string = string;
    }
    
    public String toString() {
        return "Pojo{integer=" + integer + ", wrp=" + wrp + ", string='" + string + '}';
    }
}
```

Create a List, which will have three instances of this class with different parameters

```java
    List<Pojo> pojos = new ArrayList<>();
    pojos.add(new Pojo(1, 11, "pojo1"));
    pojos.add(new Pojo(2, 22, "pojo2"));
    pojos.add(new Pojo(3, 33, "pojo3"));
```
Then create object Merger, and in the constructor we will pass our List
```java
    Merger<Pojo> merger = new Merger<>(pojos);
```
Next, to get the combined object, we need to call the method _getResult()_   
There are two ways to use the _getResult()_ method

* ***getResult()*** without parameters - merges all objects into the first instance in the list
* ***getResult(Object container)*** - merges all objects into the transferred container

OK. Let's merge everything into a new instance and see what happens
```java
    Pojo result = merger.getResult(new Pojo());
    System.out.println(result);
```
**CMD**
```cmd
Pojo{integer=0, wrp=11, string='pojo1}
```

***to be continued...***

