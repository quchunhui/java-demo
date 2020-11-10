###名称
模板模式

###角色
- AbstractClass 抽象类
负责实现模板方法，还负责生命在模板方法中所使用到的抽象方法。
这些抽象方法由子类ConcreteClass角色负责实现。

- ConcreteClass 具体类
改角色负责具体实现AbstractClass角色的抽象方法。
这里实现的方法将会在AbstractClass角色的模板中被调用。

###优点
由于在父类的模板方法中编写了方法，因此无需在每个子类中再编写方法