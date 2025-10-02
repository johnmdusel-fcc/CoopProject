# Coop Project for CMSC 131

This project is a simplified version of an app to help manage a community tool/vehicle sharing co-op. In the real world, this would be part of the backend of a SaaS app such as [Lindrs](https://www.lindrs.com/) or [Getaround](https://getaround.com/). The DC Department of Parks and Recreation also has a [tool sharing program](https://dpr.dc.gov/service/garden-tool-share-program). See also this overview in [Mother Eath News](https://www.motherearthnews.com/sustainable-living/nature-and-environment/forming-community-tool-co-op-zmaz04amzsel/).

This writeup follows Southard's instructions from the Course Notes.

# 0. Project Concept and Design

About the goal of this project:

> Create a software package that will run the basic operations of a community vehicle/tool sharing program: the list of rental items available for rental with their current availability statuses, checkouts and returns, *something analogous to interest payments and fees*, keeping an audit trail of all activity.

## 0.1 Initial Project Design

- Our co-op will be organized by rental item. Each rental item has a UID (unique identifier, an alphanumeric string). It also has an owner (a person). The same person can have multiple rental items.

- There will be tools and vehicles (collectively called rental items) available for rental. The list of rental items and their starting statuses will be kept in one file. The program will read that file at startup.

    - Vehicles allow only a limited number of checkouts in a given time. 
 
    - Tools have no limit on checkouts. *Analogue of service fee?*
 
    - It's possible for an rental item to become damaged while being used. If that happens, the rental item needs maintenance and becomes unavailable. Each rental item has a fixed maintenance price. An rental item becomes unavailable once it's damaged and stays unavailable for the rest of the month.

- A list of transactions (checkout, return) will be kept in a second file, in the order of occurrence. Each file will represent one month's transactions. The program will read these and operate on rental items accordingly.

- Transactions initial include rentals and returns. In time we may add others, such as maintenance.

    - *N.B. Dusel proposes "rent/return" for Co-op and "checkin/checkout" for rental item, analogous to "withdraw/deposit" for Bank and "debit/credit" for Account. He's open to other schemes though.*

    - Rent means the availability status changes from `true` to `false`.

    - Return means that the availability status changes from `false` to `true`.
 
- At the end of each month, the program will calculate the total maintenance cost for all damaged rental items. This cost will be based on the state of the rental item at the end of the month and its maintenance price. 

- At the end of each month, the program will write out a new copy of the rental items file, showing availability states at that time.

- The program will keep an audit (log) file which includes all valid transactions, as well as errors that occur.

    - One type of error we must watch for is an attempt to checkout an rental item that's unavailable.
 
    - We also must keep track of rental items entering a damaged state.
 
 
 ## 0.2 Initial Object Design
 
 This is intended to be an in-class activity. It's best to do with a small team and a whiteboard. It's good to have a team because people have different points of view and can contribute different ideas.
 
 Review the **Initial Project Design** and make up a lost of all the objects --- distinct entities --- which you see. For each one, list any attributes and/or behaviors you think they would have in the context of this system. Note down any *has-a* relationships: situations in which one object is part of the implementation of another.
 
 > For example: The fundamental object is a Co-op. A Co-op *HAS* rental items. Each rental item *has* an ID, and so on. Make sure each of the objects you define is whole and complete in itself, and separate from the other entities.
 
In the separate phases of this assignment we are going to get specific about some of the objects. We'll ask you to implement them. But the thinking you do up front at this stage will inform that work. *You might think of improvements to how we have broken down and specified the objects: talk to your instructor!*

This project is broken into multiple phases. Each phase is small and has a definite goal. You'll see how a sizable project can be evolved one phase at a time. In some cases, code you write in an earlier phase will get replaced by something from a later phase. Other times, you'll have to live with your code from a previous phase. This is typical of how real software development proceeds.


# 1. Phase 1: Co-op and rental item Classes


## 1.1. Rental Item class

This class is the foundation for all of our work.
Pretty much everything our program does will involve operations on one or more accounts. 

### rental item class data 

The data for the Rental Item class --- in this phase --- will comprise the following:

- The rental item ID (String). Alphanumeric. Every rental item has a unique ID (UID) that distinguishes it from all other rental items in the same Co-op.

- The name of the owner (String). This is not necessarily unique: the same person might have multiple rental items.

- The current availability/maintenance state, represented as a enum.

- The rental item type, tool or vehicle, represented as an enum. 

### Rental Item class constructor

The constructor arguments with be the rental item ID, rental item owner, and rental item type. The availability/maintenance state defaults to being available.

### Rental Item class methods

The Rental Item class will need accessor methods for:

- the rental item ID

- the name of the rental item's owner

- the current state of the rental item

These will be used in later phases to process transactions. Notice that we have not yet provided any method to change the rental item state. That'll come later as well.

## 1.2 Co-op class

The Co-op class holds the collection of all the rental items in the Co-op. It provides methods to read the various data files and perform the operation of a co-op. Most of these methods will in turn create other objects and call their methods.

### Co-op class data

The data for the Co-op class will be an array of Rental Item objects.

### Constructor: `Coop()`

This will initialize the Rental Items array. It will not fill it; that will be done in the `loadRental Items` method.

### Co-op class methods

- `addRentalItem`: Adds a new Rental Item object to its array.

- `find`: Find an Rental Item object in its rental items array using the UID, and return the reference to that rental item. This can be done using linear search.

- `getInventoryCount`: Returns the number of rental items.

- Any others that you need!


## 1.3 Test coverage

You must write test coverage for all of your code.
Remember the key points about designing unit tests. 
For example, you can create a Co-op object, add some rental items, and then verify that the count is correct. You would also want to test that you can find one of the rental items. 

*Test coverage is a requirement for this project.* 
Any code lacking test coverage is considered broken, and is worth zero points.



# 2. Phase 2: Saving and Loading the Co-op Data

In this phase of the project we'll add the functionality of loading a list of rental items from a data file.
We'll also add functionality for saving out our list of rental items.
For now, all we're doing is loading a file and then writing the rental items back out.

## 2.1 Co-op Class methods

- `loadRentalItems(String filename)`: This method will read the specified rental items file and fill in the Co-op's array of rental items with `RentalItem` objects.

- `writeRentalItems(String filename)`: This method will write the contents of the Co-op's array of `RentalItem`s to a specified file.

## 2.2 Rental Item class Factory Method

The argument of the factory method will be one line of data from the CV file, pased as a single string. **You can assume there are no syntax errors in the accounts file. ** (In practice, the CSV file would be written by a piece of software, not input by a human.)
For example:
```
"tool,xf123456,Lorenzo"
```
The factor method needs to break this into tokens and then assign the individial values into attributes of the `RentalItem` class.

- You can split the line into tokens using the call `split(",")`.

- You can convert a text string to a `RentalItemType` using the `valueOf` method (it's inherited by any `Enum`), just be sure that the string you pass is compatible with the enum's values. For example, `RentalItemType.valueOf("tool")` will throw but `RentalItemType.valueOf("TOOL")` will return the value `RentalItemType.TOOL`.

## 2.3 Writing the Rental Items File

In general, we can write lines to a CSV file (or any other text file) using a `FileWriter` object. The skeleton of such a routine looks like this:

```
boolean writeRentalItems(String filename) {
    boolean result = true;
    File file = new File(filename);
    FileWiter writer = null;
    try {
        // code here to get each rental item and write its line
        writer.close();
    } catch(IOException e) {
        e.printStackTrace();
        result = false;
    }
    return result;
}
```

This function would go in the `Coop` class. The individual `RentalItem` objects will provide the lines of data to write, one per rental item. To do this you'll want to add a method called `toCSV()` to the `RentalItem` class. Its job is to make the line of the CSV file for that account, and return it as a `String` to be written to the file. This respects *encapsulation*: the `Coop` class manages the overall job, but the `RentalItem` class decides how each rental item will get converted to a string.

## 2.4 Test Coverage

As part of this assignment, you need to write unit tests for your load and save methods. As with Phase 1, this is a required part of the project. It's not an add-on; code lacking test coverage will be considered broken.

One way to test file load is to load a known file and then verify that the count is as expected and that you can find certain rental items you know are in the file.

File save can be more ambitious. The thing to notice is that once we have tested the file load, we can use the file load function to test the file save. Here's an approach:
1 (testing file save). Make some rental items and save them to a file.
2 (already tested, so OK). Load the file back into a new `Coop`. 
3. Verify that the new `Coop` truly contains those rental items.

# 3. Phase 3: Transactions

In this phase we will add the functionality of loading a list of transactions from file, and processing them.

## 3.1 Transaction Class Hierarchy

You will need `Transaction` and `Checkout` and `Return` transaction classes (derived classes) as discussed in section 6.3 of the Course Notes.

## 3.2 `main()` function

Our `main()` function will get a new line, to load and process the transactions file. Note that with this change the `rentalitems.end.txt` file will no longer be identical to `rentalitems.txt`; it will reflect the effects of the transactions.

Your program might be asked to provess any number of transactions files, one at a time, after having loaded the rental items. Since each transactions file represents one month, yo might have `transactions.jan.txt`, `transactions.feb.txt`, and so on.

```
public static void main(String[] args) {
    Coop coop = new Coop();
    coop.loadRentalItems("rentalitems.txt");
    coop.processTransactions("transactions.txt");
    coop.writeRentalItems("rentalitems.end.txt");
}
```

## 3.3 The Transactions File

Each transactions file contains one month's worth of transactions, in CSV format. Each line represents one transaction. Here are sample lines:

> `checkout,xf123456`

> `return,xf123456`

Note that the rental items are referred to by their unique ID. The item ID `xf123456` is a tool belonving to Lorenzo, from the rental items example. So here Lorenzo's tool has been checked out and then returned.

You can assume that there are no syntax errors in the rental items file. There may, however, be logical errors, such as rental items that don't exist, checkouts that are not allowed, and so on.

## 3.4 Co-op class: `processTransactions(String filename)`

This method will read the specified transactions file. For each line of the file, it will call a factory method in the `Transaction` class to make a `Transaction` object, and execute the transaction. This method should never need to know what is the exact type of each transaction object, nor what the execute methods are doing. Notionally, the routine should go like this:

```
Transaction trs = Transaction.fromCSV(csvLine);
if (trs != null) {
    idx itmIdx = findItem(itm.getUID());
    if (itmIdx >= 0) {
        RentalItem itm = rentalItems[itmIdx];
        // conditionals checking rental item state vs trs type
    } else {
        // error condition: rental item not found
    }
}
```

The `execute` method will in tern invoke the `checkout` or `return` method of the `RentalItem` class, discussed below.

When we get to test coverage, we'll see that there might be a way to improve on this and make the code more testable (and also more general).

### Transaction class factory method

This method will work much the same as the `RentalItem` factory method. It will use the first token on the line to determind what type of transaction to make. it will pass the other tokens (converted to the appropriate type) to the constructors for the individual transactions.

## 3.5 Co-op class: `find` method

The `Coop` class will need to use its `find` method to return a reference to a rental item, so that a transaction can operate on it.

## 3.6 Rental Item class: `checkout` and `checkin` methods

The transaction objects need to change the state of the rental items. Your first thought might be to have methods called `getAvailability()` and `setAvailability()`, but that is not the best way. Encapsulation dictates that changing an object's internal data must be done by the object. There are some error conditions that can occur here and we need to think about them. For example, what if we're asked to checkout an item that's unavailable or damaged? Accordingly, we'll give the rental item class two new methods: one to rent it and another to return it. It's a good idea to use names that originate in the problem doman, so good names for these methods would be `checkOut` (to rent) and `checkIn` (to return).

There will be no argument for either method (unlike the bank project's `credit` and `debit` functions). What error/consistency checks do we need to do? For example, what do you think the `checkOut` method should do if asked to check out an item that is unavailable or damaged?


### 3.6.1 Modeling damage (*Coop project -ism*)

When an item's `checkIn` method is called, its availability state will change from unavailable to either available or damaged.The `checkIn` comes with a probability that the item was damaged during its rental period:

- The probability that a tool is damanged during a rental period is 0.05.

- The probability that a vehicle is damaged during a rental period is 0.01.

When `checkIn` is called, a number between 0 and 1 is chosen uniformly at random. The rental item's availability state becomes available or damaged based on that number's value.


## 3.7 Error conditions

The following error conditions must be checked for:

- Rental item not found

- Incompatibility of rental item availablity state. *I.e.*, an attempt to check out an item that is unavailable or damaged.

| Rental Item State | Attempt to checkOut (rent) | Attempt to checkIn (return) |
|-------------------|---------------------------|-----------------------------|
| AVAILABLE         | ok                        | error                       |
| UNAVAILABLE       | error                     | ok                          |
| DAMAGED           | error                     | error                       |

For now, you can just output to the console when an error occurs. See, for example, the error handling you wrote for the `Coop` class `loadRentalItems` method.


## 3.8 Test Coverage

You will want simple tests for the `checkOut` and `checkIn` methods. You'll also want a test that builds on these for the `processTransactions` method. To make your code more testable, consider modifying the structure outlined earlier to use an array of `Transaction` objcts. The array could be constructed while reading the file, and then passed to the method which executes all the transactions. Each of these two methods would then be individually simpler and more easily testable.

# 4. Phase 4: Auditing

## 4.1 Audit class

## 4.2 Audit file


# 5. Phase 5: Maintenance Costs 