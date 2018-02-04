/**
 * File: Euclidean.java
 * Author: Thompson Rajan
 * Course: MSCS 630 Security Algorithms and Protocols
 * Assignment: Lab 2
 * Due Date: Wednesday, February 7, 2018
 * Version: 1.0
 *
 * This file contains the implementation of the Euclidean Algorithm.
 */

import java.util.Scanner;

/**
 * This class implements the Euclidean Algorithm to find the greatest common
 * divisor of given two numbers.
 */
public class Euclidean {

  /**
   * This method takes two long inputs and return their gcd.
   * @param a - long input
   * @param d - long input
   * @return - returns gcd of a and d, gcd(a,d).
   */
  static long euclidAlg(long a, long d){

    //Get quotient
    long q = Math.floorDiv(a, d);

    //Get remainder
    long r = a - d * q;

    //Call euclidAlg() recursively when remainder is not 0 .
    if(r != 0)
      return euclidAlg(d, r);

    return d;
  }

  public static void main(String[] args) {

    Scanner input = new Scanner(System.in);

    //Get input values
    long a = input.nextLong();
    long d = input.nextLong();

    //Get gcd(a,b)
    long r = euclidAlg(a, d);

    System.out.println(r);
  }
}
