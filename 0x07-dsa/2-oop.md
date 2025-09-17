# Object-Oriented Programming (OOP) in Python

---

## WTF is OOP?

* **Object-oriented programming** is a paradigm centred around objects rather than actions.
* Everything in Python is an object.

  * Objects are instances of **classes**.
  * A class is to OOP what a blueprint is to a builder.
  * All built-in data types in Python are instances of classes: strings (`str`), integers (`int`), lists (`list`), etc.
* You can create your own data types by defining classes using the `class` keyword.

```python
class User:
    pass
```

* Instances (objects) are created from a class:

```python
user_1 = User()
user_2 = User()
```

`user_1` and `user_2` are distinct instances but share the `User` blueprint.

---

## Methods, Attributes, Properties, Constructors and Decorators

### Method

* A function defined inside a class.
* Describes actions the object can perform.
* Can access or modify the object’s attributes.

### Attribute

* A variable that holds data about an object or class.
* Can be instance-specific (unique) or class-level (shared).

### Property

* Special kind of attribute controlling access with getter, setter, and deleter methods.
* Defined with `property()` or the `@property` decorator.
* Enables attribute-like access to methods with validation or processing.

### Constructor

* A special method called when an object is created.
* Usually `__init__()` in Python.
* Used to initialise attributes.

```python
class User:
    def __init__(self, first_name):
        self.first_name = first_name
```

* `self` refers to the current instance.
* `cls` refers to the class itself (used in class methods).

### Decorator

* A function modifying/enhancing other functions or methods.
* Uses `@` syntax above method definitions.
* Common decorators in classes:

  * `@staticmethod` — method does not take `self` or `cls`.
  * `@classmethod` — method takes `cls`.
  * `@property` — turns a method into a managed attribute.

---

### Example

```python
class User:
    def __init__(self, first_name):
        self.first_name = first_name

    def greet(self):
        return f"Hello, {self.first_name}!"

    @classmethod
    def class_info(cls):
        return f"This is the {cls.__name__} class."

    @staticmethod
    def static_welcome():
        return "Welcome to the User class!"

    @property
    def name(self):
        return self.first_name.title()

    def __str__(self):
        return f"User: {self.name}"
```

Usage:

```python
user = User("goat matata")
print(user.greet())              # Hello, goat matata!
print(User.class_info())         # This is the User class.
print(User.static_welcome())     # Welcome to the User class!
print(user.name)                 # Goat Matata
print(str(user))                 # User: Goat Matata
```

---

## Encapsulation

### Summary

Encapsulation restricts direct access to internal attributes, protecting an object’s state.

### Explanation

* Prefix attributes with `_` (protected by convention) or `__` (private with name mangling).
* Use properties (`@property`) to manage access with validation.

### Code

```python
class User:
    def __init__(self, name, age):
        self._name = name
        self.__age = age

    @property
    def age(self):
        return self.__age

    @age.setter
    def age(self, value):
        if value < 0:
            raise ValueError("Age cannot be negative")
        self.__age = value
```

---

## Inheritance

### Summary

Inheritance allows a class to inherit attributes and methods from a parent class.

### Explanation

* Reuse or extend functionality.
* Use `super()` to call parent methods.

### Code

```python
class Animal:
    def speak(self):
        return "Some generic sound"

class Dog(Animal):
    def speak(self):
        return "Woof!"
```

---

## Polymorphism

### Summary

Different classes can be used interchangeably if they implement the same methods.

### Explanation

* Methods with the same name behave differently depending on the object.

### Code

```python
def animal_speak(animal):
    print(animal.speak())

animal_speak(Dog())    # Woof!
animal_speak(Animal()) # Some generic sound
```

---

## Composition vs Inheritance

### Summary

* **Inheritance**: “is-a” relationship (Dog is an Animal).
* **Composition**: “has-a” relationship (Car has an Engine).

### Code

```python
class Engine:
    def start(self):
        return "Engine started"

class Car:
    def __init__(self):
        self.engine = Engine()

    def start_car(self):
        return self.engine.start()
```

---

## Class vs Instance Variables

### Summary

* Instance variables belong to individual objects.
* Class variables are shared by all instances.

### Code

```python
class User:
    user_count = 0

    def __init__(self, name):
        self.name = name
        User.user_count += 1
```

---

## More Magic (Dunder) Methods

### Summary

Special methods customise object behaviour for Python’s built-in functions and operators.

### Code

```python
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __len__(self):
        return int((self.x ** 2 + self.y ** 2) ** 0.5)

    def __repr__(self):
        return f"Vector({self.x}, {self.y})"
```

---

## Abstraction (Intro)

### Summary

Hiding implementation details while exposing a simple interface.

### Explanation

* Abstract base classes (ABCs) with the `abc` module.
* Abstract methods enforce implementation in subclasses.

### Code

```python
from abc import ABC, abstractmethod

class Shape(ABC):
    @abstractmethod
    def area(self):
        pass

class Circle(Shape):
    def __init__(self, radius):
        self.radius = radius

    def area(self):
        return 3.1415 * self.radius ** 2
```

---

## Common Use-Cases

* Modelling real-world objects.
* GUI elements.
* Games.
* Data models and business logic.

---
