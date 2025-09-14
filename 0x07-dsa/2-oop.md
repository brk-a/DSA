# OOP
* object-oriented programming
* everything in python is an object
    - objects are represented as a **class**
     - a class is to OOP what a blueprint is to a builder
    - all data types in python are instances of a class
        - strings are instances of class `str`, ints are instances of class `int`, lists are instances of class `list` and so on
    - one can make their own data types and objects by creating a class using the `class` keyword

        ```python
            class User:
                pass
        ```

    - instances of said class are created *viz*

        ```python
            user_1 = User()
            user_2 = User()
        ```
    
    - `user_1` and `user_2` are instances of the same class, that different objects that have the blueprint of `User`
## methods, attributes, properties, constructors and decorators
* **method** &rarr; a function that resides/defined in a class
    - describes the behaviour or actions that an object created from the class can perform
    - operate on the attributes of the object and can access or modify the object's state
* **attribute** &rarr; variable that holds data or state about an object or class
    - represents the properties or characteristics of the object
    - can be instance-specific (unique to each object) or class-level (shared among all instances)
* **property** &rarr; a special kind of attribute that allows custom behaviour when getting, setting or deleting a value
    - defined using the built-in `property()` function or the `@property` decorator, enabling methods to be accessed like attributes while controlling access witx`h getter, setter and deleter functions
* **constructor** &rarr; a special method automatically called when a new instance of a class is created
    - usually defined by the `__init__()` method
    -  purpose of the constructor is to initialise the instance's attributes

        ```python
            class User:
                def __init__(self, first_name):
                    self.first_name = first_name
        ```

    - `self` refers to the instance of the class itself
        - allows access to instance attributes and methods within class methods
        - passed implicitly as the first parameter in instance methods
        - is a convention (not a Python keyword) but should always be used for clarity
    - `__init__` is an example of a **dunder method**
        - dunder dethods (Double Underscore Methods), also known as magic methods or special methods, have names with double underscores (`__init__`, `__str__`, `__repr__` etc.)
        - allow customising behaviour of objects (e.g., initialisation, string representation, addition, etc)
        - `__init__`: constructor
        - `__str__`: string representation for users
        - `__repr__`: developer-facing string representation
    - `cls` refers to the class itself rather than an instance
        - used in class methods which affect the class as a whole rather than individual instances
        - class methods are defined with the `@classmethod` decorator and take `cls` as the first parameter
* **decorator** &rarr; a function that modify or enhance other functions or methods
    - the `@` symbol is typically the first character of a decorator
    - placed right above the function/method to modify/enhance
    - examples of decorators that alter method behaviour: `@staticmethod`, `@classmethod` and `@property` 
    - `@staticmethod` makes a method not take `self` or `cls`; it behaves like a regular function within the class namespace
    - `@classmethod` makes a method take `cls` as the first argument and can modify class state
    - `@property` turns a method into a managed attribute, allowing control over getting, setting and deleting a value with method calls but attribute-like syntax
* example

    ```python
        class User:
            # Constructor: called when creating an instance, initialises the first_name attribute
            def __init__(self, first_name):
                self.first_name = first_name
            
            # Instance method: operates on an instance, uses 'self'
            def greet(self):
                return f"Hello, {self.first_name}!"
            
            # Class method: operates on the class itself, uses 'cls'
            @classmethod
            def class_info(cls):
                return f"This is the {cls.__name__} class."
            
            # Static method: does not use self or cls, utility method within class namespace
            @staticmethod
            def static_welcome():
                return "Welcome to the User class!"
            
            # Property: managed attribute allowing controlled access to first_name
            @property
            def name(self):
                return self.first_name.title()
            
            # Dunder (magic) method: custom string representation of an instance
            def __str__(self):
                return f"User: {self.name}"
    ```

* usage

    ```python
        user = User("goat matata")
        print(user.greet())              # Hello, goat matata!
        print(User.class_info())         # This is the User class.
        print(User.static_welcome())     # Welcome to the User class!
        print(user.name)                 # Goat Matata (property capitalises the name)
        print(str(user))                 # User: goat matata (dunder method overriding string output)
    ```