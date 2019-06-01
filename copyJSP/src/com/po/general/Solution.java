package com.po.general;

import java.util.Comparator;

public abstract class Solution {
    public double fitnessValue;
    public abstract boolean betterThan(Solution so1,Solution so2);
    public static void Qsort(Solution[] solutions){
        Qsort(solutions,0,solutions.length);
    }
    public static void Qsort(Solution [] solutions,int begin,int end){
        if(begin>=end) return;
        Solution key=solutions[begin];
        int i=begin;
        int j=end-1;
        while(j>i){
            while(j>i&&solutions[i].betterThan(key,solutions[j]))j--;
            solutions[i]=solutions[j];
            while(j>i&&solutions[i].betterThan(solutions[i],key))i++;
            solutions[j]=solutions[i];
        }
        solutions[i]=key;
        Qsort(solutions,begin,i);
        Qsort(solutions,i+1,end);
    }
}
