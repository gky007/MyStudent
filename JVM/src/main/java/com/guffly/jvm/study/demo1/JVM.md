# JVM-运行时数据区域
## 运行时数据区
JVM虚拟机规范要求，运行时数据分为七个区域：程序计数器、Java堆、方法区、虚拟机栈、本地方法栈、运行时常量池以及本地内存(也被称为堆外内存或直接内存。

![Image text](http://121.43.100.98:8888/group1/M00/00/02/rBC9b2HB9g-ANv3JAAA1uaDpdsY718.jpg)


其中程序计数器、虚拟机栈和本地方法栈是线程私有的。由于Java支持多线程执行，因此每个线程需要保存当前线程的运行时信息，包括当前执行的代码位置，以及其他运行时所必要的信息。

# 程序计数器
程序计数器在JAVA虚拟机规范中称为Program Counter Register，即为PC寄存器，它可以看作当前线程所执行的字节码行号指示器，字节码解释器工作时通过改变这个计数器的值来选取下一条需要执行的字节码指令。

行号指示器的行号，并不是我们直接理解的行号，实际是字节码的索引号。JVM的字节码占用1个字节，而常量池索引占用2个字节。因此下面的例子中，第一行字节码共占用3个字节。所以第二行dup的行号为3。

![Image text](http://121.43.100.98:8888/group1/M00/00/02/rBC9b2HB9r6ABf6rAABtmrvyAx8091.jpg)

需要注意，只有执行的是非本地（Native）方法，程序寄存器才会记录JAVA虚拟机正在执行的字节码指令地址，若当前执行方法是本地方法,则程序计数器的值为空(Undefined)。

# 方法区
在编译产生的class文件中，包含有类的类型信息和常量池等信息，这些信息在类加载时会被加载到运行时的方法区中。方法区主要用于存储被虚拟机加载的类信息、静态变量、JIT后的代码字节码缓存、运行池常量。虚拟机规范把方法区列为堆的一部分，但是虚拟机实现可以不实现方法区的自动垃圾回收，而是依赖于对常量池和类型的卸载来完成。

![Image text](http://121.43.100.98:8888/group1/M00/00/02/rBC9b2HB9z2AUleLAABiEPX6494086.jpg)

# 实现方式
在JDK1.7之前，HotSpot是使用GC的永久代来实现方法区，省去了专门编写方法区的内存管理代码。 从JDK1.8开始，使用元空间替代永久代来存放方法区的数据。元空间属于本地内存。简而言之使用了本地内存替换堆内存来存放方法区的数据。
```
若方法区内存空间不满足内存分配的请求时，将抛出OutOfMemoryError异常。
```

![Image text](http://121.43.100.98:8888/group1/M00/00/02/rBC9b2HB-DSALXgHAADMRCzQ-h0381.jpg)


# 类型信息
类型信息包括代码中的类名、修饰符、字段描述符和方法描述符。

# 字段描述符
字段描述符用于表示类、实例和局部变量。比如用L表示对象，用[表示数组等。

字段描述符内部解释表如下图所示。

|字段描述符|类型|含义| |-|-|-| |B|byte|有符号的字节型数| |C|char|unicode字符码点,UFT-16编码| |D|double|双精度浮点数| |F|float|单精度浮点数| |I|int|整型数| |J|long|长整数| |L className|reference|className的类的实例| |S|short|有符号短整数| |Z|boolean|布尔值true/false| |[|reference|一维数组|

#方法描述符
方法描述符表示0个或多个参数描述符以及1个返回值描述符，用于表示方法的签名信息。若返回值为void则用V表示。

方法描述符的格式： (参数描述符) + 返回值描述符。

比如Object m(int i, double d, Thread t)(){}方法可以表示为(IDLjava/lang/Thread;)Ljava/lang/Object;。

- I是int类型的字段描述符
- D是double类型的字段描述符
- Ljava/lang/Thread;是Thread类型的内部描述符
- Ljava/lang/Object;是方法的返回值为object类型

```
方法描述符分割各标识符的符号不用.，而用/表示。
```
```
public class SymbolTest{
    private final static String staticParameter = "1245";
    public static void main(String[] args) {
        String name = "jake";
        int age = 54;
        System.out.println(name);
        System.out.println(age);
    }
}
```
上面一个简单的例子，编译通过后，可以通过javap -s xxx.class命令查看内部签名。
```$xslt
D:\study\java\symbolreference\out\production\symbolreference>javap -s com.company.SymbolTest
Compiled from "SymbolTest.java"
public class com.company.SymbolTest {
  public com.company.SymbolTest();
    descriptor: ()V

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
}
```
可以看出无参构造函数的方法描述符为()V,main方法的方法描述符为([Ljava/lang/String;)V

# 运行时常量池
运行时常量池保存了编译期常量和运行期常量。编译期常量是在编译时编译器生成的字面量和符号引用。在类加载时，会把编译时的符号引用保存到符号表，字符串保存到字符串表，实际内容是保存到堆中。

![Image text](http://121.43.100.98:8888/group1/M00/00/02/rBC9b2HB-x-AUsaFAACDG7g2_bI464.jpg)

字面量指的是代码中直接写的字符串或数值等常量或声明为final的常量值。比如string str="abc"或int value = 1这里的abc和1都属于字面量。运行期常量值的是运行期产生的新的常量，比如String.intern()方法产生的字符串常量会被保存到运行时常量池缓存起来复用。

运行时常量在方法区中分配，在加载类和接口到虚拟机后就会创建对应的运行时常量。若创建运行时常量所需的内存空间超过了方法区所能提供的最大值，则会抛出OutOfMemoryError异常。

还是上面的代码示例，通过javap -v可以输出包括运行时常量的附加信息。下面列出了了部分常量输出内容。

```$xslt
D:\study\java\symbolreference\out\production\symbolreference>javap -v com.company.SymbolTest
...
Constant pool:
   #1 = Methodref          #7.#28         // java/lang/Object."<init>":()V
   #2 = String             #29            // jake
   #3 = Fieldref           #30.#31        // java/lang/System.out:Ljava/io/PrintStream;
  ...
   #7 = Class              #36            // java/lang/Object
   #8 = Utf8               staticParameter
   #9 = Utf8               Ljava/lang/String;
  #10 = Utf8               ConstantValue
  #11 = String             #37            // 1245
  #12 = Utf8               <init>
  #13 = Utf8               ()V
  ...
  #18 = Utf8               Lcom/company/SymbolTest;
  #19 = Utf8               main
  #20 = Utf8               ([Ljava/lang/String;)V
  #21 = Utf8               args
  #22 = Utf8               [Ljava/lang/String;
  #23 = Utf8               name
  #24 = Utf8               age
  #25 = Utf8               I
  #26 = Utf8               SourceFile
  #27 = Utf8               SymbolTest.java
  #28 = NameAndType        #12:#13        // "<init>":()V
  #29 = Utf8               jake
  ...
  #35 = Utf8               com/company/SymbolTest
  #36 = Utf8               java/lang/Object
  #37 = Utf8               1245
  ...
```
通过输出的静态常量信息可以很清楚的看出JVM编译时对字面量和符号引用的处理，包括类型名、变量名、方法等都用符号来代替了。比如第一个常量为对象类构造方法java/lang/Object."<init>":()V。去除其他不相关的常量，最终的符号引用和字面量关系如下表。

|索引|类型|值|
|-------|-------|-------|
|`0`|`Methodref`|`#7.#28(java/lang/Object."":()V)`|
|...|           |           |	
|`7`|	`Class` |	`#36`   |
|...|	        |	        |
|`12`|	`Utf8`  |	`<init>`|
|`13`|	`Utf8`  |	`()V`   |
|...|	        |           |
|`28`|`NameAndType`|`#12:#13("":()V)`|
|...|	        |           |
|`36`|	`Utf8`	|`java/lang/Object`|

Java虚拟机栈和本地方法栈
JVM规范要求JVM线程要同时具有本地方法栈和java虚拟机栈。JVM自身执行使用本地方法栈，而执行java方法使用java虚拟机栈。不过在hotspot实现中，本地方法栈和java虚拟机栈，共用同一块线程栈。

和程序计数器一样，每一个JAVA虚拟机线程都有自己私有的JAVA虚拟机栈。Java虚拟机规范允许Java虚拟机栈被实现为固定大小，也允许动态扩展和收缩。

```$xslt
当线程请求的栈深度大于虚拟机允许的栈深度，则会抛出StackOverflowError异常。当栈动态扩展无法申请到足够的内存时，则会排除OutOfMemoryError异常。
```

