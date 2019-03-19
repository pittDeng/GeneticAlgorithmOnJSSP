package com.po;

public abstract class Function {
    public static int minOfArray(int [] arr){
        int min=arr[0];
        for(int i=1;i<arr.length;++i){
            if(arr[i]<min)min=arr[i];
        }
        return min;
    }
    public static int maxOfArray(int [] arr){
        int max=arr[0];
        for(int i=1;i<arr.length;++i){
            if(arr[i]>max)max=arr[i];
        }
        return max;
    }
    //求Solution [] pop中judge值的和
    public static double sumOfJudge(Solution [] pop){
        double res=0;
        for(int i=0;i<pop.length;++i){
            res+=pop[i].judge;
        }
        return res;
    }
    //按judge从大到小排列
    public static void qsort(Solution [] pop){
        Qsort(pop,0,pop.length);
    }
    private static void Qsort(Solution [] pop,int begin,int end){
        if(begin>=end) return;
        Solution key=pop[begin];
        int i=begin;
        int j=end-1;
        while(j>i){
            while(j>i&&pop[j].judge<=key.judge)j--;
            pop[i]=pop[j];
            while(j>i&&pop[i].judge>=key.judge)i++;
            pop[j]=pop[i];
        }
        pop[i]=key;
        Qsort(pop,begin,i);
        Qsort(pop,i+1,end);
    }

    //按时间从小到大排列
    public static void qsortTime(Solution [] pop){
        QsortTime(pop,0,pop.length);
    }
    public static void QsortTime(Solution [] pop,int begin,int end){
        if(begin>=end) return;
        Solution key=pop[begin];
        int i=begin;
        int j=end-1;
        while(j>i){
            while(j>i&&pop[j].time>=key.time)j--;
            pop[i]=pop[j];
            while(j>i&&pop[i].time<=key.time)i++;
            pop[j]=pop[i];
        }
        pop[i]=key;
        QsortTime(pop,begin,i);
        QsortTime(pop,i+1,end);
    }
}
