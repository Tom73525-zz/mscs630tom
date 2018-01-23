/**
 * File: Driver.java
 * Author: Thompson Rajan
 * Course: MSCS 630
 * Assignment: Lab 1
 * Due Date: Wednesday, January 24, 2018
 * Version: 1.0
 *
 * This file contains the implementation of a pseudo-encryption.
 */

import java.util.Scanner;

/**
 * This class implements a pseudo-encryption. It takes in a plaintext file
 * and maps each character to the set of 0 - 25 numbers, irrespective of their
 * cases. All spaces are mapped to 26.
 */

public class Driver {

  /**
   * This method converts a string into an array of integers which are mapped
   * from (A - Z) or (a - z) to (0 - 25) with spaces mapped to 26.
   * @param s Input string
   * @return returns an array of integers mapped from character strings.
   */
  public static int[] str2int(String s) {

    int[] e = new int[s.length()];
    for (int i = 0; i < s.length(); i++) {

      s = s.toUpperCase();
      char c = s.charAt(i);

      if (c != 32) {
        c -= 65;
        e[i] = (int) c;
      } else
        e[i] = 26;
    }
    return e;
  }

  public static void main(String[] args) {

    Scanner in = new Scanner(System.in);

    while(in.hasNext()){

      String s = in.nextLine();

      int[] d = str2int(s);

      for(int i : d){
        System.out.print(i + " ");
      }
      System.out.println();
    }
  }
}
