package com.po.mgra;

import com.po.general.Func;
import com.po.general.JSPOperator;
import com.po.general.Operator;

public class GraOperator extends Operator {
    public GraOperator(int cLength, Func func,int jobNum){
        super(cLength,func);
        alreadyAddNum=new int[jobNum];
    }
    public static int [] pbx(int [] c1,int [] c2,int [] sumSo,double pbxPossibility){
        int [] offspring= JSPOperator.generateAllNegativeOffspring(c1.length);
        clearAlreadyAddNum();
        for(int i=0;i<c1.length;++i){
            if(random.nextDouble()< pbxPossibility){
                offspring[i]=c1[i];
                ++alreadyAddNum[c1[i]];
            }
        }
        Operator.fillRest(offspring,c1,c2,alreadyAddNum,sumSo);
        return offspring;
    }
}
