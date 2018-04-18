/**
 * File: DriverAES.java
 * Author: Thompson Rajan
 * Course: MSCS 630 Security Algorithms and Protocols
 * Assignment: Lab 5
 * Due Date: Wednesday, April 18, 2018
 * Version: 1.0
 *
 * This file contains a class that calls the AESCipher to generate the
 * ciphertext.
 */

import java.util.Scanner;

/**
 * This class calls the AESCipher to generate the ciphertext.
 */
public class DriverAES {
  /**
   * This method makes a static call to AES to get the ciphertext for a given
   * plaintext and a key.
   * @param args - null
   */
  public static void main(String[] args) {

    Scanner in = new Scanner(System.in);

    String keyHex = in.nextLine();
    String pTextHex = in.nextLine();

    // Get Ciphertext
    String cTextHex = AESCipher.AES(pTextHex,keyHex);

    System.out.println(cTextHex);
  }
}
