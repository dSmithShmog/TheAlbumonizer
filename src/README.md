# TheAlbumonizer
A simple JavaFX application that takes an `integer` as user input, uses that as part of a string query, 
and displays placeholder information from photos inside a given album at [https://jsonplaceholder.typicode.com/photos](https://jsonplaceholder.typicode.com/photos)

The app displays *ID*, *Title*, and *URLs* by default with the *URLs* being toggleable. 

There is a runnable `.jar` file included in the \src\artifacts\ directory. I have yet to figure out how to have intellij simply include the \out\ directory in version control. This was built with Java8, JavaFX 2.\_, inside the IntelliJ IDE, and has been tested on Windows10.

### Future Works / Updates
1. Improve the overall structure/refactor main source file. This is mostly a symptom of my lack of experience of JavaFX (having never used it before)
2. Make Hyperlinks actually open external window on click
3. Make it less hideous. Although I do love the color purple.
4. Add additional user control in the *left* and *right* areas of the `BorderPane`. For example, the ability to sort alphabetically or other filtering options.



