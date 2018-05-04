/**
 * File: DriverAES.java
 * Author: Thompson Rajan
 * Course: MSCS 630 Security Algorithms and Protocols
 * Assignment: Lab 4
 * Due Date: Wednesday, April 4, 2018
 * Version: 1.0
 *
 * This file contains a class that calls the AESCipher to generate secure keys.
 */

import java.util.Scanner;

/**
 * This class calls the AESCipher class to generate round keys for a given key.
 */
public class DriverAES {
  /**
   * This method makes a static call to aesRoundKeys to get the round keys.
   * @param args - null
   */
  public static void main(String[] args) {

    Scanner in = new Scanner(System.in);

    String keyHex = in.next();

    // Get round keys
    String[] roundKeys = AESCipher.aesRoundKeys(keyHex);
    for(int i = 0; i < roundKeys.length; i++){
      System.out.println(roundKeys[i]);
    }
  }
}
