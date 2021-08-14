This project was designed with the intent of tracking various prices within the Stock Market for any Company.

The program uses a GridPane that holds an enter button, two text field nodes and a Label. The program prompts a user to 
enter information ( a stock ticker & monthly span ) which is then used to
search an online API of monthly open, close, high and low stock prices. If the user does not enter a ticker symbol
and a monthly span, the program prompts the user to try "IBM" and "12" so the program can run.

There are two classes in the program: the Main class and the KeyController class. Within the Main class is an inner class 
called StockReader that takes the user input and stores the information from the API within a JsonObject. The dates or "keys"
are converted into LocalDate objects so that they can be sorted by the most recent month and year. This is done using the grabKeys()
and sortKeys() methods. This is done within the KeyController class because when the dates are extracted from the JsonObject
they are not in order.

After storing the information within the KeyController object, the KeyController class then processes the information in the document by grabbing the 
months of the company and then storing the information of the open, high, low and close prices in variables.
To get this information the KeyController class uses the grabOpen... methods. It stores the information into an array of Doubles and
within the Main class, this information is added to a LineChart as a series of x and y values.

Than main class of the program is the Main class. It holds the launch method and the class that processes the API.