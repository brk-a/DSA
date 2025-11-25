# best time to buy stocks problem

## problem
* you are given an array, `prices`, such that `prices[i]` is the price of a given
stock on the i<sup>th</sup> day
* you want to maximise your profit by choosing a single day to buy a stock and choosing
a **different day in the future** to sell said stock
* return the max profit you can achieve from this transaction; return `0` if you cannot
achieve a profit

## assumptions
* *nada*

## examples
### example 1:

```plaintext
    input: prices = [7, 1, 5, 3, 6, 4]
    output: 5
    explanation: buy on day 2 (price = 1) and sell on day 5 (price = 6) so that
        profit will be 5 (6 - 1)
    note: you cannot buy on day 2 and sell on day one because commom sense (one must
        buy before they sell)
```

### example 2:

```plaintext
    input: prices = [7, 6, 4, 3, 1]
    output: 0
    explanation: no transactions executed, therefore, profit = zero
```

## constraints
* 2 &le; prices.length &le; 10<sup>4</sup>
* -10<sup>9</sup> &le; prices[i] &le; 10<sup>9</sup>

## approach(es)
### approach 1: brute force O(N<sup>2</sup>) time
* have a variable for profit; initialise it to zero
* have a pointer to `prices[0]`
* for each element starting at `prices[1]` check if the difference between `price[i]` and the value of the first pointer is greater than the current value of profit
    * if yes, set profit to said value, else, carry on
* move the pointer one step forward and repeat the previous step until the pointer gets to the end of the array
* return the value of profit
### approach 2: one pass O(N) time
* have two variables
    * one for profit, initialised to zero
    * one for minimum price, initialised to `MaxInt32`
* for each element starting at `prices[0]`
    * check if `prices[i]` is greater than minimum price
        * if yes, continue, else, set minimum price to `prices[i]`
    * check if the difference between `prices[i]` and minimum price is greater than profit
        if yes, set profit to the difference between `prices[i]` and minimum price
* return the value of profit