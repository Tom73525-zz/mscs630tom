/**
 * File: HexMatP.java
 * Author: Thompson Rajan
 * Course: MSCS 630 Security Algorithms and Protocols
 * Assignment: Lab 3
 * Due Date: Wednesday, February 28, 2018
 * Version: 1.0
 *
 * This file contains the implementation to convert a plaintext to a
 * padded hex-matrix
 */

import java.util.Scanner;

/**
 * This class is used to convert a plaintext to hex matrix padded with a give
 * character s
 */
public class HexMatP {

  /**
   * This method takes a padding a character and a string representing a
   * plaintext and returns a padded hex-matrix.
   * @param s - Padding character
   * @param p - Plaintext string
   * @return - Returns padded hex - matrix
   */
  static int [][] getHexMatP(char s, String p){

    char m[] = p.toCharArray();

    // Initialize temporary 4 x 4 matrix to collect values in 4 x 4 order.
    int t[][] = new int[4][4];

    // This matrix is used to collect ordered values from t[][].
    int mat[][] = new int[(m.length / 16 + 1) * 4][4];

    // Indices for finalized matrix mat[][]
    int x = 0;
    int y = 0;


    System.out.println();

    // Trace current character
    int i = 0;

    while (i < m.length) {
      for (int j = 0; j < 4; j++) {
        for (int k = 0; k < 4; k++) {

          // Store ASCII if less than plaintext length.
          if (i < m.length) {
            t[j][k] = m[i];
            i++;
          }
          // Pad if greater than plaintext length.
          else {
            t[j][k] = s;
            i++;
          }
        }
      }

      // Store transposed matrix to the final matrix.
      for(int a = 0; a < 4; a++){
        for(int b = 0; b < 4; b++){
          mat[x][y++] = t[b][a];
          if(y == 4)
            y = 0;
        }
        x++;
      }
    }
    return mat;
  }

  public static void main(String[] args) {

    // Get padding character and plaintext.
    Scanner in = new Scanner(System.in);
    char s = in.nextLine().charAt(0);
    String p = in.nextLine();

    // Get hex-matrix
    int mat[][] = getHexMatP(s,p);

    for(int i=0; i < mat.length;i++){
      for(int j= 0 ; j< mat[0].length; j++){
        System.out.print(Integer.toHexString(mat[i][j]).toUpperCase() + " ");
      }
      System.out.println();
      if((i+1) % 4 == 0 ){
        System.out.println();
      }
    }
  }
}
