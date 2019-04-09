package com.po;

import com.po.general.Func;

public interface Parameter {
    public static double alpha=0.5;
    public static double pbxPossibility=0.5;
    public static double opxPossibility=0.5;
    public static double csonRatio=0.5;
    public static double msonRatio=0.5;
    public static int numOfIteration=200000;
    public static int numOfStay=1;
    public static Func func=(x)->{return Code.decode(x);};
}
