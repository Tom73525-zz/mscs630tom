/**
 * File: Determinant.java
 * Author: Thompson Rajan
 * Course: MSCS 630 Security Algorithms and Protocols
 * Assignment: Lab 3
 * Due Date: Wednesday, February 28, 2018
 * Version: 1.0
 *
 * This file contains the implementation to estimate the determinant of a
 * matrix in modulo m
 */

import java.util.Scanner;

/**
 * This class estimates the determinant of an n x n matrix in modulo m
 */
public class Determinant {

  /**
   * This method calculates the determinant of a matrix recursively in modulo m.
   * @param m - Modulo input
   * @param A - Given matrix
   * @return - Determinant of matrix A in modulo m
   */
  public static int cofModDet(int m, int[][] A){
    int det = 0;
    int n = A.length;
    int sign = 1;
    int p = 0;
    int q = 0;

    // Return first element in mod m if the matrix size is 1
    if(n == 1){
      det = A[0][0] % m;
    }
    else{

      // Initialize sub-matrix to store co-factors.
      int b[][] = new int[n-1][n-1];
      for(int x = 0 ; x < n ; x++){

        // Indices to pick co-factor elements
        p=0;
        q=0;

        for(int i = 1; i < n; i++){
          for(int j = 0; j < n; j++){
            if(j != x){

              // Pick co-factor for corresponding element
              b[p][q++] = A[i][j] % m;
              if(q % (n-1) == 0){

                // Reset column when at last column of the current row &
                // move to next row
                p++;
                q=0;
              }
            }
          }
        }
        // Estimate determinant from co-factors
        det = det + A[0][x] * cofModDet(m,b) * sign;

        // Flip signs
        sign = sign * (-1);
      }
    }
    return det % m;
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    // Get modulo m and matrix size n
    int m = in.nextInt();
    int n = in.nextInt();
    int A[][] = new int[n][n];

    // Get matrix input
    for(int i= 0; i < n ; i++) {
      for (int j = 0; j < n; j++) {
        A[i][j] = in.nextInt() % m;
      }
    }
    System.out.println(cofModDet(m, A));
  }
}
