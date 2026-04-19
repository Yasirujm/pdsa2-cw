package com.pdsa.backend.minimumcost.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class CostMatrixGeneratorService {
    public int[][] generateMatrix(int n) {
        Random random = new Random();
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = 20 + random.nextInt(181);
            }
        }
        return matrix;
    }
}